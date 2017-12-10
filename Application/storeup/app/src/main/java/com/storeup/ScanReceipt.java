package com.storeup;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.storeup.Extras.CustomJSONObjectRequest;
import com.storeup.Extras.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Krishna.R.K on 11/18/2017.
 */

public class ScanReceipt extends Fragment implements View.OnClickListener{
    TextView test;
    String s;
    private static String KEY_SUCCESS = "success";
    /*private String url = "http://10.0.2.2:3000/ocr/getImageOcr";*/
    private String url = "https://storeup-server.herokuapp.com/ocr/getImageOcr";
    private String userChoosenTask;
    private int REQUEST_CAMERA = 1;
    private int UPLOAD_FLAG = 0;
    //
    private ImageView imageView;

    private FloatingActionButton buttonUpload;
    private int PICK_IMAGE_REQUEST = 2;
    private Uri filePath;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference uploadRef;
    AppSessionManager appSessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_scanreceipt_fragment,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Scan Receipt");

        appSessionManager=new AppSessionManager(getActivity().getApplicationContext());

        buttonUpload = (FloatingActionButton)getView().findViewById(R.id.upload_fab);

        imageView = (ImageView)getView().findViewById(R.id.imageView);


        //attaching listener
        buttonUpload.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }


    @Override
    public void onClick(View view) {
        if (view == buttonUpload) {
            uploadFile();
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Upload your Receipt!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    UPLOAD_FLAG = 0;
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    UPLOAD_FLAG = 1;
                    showFileChooser();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK ) {  /*&&&& data != null && data.getData() != null*/
            if (requestCode == PICK_IMAGE_REQUEST){
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bitmap=null;
        if (data != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis() + ".jpeg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        filePath = Uri.fromFile(destination);
        data.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
        imageView.setImageBitmap(thumbnail);
    }
    String imageName ="";
    private void uploadFile() {
        if (filePath != null) {

            final String email =appSessionManager.getKeyEmail();
            final String userId = appSessionManager.getUserId();

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            final String[] arr = filePath.toString().split("/");
            imageName = UUID.randomUUID().toString();
            if(UPLOAD_FLAG == 1) {
                uploadRef = storageReference.child("images/" + imageName);
                //Toast.makeText(getActivity().getApplicationContext(), "Storage Uri: " + arr[arr.length - 1], Toast.LENGTH_LONG).show();
            } else if (UPLOAD_FLAG == 0) {
                Random random = new Random();
                int key =random.nextInt(1000);
                uploadRef = storageReference.child("images/" +imageName );
                //Toast.makeText(getActivity().getApplicationContext(), "Storage Uri: " + "pic" + key, Toast.LENGTH_LONG).show();
            }
            uploadRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();

                            //and displaying a success toast
                            @SuppressWarnings("VisibleForTests")StorageReference downloadUri = taskSnapshot.getStorage();
                            @SuppressWarnings("VisibleForTests")final String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                            //Toast.makeText(getActivity().getApplicationContext(), "File Uploaded "+downloadUri.toString(), Toast.LENGTH_LONG).show();



                            CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.POST, url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if (response.getString(KEY_SUCCESS) != null) {
                                                    int success = Integer.parseInt(response.getString(KEY_SUCCESS));
                                                    if (success == 1) {
                                                        Toast.makeText(getActivity().getApplicationContext(), R.string.registered, Toast.LENGTH_LONG).show();

                                                    } else if (success == 0) {
                                                        Toast.makeText(getActivity().getApplicationContext(), R.string.email_exists, Toast.LENGTH_LONG).show();
                                                    }else if (success == 2) {
                                                        Toast.makeText(getActivity().getApplicationContext(), R.string.username_exists, Toast.LENGTH_LONG).show();
                                                    }else {
                                                        Toast.makeText(getActivity().getApplicationContext(), R.string.invalid_post, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Response Error", error.toString());
                                    Toast.makeText(getActivity().getApplicationContext(), R.string.invalid_post, Toast.LENGTH_LONG).show();
                                }
                            }) {

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    HashMap<String, String> headers = new HashMap<>();
                                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                                    headers.put("User-agent","My agent");
                                    return headers;
                                }

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("tag", "register");
                                    params.put("email", email);
                                    params.put("StorageReference",imageName);
                                    params.put("userId",userId);
                                    params.put("downloadUrl",downloadUrl);
                                    return params;
                                }

                            };
                            rq.setRetryPolicy(new DefaultRetryPolicy(
                                    0,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            VolleyController.getInstance(getActivity().getApplicationContext()).addToRequestQueue(rq);




                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Please select a file to upload", Toast.LENGTH_LONG).show();
        }
    }
}

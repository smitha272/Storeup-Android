package com.storeup;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.storeup.Helpers.ImageLoader;


/**
 * A simple {@link Fragment} subclass.
 */
public class Receipt_details extends Fragment {

    private ImageView imageView2;
    private TextView storeName;
    private TextView storeAddress;


    public Receipt_details() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_receipt_details, container, false);
        Bundle bundle = getArguments();
        String store_name = bundle.getString("store_name");
        String store_address = bundle.getString("store_address");
        String download_url = bundle.getString("download_url");
        imageView2 = (ImageView)view.findViewById(R.id.imageView2);
        storeName = (TextView)view.findViewById(R.id.storeName);
        storeAddress = (TextView)view.findViewById(R.id.storeAddress);

        storeName.setText(store_name);
        storeAddress.setText(store_address);

//        nameTextView.setText(currentUserReceiptDetails.getStore_name());
//        numberTextView.setText(currentUserReceiptDetails.getStore_address());
        new ImageLoader(download_url, imageView2).execute();

        return view;
    }

}

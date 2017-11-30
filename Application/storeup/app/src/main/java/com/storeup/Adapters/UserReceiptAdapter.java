package com.storeup.Adapters;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.storeup.Helpers.ImageLoader;
import com.storeup.R;
import com.storeup.Entity.UserReceiptDetails;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserReceiptAdapter extends ArrayAdapter<UserReceiptDetails> {

    private static final String LOG_TAG = UserReceiptAdapter.class.getSimpleName();
    public UserReceiptAdapter(Activity context, ArrayList<UserReceiptDetails> userReceipts) {
        super(context, 0, userReceipts);
    }
    boolean val = true;

    @Override
    public boolean isEnabled (int position) {

        return val;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.user_receipt_view, parent, false);
        }


        UserReceiptDetails currentUserReceiptDetails = getItem(position);
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.receipt_store_name);
        TextView numberTextView = (TextView) listItemView.findViewById(R.id.receipt_store_address);
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.receipt_icon);
        TextView errorView = (TextView) listItemView.findViewById(R.id.errorText);
        if(currentUserReceiptDetails.getStore_name()!=null){
            nameTextView.setText(currentUserReceiptDetails.getStore_name());
            numberTextView.setText(currentUserReceiptDetails.getStore_address());
            new ImageLoader(currentUserReceiptDetails.getDownload_url(), iconView).execute();
            errorView.setVisibility(View.INVISIBLE);
        }
        else {
            val = false;
            nameTextView.setVisibility(View.INVISIBLE);
            numberTextView.setVisibility(View.INVISIBLE);
            iconView.setVisibility(View.INVISIBLE);
            errorView.setText(Html.fromHtml("Sorry you don't have any scanned receipts! Please go to the scan receipts section to scan some."));
        }
        return listItemView;
    }

}

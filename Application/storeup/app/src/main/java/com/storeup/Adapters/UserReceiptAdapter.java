package com.storeup.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.storeup.Helpers.ImageLoader;
import com.storeup.R;
import com.storeup.Entity.UserReceiptDetails;
import java.util.ArrayList;

public class UserReceiptAdapter extends ArrayAdapter<UserReceiptDetails> {

    private static final String LOG_TAG = UserReceiptAdapter.class.getSimpleName();
    public UserReceiptAdapter(Activity context, ArrayList<UserReceiptDetails> userReceipts) {
        super(context, 0, userReceipts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.user_receipt_view, parent, false);
        }

        UserReceiptDetails currentUserReceiptDetails = getItem(position);
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.version_name);
        nameTextView.setText(currentUserReceiptDetails.getVersionName());

        TextView numberTextView = (TextView) listItemView.findViewById(R.id.version_number);
        numberTextView.setText(currentUserReceiptDetails.getVersionNumber());

        ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_item_icon);
        new ImageLoader(currentUserReceiptDetails.getImageResourceId(), iconView).execute();
        return listItemView;
    }

}

package com.storeup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.storeup.Entity.UserReceiptData;
import com.storeup.R;

import java.util.List;

public class UserReceiptHolder extends RecyclerView.ViewHolder {

    public ImageView scannedImages;

    public TextView errorText;

    public UserReceiptHolder(final View itemView, List<UserReceiptData> receiptData) {
        super(itemView);
        scannedImages = (ImageView) itemView.findViewById(R.id.scannedImages);
        errorText = (TextView) itemView.findViewById(R.id.errorText);
    }
}

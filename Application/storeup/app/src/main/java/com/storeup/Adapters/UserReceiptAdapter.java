package com.storeup.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storeup.Entity.UserReceiptData;
import com.storeup.Helpers.ImageLoader;
import com.storeup.R;

import java.util.List;

public class UserReceiptAdapter extends RecyclerView.Adapter<UserReceiptHolder>{
    private List<UserReceiptData> userReceiptData;

    protected Context context;

    public UserReceiptAdapter(Context context, List<UserReceiptData> userReceiptData) {
        this.userReceiptData = userReceiptData;
        this.context = context;
    }


    @Override
    public UserReceiptHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserReceiptHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_receipt_view, parent, false);
        viewHolder = new UserReceiptHolder(layoutView, userReceiptData);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final UserReceiptHolder holder, final int position) {
        System.out.print("Hey");
        if(userReceiptData.size()>=1 && userReceiptData.get(position).getReceipt_id()!=-1) {
            System.out.println("size>0");
            new ImageLoader(userReceiptData.get(position).getDownload_url(), holder.scannedImages).execute();
            holder.errorText.setVisibility(View.INVISIBLE);
        }
        else {
            System.out.println("size<0");
            holder.errorText.setText(Html.fromHtml("Sorry you don't have any scanned receipts! Please go to the scan receipts section to scan some."));
            holder.scannedImages.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.userReceiptData.size();
    }
}

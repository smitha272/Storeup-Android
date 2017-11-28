package com.storeup.Adapters;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.storeup.Entity.UserHomeDetails;
import com.storeup.R;

import java.util.ArrayList;

public class UserHomeAdapter extends ArrayAdapter<UserHomeDetails> {

    private static final String LOG_TAG = UserHomeAdapter.class.getSimpleName();
    public UserHomeAdapter(Activity context, ArrayList<UserHomeDetails> userHome) {
        super(context, 0, userHome);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.user_home_view, parent, false);
        }


        UserHomeDetails homedetails = getItem(position);
        TextView couponName = (TextView) listItemView.findViewById(R.id.couponName);
        TextView couponDesc = (TextView) listItemView.findViewById(R.id.couponDescription);
        ImageView couponIcon = (ImageView) listItemView.findViewById(R.id.coupon_image);

        couponDesc.setText("Store Name: "+homedetails.getStore());
        couponName.setText("You have Scanned: "+homedetails.getCount()+" Receipt");
        couponIcon.setImageResource(homedetails.getCoupon_icon());

        return listItemView;
    }

}

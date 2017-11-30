package com.storeup.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.storeup.Entity.UserCouponDetails;
import com.storeup.R;
import java.util.ArrayList;
import java.util.UUID;

public class UserCouponAdapter extends ArrayAdapter<UserCouponDetails> {

    private static final String LOG_TAG = UserCouponAdapter.class.getSimpleName();


    public UserCouponAdapter(Activity context, ArrayList<UserCouponDetails> userReceipts) {
        super(context, 0, userReceipts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.user_coupon_view, parent, false);
        }

        UserCouponDetails couponsToBeDisplayed = getItem(position);
        TextView couponName = (TextView) listItemView.findViewById(R.id.couponName);
        TextView couponDesc = (TextView) listItemView.findViewById(R.id.couponDescription);
        ImageView couponIcon = (ImageView) listItemView.findViewById(R.id.coupon_image);
        TextView couponCode = (TextView) listItemView.findViewById(R.id.couponCode);


        if(couponsToBeDisplayed.getIsRemainder()) {
            couponName.setText(couponsToBeDisplayed.getStore());
            couponDesc.setText("Sorry, you haven't scanned enough receipts yet. You need to scan just " + couponsToBeDisplayed.getCount() + " more receipts to win a new "+ couponsToBeDisplayed.getStore() + " coupon!");
            couponIcon.setImageResource(couponsToBeDisplayed.getCoupon_icon());
            couponCode.setVisibility(View.GONE);
        }
        else {
            couponName.setText(couponsToBeDisplayed.getStore());
            couponDesc.setText("Congratulations! You just won a " + couponsToBeDisplayed.getStore()  + " coupon!");
            String str = UUID.randomUUID().toString().toUpperCase().substring(0,8);


            couponCode.setText(str);

            if(couponsToBeDisplayed.getStore().equals("Costco")){
                couponIcon.setImageResource(R.drawable.costco);
            }
            else if(couponsToBeDisplayed.getStore().equals("Walmart")){
                couponIcon.setImageResource(R.drawable.costco);
            }
            else if(couponsToBeDisplayed.getStore().equals("Target")){
                couponIcon.setImageResource(R.drawable.costco);
            }
            else {
                couponIcon.setImageResource(R.drawable.store);
            }
        }
        return listItemView;
    }

}

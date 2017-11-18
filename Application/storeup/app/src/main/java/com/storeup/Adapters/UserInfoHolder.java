package com.storeup.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.storeup.Entity.UserProfileData;
import com.storeup.R;

import java.util.List;

public class UserInfoHolder extends RecyclerView.ViewHolder {

    public TextView userName;

    public TextView userPass;

    public TextView userAddress;

    public TextView userPhone;

    public UserInfoHolder(final View itemView, List<UserProfileData> userData) {
        super(itemView);
        /*userName = (TextView) itemView.findViewById(R.id.username);
        userPass = (TextView)itemView.findViewById(R.id.userpassword);
        userAddress = (TextView) itemView.findViewById(R.id.useraddress);
        userPhone = (TextView) itemView.findViewById(R.id.useraddress);*/
    }
}

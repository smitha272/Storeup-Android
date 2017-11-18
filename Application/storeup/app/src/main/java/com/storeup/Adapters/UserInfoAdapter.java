package com.storeup.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storeup.Entity.UserProfileData;
import com.storeup.R;


import java.util.List;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoHolder>{
    private List<UserProfileData> userProfileData;

    protected Context context;

    public UserInfoAdapter(Context context, List<UserProfileData> userProfileData) {
        this.userProfileData = userProfileData;
        this.context = context;
    }


    @Override
    public UserInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserInfoHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_view, parent, false);
        viewHolder = new UserInfoHolder(layoutView, userProfileData);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final UserInfoHolder holder, final int position) {

        holder.userName.setText(Html.fromHtml(userProfileData.get(position).getUsername()));
        holder.userPass.setText(Html.fromHtml(userProfileData.get(position).getPassword()));
        holder.userAddress.setText(Html.fromHtml(userProfileData.get(position).getStreet()  + userProfileData.get(position).getCity() + userProfileData.get(position).getState() + userProfileData.get(position).getZipcode()));
        holder.userPhone.setText(Html.fromHtml(userProfileData.get(position).getPhone()));

    }

    @Override
    public int getItemCount() {
        return this.userProfileData.size();
    }
}

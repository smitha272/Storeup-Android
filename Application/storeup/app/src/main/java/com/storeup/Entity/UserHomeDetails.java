package com.storeup.Entity;

public class UserHomeDetails {

    private String store;

    private String count;

    private int coupon_icon;

    private boolean isRemainder;

    public UserHomeDetails(String store, String count, int coupon_icon, boolean isRemainder)
    {
        this.store = store;
        this.count = count;
        this.coupon_icon = coupon_icon;
        this.isRemainder = isRemainder;
    }

    public String getStore() {
        return store;
    }

    public String getCount() {
        return count;
    }

    public int getCoupon_icon() {
        return coupon_icon;
    }

    public boolean getIsRemainder() {
        return isRemainder;
    }
}

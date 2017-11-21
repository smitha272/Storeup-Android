package com.storeup.Entity;

public class UserReceiptData {

    private int receipt_id;

    private String store_name;

    private String store_address;

    private String download_url;

    private String distance_traveled_by_user;

    public UserReceiptData(int receipt_id, String store_name, String store_address, String download_url, String distance_traveled_by_user) {
        this.receipt_id = receipt_id;
        this.store_name = store_name;
        this.store_address = store_address;
        this.download_url = download_url;
        this.distance_traveled_by_user = distance_traveled_by_user;
    }

    public int getReceipt_id() {
        return receipt_id;
    }
    public String getStore_name() {
        return store_name;
    }
    public String getStore_address() {
        return store_address;
    }
    public String getDownload_url() {
        return download_url;
    }
    public String getDistance_traveled_by_user() {
        return distance_traveled_by_user;
    }
}

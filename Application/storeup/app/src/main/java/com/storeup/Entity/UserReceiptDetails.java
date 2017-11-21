package com.storeup.Entity;

public class UserReceiptDetails {

    private String store_name;

    private String store_address;

    private String download_url;

    public UserReceiptDetails(String store_name, String store_address, String download_url)
    {
        this.store_name= store_name;
        this.store_address= store_address;
        this.download_url= download_url;
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
}

package com.storeup.Entity;

public class UserReceiptDetails {

    private String store_name;

    private String store_address;

    private String download_url;

    private String latitude="0";

    private String longitude="0";

    public UserReceiptDetails(String store_name, String store_address, String download_url)
    {
        this.store_name= store_name;
        this.store_address= store_address;
        this.download_url= download_url;
    }
    public UserReceiptDetails(String store_name, String store_address, String download_url, String latitude, String longitude)
    {
        this.store_name= store_name;
        this.store_address= store_address;
        this.download_url= download_url;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
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

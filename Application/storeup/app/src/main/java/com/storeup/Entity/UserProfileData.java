package com.storeup.Entity;

public class UserProfileData {

    private String username;
    
    private String password;

    private String street;

    private String city;

    private String state;

    private String phone;

    private String zipcode;

    public UserProfileData( String username,String password,String street,String city ,String state,String phone, String zipcode ) {
        this.username = username;
        this.password = password;
        this.street = street;
        this.city = city;
        this.state = state;
        this.phone = phone;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public  String getZipcode() { return zipcode; }

    public String getStreet() {
        return street;
    }

    public  String getUsername() {return username;}

    public  String getPassword() {return password;}

    public String getState() {
        return state;
    }

    public String getPhone() { return phone; }
}

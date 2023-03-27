package com.app.presidents;

public class President {
    private String name;
    private String imageURL;
    private String date;
    private String country;

    public President(String name, String imageURL, String date, String country) {
        this.name = name;
        this.imageURL = imageURL;
        this.date = date;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

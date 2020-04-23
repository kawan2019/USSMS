package com.example.ussms.Model;

import java.util.Date;

public class Notification {
    private String title;
    private String message;
    private String from;
    private String Photo;
    private String category;
    private String category_photo;
    private boolean status;
    private Date timestamp;

    public Notification(){}
    public Notification(String title, String message, String from, Date timestamp, String photo, String category, String category_photo, boolean status) {
        this.title = title;
        this.message = message;
        this.from = from;
        this.timestamp = timestamp;
        Photo = photo;
        this.category = category;
        this.category_photo = category_photo;
        this.status = status;
    }



    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_photo() {
        return category_photo;
    }

    public void setCategory_photo(String category_photo) {
        this.category_photo = category_photo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }




}

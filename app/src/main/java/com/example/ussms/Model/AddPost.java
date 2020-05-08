package com.example.ussms.Model;

import android.net.Uri;

public class AddPost {
    String imageName;
    Uri imageURI;

    public AddPost() {
    }

    public AddPost(String imageName, Uri imageURI) {
        this.imageName = imageName;
        this.imageURI = imageURI;
    }

    public String getImageName() {
        return imageName;
    }

    public Uri getImageURI() {
        return imageURI;
    }
}

package com.example.ussms.Model;

import java.util.List;

public class Posts {
    private List<String> Image;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPostOwnerName() {
        return PostOwnerName;
    }

    public void setPostOwnerName(String postOwnerName) {
        PostOwnerName = postOwnerName;
    }

    public String getPrivacy() {
        return Privacy;
    }

    public void setPrivacy(String privacy) {
        Privacy = privacy;
    }

    public String getPostOwnerImage() {
        return PostOwnerImage;
    }

    public void setPostOwnerImage(String postOwnerImage) {
        PostOwnerImage = postOwnerImage;
    }

    private String Description;
    private String PostOwnerName;
    private String Privacy;
    private String PostOwnerImage;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private String Id;




    public List<String> getImage() {
        return Image;
    }

    public void setImage(List<String> image) {
        Image = image;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    private String Department;

    public Posts(){}
    public Posts(List<String> imagesList,String department) {
        this.Image = imagesList;
        this.Department = department;
    }

    public List<String> getImagesList() {
        return Image;
    }

    public void setImagesList(List<String> imagesList) {
        this.Image = imagesList;
    }


}

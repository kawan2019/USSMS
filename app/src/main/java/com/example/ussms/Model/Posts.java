package com.example.ussms.Model;

import java.util.List;

public class Posts {
    private List<String> Image;

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

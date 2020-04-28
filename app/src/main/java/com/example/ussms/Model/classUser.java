package com.example.ussms.Model;

public class classUser {

    private String ClassName;
    private  String ClassOwner;


    private  String PhotoUser;




    private classUser() {
    }

    private classUser(String className, String ClassOwner,String PhotoUser ) {
        this.ClassName = ClassName;
        this.ClassOwner = ClassOwner;
        this.PhotoUser = PhotoUser;


    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getClassOwner() {
        return ClassOwner;
    }

    public void setClassOwner(String classOwner) {
        ClassOwner = classOwner;
    }

    public String getPhotoUser() {
        return PhotoUser;
    }

    public void setPhotoUser(String photoUser) {
        PhotoUser = photoUser;
    }

}
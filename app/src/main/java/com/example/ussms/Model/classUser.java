package com.example.ussms.Model;

public class classUser {

    private String ClassName;
    private  String ClassOwner;
    private String IMAGE;


    private classUser() {
    }

    private classUser(String className, String ClassOwner, String IMAGE) {
        this.ClassName = ClassName;
        this.ClassOwner = ClassOwner;
        this.IMAGE = IMAGE;

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

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }


}
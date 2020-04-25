package com.example.ussms.Model;

public class classUser {

    private String ClassName;

    private classUser() {
    }

    private classUser(String className) {
        this.ClassName = ClassName;

    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }


}
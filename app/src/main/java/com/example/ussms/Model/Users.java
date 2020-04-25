package com.example.ussms.Model;

public class Users  {
    private String USERNAME;
    private String UID;
    private String FULLNAME;
    private String IMAGE;
    private String ClassName;
    private  String ClassOwner;



    private Users(){
    }
    private Users(String USERNAME,String UID,String FULLNAME,String IMAGE,String ClassName , String ClassOwner){
        this.USERNAME = USERNAME;
        this.UID = UID;
        this.FULLNAME = FULLNAME;
        this.IMAGE = IMAGE;
        this.ClassName = ClassName;
        this.ClassOwner = ClassOwner;



    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getFULLNAME() {
        return FULLNAME;
    }

    public void setFULLNAME(String FULLNAME) {
        this.FULLNAME = FULLNAME;
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



}

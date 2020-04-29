package com.example.ussms.Model;

public class classUser {

    private String ClassName;
    private  String ClassOwner;
    private  String PhotoUser;
    private  long ClassLevel;
    private String ClassDepartment;

    private classUser() {
    }
    private classUser(String className, String ClassOwner,String PhotoUser, String ClassDepartment,long ClassLevel ) {
        this.ClassName = ClassName;
        this.ClassOwner = ClassOwner;
        this.PhotoUser = PhotoUser;
        this.ClassDepartment = ClassDepartment;
        this.ClassLevel =ClassLevel;

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


    public String getClassDepartment() {
        return ClassDepartment;
    }

    public void setClassDepartment(String classDepartment) {
        ClassDepartment = classDepartment;
    }

    public long getClassLevel() {
        return ClassLevel;
    }

    public void setClassLevel(long classLevel) {
        ClassLevel = classLevel;
    }

}
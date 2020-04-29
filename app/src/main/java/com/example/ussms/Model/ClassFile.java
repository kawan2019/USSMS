package com.example.ussms.Model;

public class ClassFile {



    private String FileOwner;
    private String File;
    private String CreateTime;
    private String FDescription;
    private String PhotoUser;
    private String FDepartment;
    private long FLevel;

    private ClassFile(){

    }

    private ClassFile(String fileOwner, String file, String createTime, String FDescription, String photoUser, String FDepartment, long FLevel) {
        this.FileOwner = fileOwner;
        this.File = file;
        this.CreateTime = createTime;
        this.FDescription = FDescription;
        this.PhotoUser = photoUser;
        this.FDepartment = FDepartment;
        this.FLevel = FLevel;
    }

    public String getFileOwner() {
        return FileOwner;
    }

    public void setFileOwner(String fileOwner) {
        FileOwner = fileOwner;
    }

    public String getFile() {
        return File;
    }

    public void setFile(String file) {
        File = file;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getFDescription() {
        return FDescription;
    }

    public void setFDescription(String FDescription) {
        this.FDescription = FDescription;
    }

    public String getPhotoUser() {
        return PhotoUser;
    }

    public void setPhotoUser(String photoUser) {
        PhotoUser = photoUser;
    }

    public String getFDepartment() {
        return FDepartment;
    }

    public void setFDepartment(String FDepartment) {
        this.FDepartment = FDepartment;
    }

    public long getFLevel() {
        return FLevel;
    }

    public void setFLevel(long FLevel) {
        this.FLevel = FLevel;
    }

}

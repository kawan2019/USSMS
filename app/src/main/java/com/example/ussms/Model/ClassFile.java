package com.example.ussms.Model;

import java.util.Date;

public class ClassFile {
    private String FileOwner;
    private String File;
    private Date CreateTime;
    private String FDescription;
    private String PhotoUser;
    private String FDepartment;
    private long FLevel;

    private ClassFile(){ }

    private ClassFile(String fileOwner, String file, String FDescription,Date CreateTime, String photoUser, String FDepartment, long FLevel) {
        this.FileOwner = fileOwner;
        this.File = file;

        this.FDescription = FDescription;
        this.PhotoUser = photoUser;
        this.FDepartment = FDepartment;
        this.FLevel = FLevel;
        this.CreateTime =CreateTime;
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

    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date createTime) {
        CreateTime = createTime;
    }


}

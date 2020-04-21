package com.example.ussms.Model;

public class Users {
    private String USERNAME;
    private String UID;
    private String FULLNAME;
    private String IMAGE;

    private Users(){
    }
    private Users(String USERNAME,String UID,String FULLNAME,String IMAGE){
        this.USERNAME = USERNAME;
        this.UID = UID;
        this.FULLNAME = FULLNAME;
        this.IMAGE = IMAGE;

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



}

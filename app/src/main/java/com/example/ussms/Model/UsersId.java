package com.example.ussms.Model;

import androidx.annotation.NonNull;

public class UsersId {

    public String userId;

    public <T extends UsersId> T withId(@NonNull final String id) {
        this.userId = id;
        return (T) this;
    }
}
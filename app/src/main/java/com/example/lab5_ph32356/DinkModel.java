package com.example.lab5_ph32356;

import com.google.gson.annotations.SerializedName;

public class DinkModel {
    @SerializedName("_id")
    private String id;
    private String name;

    public DinkModel(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

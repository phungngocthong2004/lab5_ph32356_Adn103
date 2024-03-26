package com.example.lab5_ph32356;

import java.util.List;

public class ApiResponse {
    private int status;
    private String messenger;
    private List<DinkModel> data;

    public ApiResponse(int status, String messenger, List<DinkModel> data) {
        this.status = status;
        this.messenger = messenger;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public List<DinkModel> getData() {
        return data;
    }

    public void setData(List<DinkModel> data) {
        this.data = data;
    }
}
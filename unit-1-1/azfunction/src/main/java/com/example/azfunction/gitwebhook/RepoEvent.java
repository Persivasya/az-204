package com.example.azfunction.gitwebhook;

public class RepoEvent {

    private String id;
    private String payload;

    public RepoEvent() {
    }

    public RepoEvent(String id, String payload) {
        this.id = id;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}

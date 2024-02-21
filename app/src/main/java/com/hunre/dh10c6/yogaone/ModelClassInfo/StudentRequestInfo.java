package com.hunre.dh10c6.yogaone.ModelClassInfo;

public class StudentRequestInfo {

    private String id;
    private String name;
    private String status;

    public StudentRequestInfo() {
        // Cần phải có constructor mặc định cho việc chuyển đổi từ Firestore
    }

    public StudentRequestInfo(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    // Getters và Setters

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


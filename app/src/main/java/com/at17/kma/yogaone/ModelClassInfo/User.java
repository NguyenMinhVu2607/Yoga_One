package com.at17.kma.yogaone.ModelClassInfo;

import java.util.List;

public class User {
    private String fullName;
    private List<String> classIds;

    public User() {
        // Empty constructor needed for Firestore
    }

    public User(String fullName, List<String> classIds) {
        this.fullName = fullName;
        this.classIds = classIds;
    }

    public String getFullName() {
        return fullName;
    }

    public List<String> getClassIds() {
        return classIds;
    }
}

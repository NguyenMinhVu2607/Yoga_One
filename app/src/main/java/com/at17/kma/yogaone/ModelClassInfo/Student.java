package com.at17.kma.yogaone.ModelClassInfo;

import java.util.List;

public class Student {
    private String id;
    private String name;
    private String status;

    public Student(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}



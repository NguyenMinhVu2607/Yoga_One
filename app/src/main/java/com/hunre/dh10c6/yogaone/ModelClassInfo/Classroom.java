package com.hunre.dh10c6.yogaone.ModelClassInfo;

import java.util.List;

public class Classroom {
    private String id;
    private String name;
    private String status;
    private List<Student> students;

    public Classroom(String id, String name, String status, List<Student> students) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.students = students;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
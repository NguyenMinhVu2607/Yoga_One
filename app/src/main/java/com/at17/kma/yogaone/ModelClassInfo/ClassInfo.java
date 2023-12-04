package com.at17.kma.yogaone.ModelClassInfo;

public class ClassInfo {
    private String className;
    private String teacherName;
    private String location;
    private long startDay;
    private long endDay;
    private String dayOfWeek;

    // Constructors
    public ClassInfo() {
        // Default constructor required for Firestore
    }

    public ClassInfo(String className, String teacherName, String location, long startDay, long endDay, String dayOfWeek) {
        this.className = className;
        this.teacherName = teacherName;
        this.location = location;
        this.startDay = startDay;
        this.endDay = endDay;
        this.dayOfWeek = dayOfWeek;
    }

    // Getters and setters
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getStartDay() {
        return startDay;
    }

    public void setStartDay(long startDay) {
        this.startDay = startDay;
    }

    public long getEndDay() {
        return endDay;
    }

    public void setEndDay(long endDay) {
        this.endDay = endDay;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}

package com.at17.kma.yogaone.ModelClassInfo;

import java.util.List;

public class ClassInfo {
    private String documentId;  // Trường mới để lưu trữ ID

    private String className;
    private List<String> dayOfWeek;
    private long endDay;
    private String location;
    private long startDay;
    private String teacherName;
    private String timeStringEnd;
    private String timeStringStart;

    // Constructors
    public ClassInfo() {
        // Default constructor required for Firestore
    }

    public ClassInfo(String documentId, String className, List<String> dayOfWeek, long endDay, String location, long startDay, String teacherName, String timeStringEnd, String timeStringStart) {
        this.documentId = documentId;
        this.className = className;
        this.dayOfWeek = dayOfWeek;
        this.endDay = endDay;
        this.location = location;
        this.startDay = startDay;
        this.teacherName = teacherName;
        this.timeStringEnd = timeStringEnd;
        this.timeStringStart = timeStringStart;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(List<String> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public long getEndDay() {
        return endDay;
    }

    public void setEndDay(long endDay) {
        this.endDay = endDay;
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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTimeStringEnd() {
        return timeStringEnd;
    }

    public void setTimeStringEnd(String timeStringEnd) {
        this.timeStringEnd = timeStringEnd;
    }

    public String getTimeStringStart() {
        return timeStringStart;
    }

    public void setTimeStringStart(String timeStringStart) {
        this.timeStringStart = timeStringStart;
    }
}

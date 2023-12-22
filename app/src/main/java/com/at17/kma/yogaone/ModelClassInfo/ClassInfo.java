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
    private String teacherId;  // Thêm trường mới

    private String timeStringEnd;
    private String timeStringStart;
    // Thêm trường students
    private List<StudentInfo> students;
    // Trong lớp ClassInfo
    private boolean conflict;

    public boolean isConflict() {
        return conflict;
    }

    public void setConflict(boolean conflict) {
        this.conflict = conflict;
    }
    private int backgroundColor; // Thêm thuộc tính mới

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    // Constructors
    public ClassInfo() {
        // Default constructor required for Firestore
    }

    public ClassInfo(String documentId, String className, List<String> dayOfWeek, long endDay, String location, long startDay, String teacherName, String teacherId, String timeStringEnd, String timeStringStart, List<StudentInfo> students, boolean conflict) {
        this.documentId = documentId;
        this.className = className;
        this.dayOfWeek = dayOfWeek;
        this.endDay = endDay;
        this.location = location;
        this.startDay = startDay;
        this.teacherName = teacherName;
        this.teacherId = teacherId;
        this.timeStringEnd = timeStringEnd;
        this.timeStringStart = timeStringStart;
        this.students = students;
        this.conflict = conflict;
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

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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

    public List<StudentInfo> getStudents() {
        return students;
    }

    public void setStudents(List<StudentInfo> students) {
        this.students = students;
    }


}

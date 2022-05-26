package com.example.campus.model;

import com.example.campus.protoModel.CourseDetail;

public class CourseDetailModel {
    private long courseId;
    private String avatarUrl;
    private String evaluatorName;
    private String attendanceFrequency;
    private String attendanceWay;
    private String examWay;
    private String examGivenGrades;
    private float credit;
    private int courseScore;
    private int likeCount;
    private String courseEvaluateWords;
    private String date;

    public CourseDetailModel(CourseDetail.CourseDetailResponseItem item){
        if (item == null){
            return;
        }
        courseId = item.getCourseId();
//        private String avatarUrl;
//        private String evaluatorName;
//        private String attendanceFrequency;
//        private String attendanceWay;
//        private String examWay;
//        private String examGivenGrades;
//        private float credit;
//        private int courseScore;
//        private int likeCount;
//        private String courseEvaluateWords;
//        private String date;
    }
    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getEvaluatorName() {
        return evaluatorName;
    }

    public void setEvaluatorName(String evaluatorName) {
        this.evaluatorName = evaluatorName;
    }

    public String getAttendanceFrequency() {
        return attendanceFrequency;
    }

    public void setAttendanceFrequency(String attendanceFrequency) {
        this.attendanceFrequency = attendanceFrequency;
    }

    public String getAttendanceWay() {
        return attendanceWay;
    }

    public void setAttendanceWay(String attendanceWay) {
        this.attendanceWay = attendanceWay;
    }

    public String getExamWay() {
        return examWay;
    }

    public void setExamWay(String examWay) {
        this.examWay = examWay;
    }

    public String getExamGivenGrades() {
        return examGivenGrades;
    }

    public void setExamGivenGrades(String examGivenGrades) {
        this.examGivenGrades = examGivenGrades;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public String getCourseEvaluateWords() {
        return courseEvaluateWords;
    }

    public void setCourseEvaluateWords(String courseEvaluateWords) {
        this.courseEvaluateWords = courseEvaluateWords;
    }

    public int getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(int courseScore) {
        this.courseScore = courseScore;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

package com.example.btl.models;

import com.google.gson.annotations.SerializedName;

public class Score {

    @SerializedName("_id")
    private String id; // Đổi từ int sang String và map với _id của MongoDB

    private String studentId;
    private String subjectCode;
    private String subjectName;
    private int    credits;
    private double midtermScore;
    private double finalScore;
    private double averageScore;
    private String grade;
    private String semester;

    public Score() {}

    // Getters
    public String getId()           { return id; } // Đổi kiểu trả về thành String
    public String getStudentId()    { return studentId; }
    public String getSubjectCode()  { return subjectCode; }
    public String getSubjectName()  { return subjectName; }
    public int    getCredits()      { return credits; }
    public double getMidtermScore() { return midtermScore; }
    public double getFinalScore()   { return finalScore; }
    public double getAverageScore() { return averageScore; }
    public String getGrade()        { return grade; }
    public String getSemester()     { return semester; }

    // Setters
    public void setId(String id)                       { this.id = id; }
    public void setStudentId(String studentId)      { this.studentId = studentId; }
    public void setSubjectCode(String subjectCode)  { this.subjectCode = subjectCode; }
    public void setSubjectName(String subjectName)  { this.subjectName = subjectName; }
    public void setCredits(int credits)             { this.credits = credits; }
    public void setMidtermScore(double midtermScore){ this.midtermScore = midtermScore; }
    public void setFinalScore(double finalScore)    { this.finalScore = finalScore; }
    public void setAverageScore(double averageScore){ this.averageScore = averageScore; }
    public void setGrade(String grade)              { this.grade = grade; }
    public void setSemester(String semester)        { this.semester = semester; }

    // Tính điểm TB: 40% giữa kỳ + 60% cuối kỳ
    public double calcAverage() {
        return Math.round((midtermScore * 0.4 + finalScore * 0.6) * 10.0) / 10.0;
    }

    // Xếp loại theo thang điểm 10
    public static String calcGrade(double avg) {
        if (avg >= 8.5) return "A";
        if (avg >= 7.0) return "B";
        if (avg >= 5.5) return "C";
        if (avg >= 4.0) return "D";
        return "F";
    }
}

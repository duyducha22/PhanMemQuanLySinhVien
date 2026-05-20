package com.example.btl.models;

public class Student {
    private String studentId;   // Dạng A12345
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String className;
    private String major;
    private String email;
    private String phone;
    private String address;
    private String academicYear;
    private String status;      // "active" | "graduated" | "suspended"

    public Student() {}

    // Getters
    public String getStudentId()    { return studentId; }
    public String getFullName()     { return fullName; }
    public String getDateOfBirth()  { return dateOfBirth; }
    public String getGender()       { return gender; }
    public String getClassName()    { return className; }
    public String getMajor()        { return major; }
    public String getEmail()        { return email; }
    public String getPhone()        { return phone; }
    public String getAddress()      { return address; }
    public String getAcademicYear() { return academicYear; }
    public String getStatus()       { return status; }

    // Setters
    public void setStudentId(String studentId)       { this.studentId = studentId; }
    public void setFullName(String fullName)         { this.fullName = fullName; }
    public void setDateOfBirth(String dateOfBirth)   { this.dateOfBirth = dateOfBirth; }
    public void setGender(String gender)             { this.gender = gender; }
    public void setClassName(String className)       { this.className = className; }
    public void setMajor(String major)               { this.major = major; }
    public void setEmail(String email)               { this.email = email; }
    public void setPhone(String phone)               { this.phone = phone; }
    public void setAddress(String address)           { this.address = address; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public void setStatus(String status)             { this.status = status; }

    // Lấy chữ cái đầu để hiện avatar
    public String getInitial() {
        if (fullName != null && !fullName.isEmpty()) {
            String[] parts = fullName.trim().split("\\s+");
            return parts[parts.length - 1].substring(0, 1).toUpperCase();
        }
        return "?";
    }
}

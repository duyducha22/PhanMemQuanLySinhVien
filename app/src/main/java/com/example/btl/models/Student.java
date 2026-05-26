package com.example.btl.models;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("_id")
    private String id; // ID tự sinh của MongoDB

    private String studentId;
    private String fullName;
    private String dob;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private String className;
    private String major;
    private int enrollmentYear;
    private String status;

    // Constructor rỗng (Bắt buộc phải có để Firebase/Retrofit đọc dữ liệu)
    public Student() {
    }

    // Constructor đầy đủ (Để anh em mình tạo object sinh viên mới gửi lên server)
    public Student(String studentId, String fullName, String dob, String gender, String email, String phone, String address, String className, String major, int enrollmentYear, String status) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.className = className;
        this.major = major;
        this.enrollmentYear = enrollmentYear;
        this.status = status;
    }

    // --- Các hàm Getter & Setter ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public int getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(int enrollmentYear) { this.enrollmentYear = enrollmentYear; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; };

    // Lấy chữ cái đầu để hiện avatar
    public String getInitial() {
        if (fullName != null && !fullName.isEmpty()) {
            String[] parts = fullName.trim().split("\\s+");
            return parts[parts.length - 1].substring(0, 1).toUpperCase();
        }
        return "?";
    }
}


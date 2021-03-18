package com.example.ansolienapp.Model;

public class Relative {
    String fullName, email, password, phone, patientEmail, image;

    public Relative() {
    }

    public Relative(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }

    public Relative(String fullName, String email, String password, String phone, String patientEmail, String image) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.patientEmail = patientEmail;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
}

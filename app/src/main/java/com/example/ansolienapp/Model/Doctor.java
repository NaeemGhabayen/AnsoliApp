package com.example.ansolienapp.Model;

public class Doctor {
    String fullName, email, password, phone, clinicPhone, image;

    public Doctor() {
    }

    public Doctor(String fullName, String email, String password, String phone, String clinicPhone, String image) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.clinicPhone = clinicPhone;
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

    public String getClinicPhone() {
        return clinicPhone;
    }

    public void setClinicPhone(String clinicPhone) {
        this.clinicPhone = clinicPhone;
    }
}

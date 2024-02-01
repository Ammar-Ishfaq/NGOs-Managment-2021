package com.ammar.fyp.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUser {

    @SerializedName("accountType")
    @Expose
    private String accountType;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("isAllow")
    @Expose
    private String isAllow;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("longlat")
    @Expose
    private String longlat;
    @SerializedName("numberEasyPaissa")
    @Expose
    private String numberEasyPaissa;
    @SerializedName("numberJazzCash")
    @Expose
    private String numberJazzCash;
    @SerializedName("numberPersonal")
    @Expose
    private String numberPersonal;
    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsAllow() {
        return isAllow;
    }

    public void setIsAllow(String isAllow) {
        this.isAllow = isAllow;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLonglat() {
        return longlat;
    }

    public void setLonglat(String longlat) {
        this.longlat = longlat;
    }

    public String getNumberEasyPaissa() {
        return numberEasyPaissa;
    }

    public void setNumberEasyPaissa(String numberEasyPaissa) {
        this.numberEasyPaissa = numberEasyPaissa;
    }

    public String getNumberJazzCash() {
        return numberJazzCash;
    }

    public void setNumberJazzCash(String numberJazzCash) {
        this.numberJazzCash = numberJazzCash;
    }

    public String getNumberPersonal() {
        return numberPersonal;
    }

    public void setNumberPersonal(String numberPersonal) {
        this.numberPersonal = numberPersonal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

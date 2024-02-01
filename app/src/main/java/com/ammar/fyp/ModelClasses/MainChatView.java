package com.ammar.fyp.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Here 1/One Means The Chat Refers To the Donor
 */
public class MainChatView {
    @Expose
    @SerializedName("name2")
    private String name2;
    @Expose
    @SerializedName("name1")
    private String name1;
    @Expose
    @SerializedName("image2")
    private String image2;
    @Expose
    @SerializedName("image1")
    private String image1;
    @Expose
    @SerializedName("email2")
    private String email2;
    @Expose
    @SerializedName("email1")
    private String email1;
    @Expose
    @SerializedName("combineEmail")
    private String combineEmail;
    @Expose
    @SerializedName("blockBy")
    private String blockby;

    public String getBlockby() {
        return blockby;
    }

    public void setBlockby(String blockby) {
        this.blockby = blockby;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getCombineemail() {
        return combineEmail;
    }

    public void setCombineemail(String combineemail) {
        this.combineEmail = combineemail;
    }
}

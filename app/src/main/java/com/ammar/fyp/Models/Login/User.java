package com.ammar.fyp.Models.Login;

public class User {
    private String email;
    private String password;
    private static final User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPassword() {
        return password;
    }


}

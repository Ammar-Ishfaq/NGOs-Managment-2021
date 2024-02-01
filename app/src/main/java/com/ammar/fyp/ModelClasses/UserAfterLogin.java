package com.ammar.fyp.ModelClasses;

/**
 *
 */
public class UserAfterLogin {
    ModelUser modelUser = new ModelUser();

    public ModelUser getModelUser() {
        return modelUser;
    }

    public void setModelUser(ModelUser modelUser) {
        this.modelUser = modelUser;
    }

    private static final UserAfterLogin ourInstance = new UserAfterLogin();

    public static UserAfterLogin getInstance() {
        return ourInstance;
    }

    private UserAfterLogin() {


    }
}

package com.ammar.fyp.Interfaces;

import com.ammar.fyp.ModelClasses.ModelUser;

public interface getSingleUserDetailByEmail {
    void onSuccess(ModelUser modelUser);

    void onfail(String msg);
}

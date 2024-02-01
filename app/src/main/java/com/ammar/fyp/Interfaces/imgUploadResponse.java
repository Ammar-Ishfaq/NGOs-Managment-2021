package com.ammar.fyp.Interfaces;

public interface imgUploadResponse {
    void onSuccess(String urlImage);

    void onFail(String msg);
}
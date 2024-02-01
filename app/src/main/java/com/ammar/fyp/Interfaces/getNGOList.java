package com.ammar.fyp.Interfaces;

import com.ammar.fyp.ModelClasses.ModelUser;

import java.util.List;

public interface getNGOList {
    void NGOList(List<ModelUser> NGOs);
    void onFail(String msg);
}

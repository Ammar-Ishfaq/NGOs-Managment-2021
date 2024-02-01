package com.ammar.fyp.Interfaces;

import com.ammar.fyp.ModelClasses.SubChatView;

public interface getLastPayment {
    void onSucces(SubChatView model);

    void onFail(String msg);
}

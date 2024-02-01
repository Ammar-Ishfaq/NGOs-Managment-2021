package com.ammar.fyp.viewmodels.login;


import android.text.TextUtils;
import android.util.Patterns;

import com.ammar.fyp.BR;
import com.ammar.fyp.Models.Login.User;
import com.ammar.fyp.Tools.Firebase;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

//Login uses MVVM Archetecture for the sake of best security Achiveness
public class LoginViewModel extends BaseObservable {


    private User user;//main pojo/model class of the user
    private Firebase mfirebase;
    private innerLogics innerLogics;//classs holds the main logics of my code
    @Bindable
    private boolean isAllow = false;//will allow the user to move to next activity
    @Bindable
    private boolean loading = false;//will allow the user to move to next activity
    @Bindable
    private boolean signup = false;//will allow the user to move to next activity
    @Bindable
    private boolean resetPassword = false;//will allow the user to move to next activity

    public LoginViewModel() {
        user = User.getInstance();
        mfirebase = new Firebase();
        innerLogics = new innerLogics();

        setLoading(false);
        setSignup(false);
        setAllow(false);
        setResetPassword(false);
    }

    public boolean getSignup() {
        return signup;
    }

    public void setSignup(boolean signup) {
        this.signup = signup;
        notifyPropertyChanged(BR.signup);
    }

    public boolean getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(boolean resetPassword) {
        this.resetPassword = resetPassword;
        notifyPropertyChanged(BR.resetPassword);
    }

    public boolean getLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }


    public boolean getIsAllow() {
        return isAllow;
    }

    public void setAllow(boolean allow) {
        isAllow = allow;
        notifyPropertyChanged(BR.isAllow);
    }

    @Bindable
    public String getUserEmail() {
        return user.getEmail();
    }

    @Bindable
    public String getUserPassword() {
        return user.getPassword();
    }


    public void setUserEmail(String email) {
        user.setEmail(email);
        notifyPropertyChanged(BR.userEmail);
    }

    public void setUserPassword(String password) {
        user.setPassword(password);
        notifyPropertyChanged(BR.userPassword);
    }

    /**
     * This is used for the click events that held on login click
     */
    public void onLoginClicked() {
        setLoading(true);
        if (innerLogics.isInputDataValid()) {
            new innerLogics().checkEmail(new callBack() {
                @Override
                public void onSuccess(String msg) {

                    setLoading(false);
                    setAllow(true);
                }

                @Override
                public void onFail(String msg) {
                    setLoading(false);
                    setAllow(false);

                }
            });
        } else {
            setLoading(false);
            setAllow(false);
        }
    }

    public void onResetClick() {
        setResetPassword(true);
    }

    public void onSignUp() {
        setSignup(true);
    }

    /**
     * Interface for the firebase successfull call backs
     */
    private interface callBack {
        void onSuccess(String msg);

        void onFail(String msg);
    }

    /**
     * Holds the logics of the Screen
     * <p>Specially for the Code to run smoothly ;)  </p>
     */
    public class innerLogics {
        /**
         * Will check that the data is valid or not
         *
         * @return
         */
        public boolean isInputDataValid() {
            return !TextUtils.isEmpty(getUserEmail()) && Patterns.EMAIL_ADDRESS.matcher(getUserEmail()).matches() && getUserPassword().length() > 5;
        }

        private void checkEmail(callBack obj) {
            mfirebase.getmAuth().signInWithEmailAndPassword(getUserEmail(), getUserPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            obj.onSuccess("Login Successfully");
                        } else {
                            obj.onFail("Login Failed");
                        }
                    });
        }


    }


}

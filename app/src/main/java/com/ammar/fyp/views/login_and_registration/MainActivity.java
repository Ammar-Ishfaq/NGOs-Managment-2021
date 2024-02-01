package com.ammar.fyp.views.login_and_registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ammar.fyp.Interfaces.isNGO;
import com.ammar.fyp.Interfaces.response;
import com.ammar.fyp.Models.Login.User;
import com.ammar.fyp.ModuleDONOR.DONORMain;
import com.ammar.fyp.ModuleNGO.NGOMain;
import com.ammar.fyp.ModuleRegister.Register;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.Firebase;
import com.ammar.fyp.Tools.FirebaseCrud;
import com.ammar.fyp.databinding.MainActivityBinding;
import com.ammar.fyp.viewmodels.login.LoginViewModel;
import com.ammar.fyp.views.Others.blockPage;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {
    private static FirebaseCrud firebaseCrud;
    private static Firebase firebase;
    private static boolean isShowErr = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        isShowErr = false;
        MainActivityBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
//        activityMainBinding.setVariable(Integer.parseInt("user"), new LoginViewModel());
        activityMainBinding.setLoginModel(new LoginViewModel());
        activityMainBinding.executePendingBindings();
        firebaseCrud = new FirebaseCrud().getInstance();
        firebase = new Firebase();

    }


    @BindingAdapter({"checkCredentials"})
    public static void login(View view, boolean isAllow) {
        if (isAllow) {
            String Email = User.getInstance().getEmail().replace
                    (".", ",");
            Snackbar.make(view,
                    "Please Wait...",
                    Snackbar.LENGTH_LONG)
                    .show();
            firebaseCrud.CheckAccountType(Email, new isNGO() {
                @Override
                public void NGO(String msg) {
                    runActivity(Email, new Intent(view.getContext(), NGOMain.class), view);

                }

                @Override
                public void DONOR(String msg) {
                    runActivity(Email, new Intent(view.getContext(), DONORMain.class), view);
                }

                @Override
                public void Fail(String msg) {
                    Snackbar.make(view,
                            msg,
                            Snackbar.LENGTH_LONG)
                            .show();
                }
            });

        } else {
            if (isShowErr)
                Snackbar.make(view,
                        "Failed To Login",
                        Snackbar.LENGTH_LONG)
                        .setAction("Forget Password?", v -> {
                            Intent intent = new Intent(view.getContext(), ResetPassword.class);
                            view.getContext().startActivity(intent);
                        })
                        .show();
            isShowErr = true;
        }
//        Toast.makeText(view.getContext(), "isAllow:" + isAllow, Toast.LENGTH_SHORT).show();
    }

    @BindingAdapter({"gotoSignup"})
    public static void Signup(View view, boolean SingUp) {
        if (SingUp) {
            Intent intent = new Intent(view.getContext(), Register.class);
            view.getContext().startActivity(intent);
        }
//        Toast.makeText(view.getContext(), "SingUp" + SingUp, Toast.LENGTH_SHORT).show();
    }

    @BindingAdapter({"gotoResetPassword"})
    public static void reset(View view, boolean resetPassword) {
        if (resetPassword) {
            Intent intent = new Intent(view.getContext(), ResetPassword.class);
            view.getContext().startActivity(intent);
        }
    }

    /**
     * Manage the blockage of the User
     *
     * @param Email
     * @param intent
     * @param view
     */
    static void runActivity(String Email, Intent intent, View view) {
        Snackbar.make(view,
                "Logging in...",
                Snackbar.LENGTH_LONG)
                .show();
        //for the removing password from the textfields i've to set that in the model class bcz of MVVM architecture
        User.getInstance().setEmail("");
        User.getInstance().setPassword("");

        FirebaseCrud.getInstance().isBlockUser(new response() {
            @Override
            public void onSuccess(String msg) {
                Intent intent = new Intent(view.getContext(), blockPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                view.getContext().startActivity(intent);
            }

            @Override
            public void onFail(String msg) {//means that the user is  not block run the following code
                firebase.getmRef().child("User").child(Email).child("deviceToken").setValue(FirebaseInstanceId.getInstance().getToken());
                view.getContext().startActivity(intent);

            }
        });
    }
}
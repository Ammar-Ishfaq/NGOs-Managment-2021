package com.ammar.fyp.views.Others;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.ammar.fyp.Interfaces.isNGO;
import com.ammar.fyp.Interfaces.response;
import com.ammar.fyp.ModuleDONOR.DONORMain;
import com.ammar.fyp.ModuleNGO.NGOMain;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.Firebase;
import com.ammar.fyp.Tools.FirebaseCrud;
import com.ammar.fyp.views.login_and_registration.MainActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {
    private Firebase firebase;
    private static FirebaseCrud firebaseCrud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        firebase = new Firebase();
        firebaseCrud = new FirebaseCrud().getInstance();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                } finally {
                    checkAlreadySignin();
                }

            }
        };
        thread.start();
    }

    /**
     * IF the USer is alredy Signin THen Move to the NExtWindow either NGO/DONOR
     */
    void checkAlreadySignin() {
        try {
            final String[] Email = {firebase.getmAuth().getCurrentUser().getEmail().replace(".", ",")};
            if (Email[0].length() > 5) {
                firebaseCrud.CheckAccountType(Email[0], new isNGO() {
                    @Override
                    public void NGO(String msg) {
                        runActivity(Email[0], new Intent(splashscreen.this, NGOMain.class));

                    }

                    @Override
                    public void DONOR(String msg) {
                        runActivity(Email[0], new Intent(splashscreen.this, DONORMain.class));
                    }

                    @Override
                    public void Fail(String msg) {
                        Intent intent = new Intent(splashscreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                Intent intent = new Intent(splashscreen.this, MainActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            Intent intent = new Intent(splashscreen.this, MainActivity.class);
            startActivity(intent);
        }
    }

    void runActivity(String Email, Intent intent) {
        FirebaseCrud.getInstance().isBlockUser(new response() {
            @Override
            public void onSuccess(String msg) {
                Intent intent = new Intent(splashscreen.this, blockPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFail(String msg) {
                firebase.getmRef().child("User").child(Email).child("deviceToken").setValue(FirebaseInstanceId.getInstance().getToken());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
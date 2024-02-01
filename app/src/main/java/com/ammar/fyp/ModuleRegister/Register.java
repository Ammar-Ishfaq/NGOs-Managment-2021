package com.ammar.fyp.ModuleRegister;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ammar.fyp.ModuleRegister.registrFrag.Fragment1;
import com.ammar.fyp.ModuleRegister.registrFrag.Fragment2;
import com.ammar.fyp.ModuleRegister.registrFrag.Fragment3;
import com.ammar.fyp.ModuleRegister.registrFrag.Fragment4;
import com.ammar.fyp.ModuleRegister.registrFrag.Fragment5;
import com.ammar.fyp.ModuleRegister.registrFrag.Fragment6;
import com.ammar.fyp.ModuleRegister.registrFrag.Fragment7;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.mainPermissionUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class Register extends AppCompatActivity {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int indexOfFragment = 0;
    private static final String TAG = "Register";
    private HandleValidation handleVAlidation;
    private RelativeLayout RegisterBackground;
    private mainPermissionUtils mainPermissionUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mainPermissionUtils = new mainPermissionUtils(this);
        mainPermissionUtils.CheckAllPermissionNeeded();//maintain all the permissions for that app

        handleVAlidation = new HandleValidation();
        RegisterBackground = findViewById(R.id.RegisterBackground);

        loadAllFragment();//for storing all the fragments in an arraylist
        loadFragment();//function that load the Fragment
        handleBtns();
        blur(findViewById(R.id.RegisterBlur));

    }


    /**
     * Will blur the Background or the View
     *
     * @param blurView
     */
    private void blur(BlurView blurView) {
        float radius = 9f;
        View decorView = getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
//        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        RelativeLayout rootView = findViewById(R.id.RegisterBackground);
        Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(rootView)
//                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true);
    }

    /**
     * Simply Load the fragment on the index position
     *
     * @return
     */
    public boolean loadFragment() {
        Fragment frag = fragments.get(indexOfFragment);
        if (frag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
            transaction.commit(); // commit the changes
            return true;
        }
        return false;
    }

    /**
     * <h1>Note:</h1>
     * <h2>Carefully handle the Index as they validate on the base of index down below the class </h2>
     * * Will load all the faragments in an array list for the better controll
     */
    private void loadAllFragment() {
        fragments.add(new Fragment1());//0is for the fragment1
        fragments.add(new Fragment2());//1 for fragment 2
        fragments.add(new Fragment3());//this fragment require the number that is obtained from the previous fragment
        fragments.add(new Fragment4());//this fragment require the number that is obtained from the previous fragment
        fragments.add(new Fragment5());//this fragment require the number that is obtained from the previous fragment
        fragments.add(new Fragment6());//this fragment require the number that is obtained from the previous fragment
        fragments.add(new Fragment7());//this fragment require the number that is obtained from the previous fragment

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    /**
     * @param view
     */
    public void Back(View view) {
        if (fragments.size() == 0) {
            return;
        }
        indexOfFragment--;

//        if (indexOfFragment == 2)
//            indexOfFragment--;//just to ignore the phone verification while testing
        loadFragment();
        handleBtns();
        handleVAlidation.MaintainBackground();
    }

    /**
     * @param view
     */
    public void Next(View view) {
        if (fragments.size() == 0) {
            return;
        }

        handleVAlidation.checkValidationsAndDoSome(new call() {
            @Override
            public void onsuccess(String msg) {
                Log.d(TAG, msg);

                indexOfFragment++;
//                if (indexOfFragment == 2)
//                    indexOfFragment++;//just to ignore the phone verification while testing
                loadFragment();
                handleBtns();
                handleVAlidation.MaintainBackground();

            }

            @Override
            public void onFail(String msg) {
                Log.d(TAG, msg);
            }
        });

    }

    public void Signin(View view) {
        finish();
    }

    /**
     * simply handle the btns visibility
     */
    void handleBtns() {
        Button back = findViewById(R.id.back);
        Button next = findViewById(R.id.RegisterNext);
        back.setVisibility(indexOfFragment < 1 ? View.GONE : View.VISIBLE);
        next.setVisibility(indexOfFragment >= fragments.size() - 1 ? View.GONE : View.VISIBLE);

        back.setEnabled(false);
        next.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                back.setEnabled(true);
                next.setEnabled(true);

            }
        }, 500);

        HandlePermission();

    }

    private void HandlePermission() {


        Dexter
                .withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                });
    }


    private class HandleValidation {
        protected void checkValidationsAndDoSome(call call) {
            switch (indexOfFragment) {
                case 0://fragment 0
                    Fragment1 fragment1 = new Fragment1();
                    if (fragment1.checkValidation(Register.this)) call.onsuccess("pass");
                    else call.onFail("fail");
                    break;
                case 1://fragment 1
                    Fragment2 fragment2 = new Fragment2();
                    if (fragment2.checkValidation(Register.this)) {
//                        Log.d("tagname", "phone: " + phone);
//                        Intent intent = new Intent(Register.this, VerifyNumber.class);
//                        intent.putExtra("Number", phone);
//                        startActivity(intent);
                        call.onsuccess("pass");

                    } else call.onFail("fail");
                    break;
                case 2:
                    Fragment3 fragment3 = new Fragment3();
                    if (fragment3.CheckValidation(Register.this)) call.onsuccess("pass");
                    else call.onFail("fail");
                    //case 2 which holds the validation of the
                    break;
                case 3:
                    Fragment4 fragment4 = new Fragment4();
                    if (fragment4.checkValidation(Register.this)) call.onsuccess("pass");
                    else call.onFail("fail");
                    //case 2 which holds the validation of the
                    break;
                case 4:
                    Fragment5 fragment5 = new Fragment5();
                    if (fragment5.CheckValidation(Register.this)) call.onsuccess("pass");
                    else call.onFail("fail");
                    //case 2 which holds the validation of the
                    break;

                case 5:
                    Fragment6 fragment6 = new Fragment6();
                    if (fragment6.CheckValidation()) call.onsuccess("pass");
                    else call.onFail("fail");
                    //case 2 which holds the validation of the
                    break;
                default:// incase
                    call.onsuccess("testing allow");
                    break;
            }
        }

        protected void MaintainBackground() {
            switch (indexOfFragment) {
//                case 1:
//                    RegisterBackground.setVisibility(View.GONE);
//
//                    break;
//                case 4:
//                    RegisterBackground.setVisibility(View.GONE);
//
//                    break;
//                default:
//                    RegisterBackground.setVisibility(View.GONE);
//                    break;
            }
        }
    }

    private interface call {
        void onsuccess(String msg);

        void onFail(String msg);
    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//
//        if (requestCode == GPS_requestCode) {
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
//                // start to find location...
//
//            } else { // if permission is not granted
//                Toast.makeText(this, "Not Granted", Toast.LENGTH_SHORT).show();
//                // decide what you want to do if you don't get permissions
//            }
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(this, "Result activity", Toast.LENGTH_SHORT).show();
        mainPermissionUtils.CheckAllPermissionNeeded();//check that all the permissions are granted r  not
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case GPS_requestCode:
//                    CheckGpsStatus(this);
//                    break;
//            }
//        }
    }
}
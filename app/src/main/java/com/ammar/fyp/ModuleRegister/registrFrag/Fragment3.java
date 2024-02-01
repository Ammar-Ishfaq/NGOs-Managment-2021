package com.ammar.fyp.ModuleRegister.registrFrag;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Holds the fragment pick image
 */
public class Fragment3 extends Fragment {
    private View view;
    Button b, resend;
    EditText edit_text;
    ProgressBar progressBar;
    String verificationCodeBySystem;
    TextView code_to_phone, timer;
    String number;
    private static Fragment2 fragment2;


    private static Model model;
    private RelativeLayout verify_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.verfify_number, container, false);
        set();
        send_code();
        btn();
        CheckData();
        return view;
    }

    private void CheckData() {
        if (CheckValidation(getActivity())) verify_layout.setVisibility(View.GONE);
        else verify_layout.setVisibility(View.VISIBLE);

    }

    public Fragment3() {

    }

    public static Model getModel() {
        return model;
    }

    private void send_code() {
        if (fragment2 == null) fragment2 = new Fragment2();
        String phone = "+" + fragment2.model.getCcppersonal() + fragment2.model.getPersonal();
        number = phone;

        code_to_phone.setText("Code is Sent To " + "\n+" + fragment2.model.getCcppersonal() + " " + fragment2.model.getPersonal());

        sendVerificationCodeToUser(number);
        Log.d("phone_number  : ", "Phone number" + number);
    }

    private void set() {
        if (model == null) model = new Model();
        model.setVerify(false);//so that if the user come again will face it for re-verification as it may be change the number

        verify_layout = view.findViewById(R.id.verify_rel_layout);
        edit_text = view.findViewById(R.id.otp_code);
        b = view.findViewById(R.id.verify_otp);
        progressBar = view.findViewById(R.id.progressBar_otp);
        code_to_phone = view.findViewById(R.id.code_to_phone_otp);
        timer = view.findViewById(R.id.timer_otp);
        resend = view.findViewById(R.id.reset_otp);
//        set_number();
        set_timer();
    }


    void set_timer() {
        resend.setEnabled(false);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
//                timer.setText("done!");
                resend.setEnabled(true);
            }

        }.start();
    }

    private void btn() {

        b.setOnClickListener(v -> {
            //code in on lic button
            String code = edit_text.getText().toString();

            if (code.isEmpty() || code.length() < 6) {
                edit_text.setError("Wrong OTP...");
                edit_text.requestFocus();
                return;
            }
//                progressBar.setVisibility(View.VISIBLE);
            verifyCode(code);

        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_timer();
                send_code();
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,   // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


    }

    //for verified or not
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
//                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser) {

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
            signInTheUserByCredentials(credential);
        } catch (Exception e) {

        }
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            model.setVerify(true);
                            Toast.makeText(getActivity(), "Your Number Verified Successfully!", Toast.LENGTH_SHORT).show();

//                            //Perform Your required action here to either let the user sign In or do something required
//                            Intent intent = new Intent(getActivity(), MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);

                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public boolean CheckValidation(Activity activity) {
        if (model.isVerify())
            return true;
        else
            Toast.makeText(activity, "Please Verify Your Number", Toast.LENGTH_SHORT).show();
        return false;
    }

    private static class Model {
        private static boolean isVerify;

        public boolean isVerify() {
            return isVerify;
        }

        public void setVerify(boolean verify) {
            isVerify = verify;
        }
    }

}



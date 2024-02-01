package com.ammar.fyp.views.login_and_registration;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.ammar.fyp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPassword extends AppCompatActivity {
    TextInputLayout recoverEmailEt;
    Button forget_password_btn;
    ProgressDialog loadingbar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        recoverEmailEt = findViewById(R.id.mEmail);
        forget_password_btn = findViewById(R.id.forget_password_btnId);
        loadingbar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        forget_password_btn.setOnClickListener(v -> {
            try {
                String emailRecover = recoverEmailEt.getEditText().getText().toString();
                beginRecovery(emailRecover);
            } catch (Exception e) {
                Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void beginRecovery(String emailRecover) {

        loadingbar.setTitle("Sending new password to this email");
        loadingbar.show();
        loadingbar.setCanceledOnTouchOutside(false);

        mAuth.sendPasswordResetEmail(emailRecover)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loadingbar.dismiss();
                        finish();
                        Toast.makeText(ResetPassword.this, "Check Email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResetPassword.this, "Failed...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
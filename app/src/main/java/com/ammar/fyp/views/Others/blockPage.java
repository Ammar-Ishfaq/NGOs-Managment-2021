package com.ammar.fyp.views.Others;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ammar.fyp.ModelClasses.UserAfterLogin;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.Firebase;
import com.ammar.fyp.views.login_and_registration.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

public class blockPage extends AppCompatActivity {
    private Firebase firebase;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_page);
        firebase = new Firebase();
        email = findViewById(R.id.emailBlock);
        email.setText(UserAfterLogin.getInstance().getModelUser().getEmail());

    }

    public void logout(View view) {
        firebase.getmAuth().signOut();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
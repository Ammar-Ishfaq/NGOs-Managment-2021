package com.ammar.fyp.Tools;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ammar.fyp.R;

public class mProgressDialog {
    private Context context;
    private AlertDialog dialog;

    public mProgressDialog(Context context) {
        this.context = context;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.progress_dialog, null);
        builder.setView(v);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.2f);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//as the keyboard Open move the dialog up to th ekeyboard
        dialog.show();

    }

    public void dismiss() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
        }
    }
}

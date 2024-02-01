package com.ammar.fyp.ModuleSendingDetailForm;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ammar.fyp.Interfaces.imgUploadResponse;
import com.ammar.fyp.ModelClasses.SubChatView;
import com.ammar.fyp.ModelClasses.UserAfterLogin;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.FirebaseCrud;
import com.ammar.fyp.Tools.MyMapUtils;
import com.ammar.fyp.Tools.mProgressDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

/**
 * For the Sending of Special form( sending payment details about a client ) to the donor
 */
public class send_a_ChatForm extends AppCompatActivity {

    private static final int MAP_REQUEST_CODE = 10;
    private final int CODETakePic = 0;
    private final int CODEPickPic = 1;
    private Bitmap bitmap = null;
    mProgressDialog mProgressDialog = new mProgressDialog(this);

    public SubChatView getSubChatView() {
        return subChatView;
    }

    private SubChatView subChatView;
    //My Details
    CircularImageView imageView;
    ImageView map;
    TextView
            contact,
            amountBody,
            message_body;
    Button submit;
    private String
            Email,
            chatCombineEmail,
            imageUrl;
    private int isEmail1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_a__chat_form);
        try {
            subChatView = new SubChatView();
            if (subChatView.getLocation() != null) {
                Glide.with(this).load(MyMapUtils.getInstance().getImgfromLongLat(subChatView.getLocation())).into(map);
            }
            chatCombineEmail = getIntent().getStringExtra("chatCombineEmail");
            isEmail1 = getIntent().getIntExtra("isEmail1", 404);
            Email = getIntent().getStringExtra("Email");

            bindView();
            bindListner();
        } catch (Exception e) {
            finish();
        }
    }


    private void bindListner() {
        imageView.setOnClickListener(v -> {
            selectImage();
        });
        map.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectLocationPickerActivity.class);
            startActivityForResult(intent, MAP_REQUEST_CODE);// Activity is started with particular requestCode

        });
        contact.setOnClickListener(v -> {
            setContact(this);
        });
        amountBody.setOnClickListener(v -> {
            setAmount(this);
        });
        message_body.setOnClickListener(v -> {
            setMessage(this);
        });
        submit.setOnClickListener(v -> {
            if (validInput()) {
                mProgressDialog.show();
                FirebaseCrud.getInstance().uploadImage(chatCombineEmail.replace(".", ",") + new Date().getTime(), bitmap, new imgUploadResponse() {
                    @Override
                    public void onSuccess(String imgUrl) {
                        subChatView.setisEmail1("" + isEmail1);
                        subChatView.setTime("" + new Date().getTime());
                        subChatView.setImage(imgUrl);
                        FirebaseDatabase.getInstance().getReference()
                                .child("Chat")
                                .child("subView")
                                .child(chatCombineEmail)
                                .push()
                                .setValue(subChatView)
                                .addOnSuccessListener(aVoid -> {
                                    FirebaseCrud.getInstance().sendNotification(
                                            send_a_ChatForm.this,
                                            Email,
                                            UserAfterLogin.getInstance().getModelUser().getEmail(),
                                            subChatView.getText()
                                    );
                                    showErr("Successfully Done");
                                    finish();
                                })
                                .addOnFailureListener(e -> showErr("Failed"));
                    }

                    @Override
                    public void onFail(String msg) {
                        showErr(msg);
                    }
                });

            }
        });
    }

    boolean showErr(String msg) {
        mProgressDialog.dismiss();
        Snackbar.make(findViewById(android.R.id.content), msg, 1000).show();
        return false;
    }

    private boolean validInput() {
        if (bitmap == null)
            return showErr("Please Choose an Image");
        if (subChatView.getLocation() == null)
            return showErr("Please Select Location");
        if (subChatView.getContact() == null || subChatView.getContact().length() < 7)
            return showErr("Contact Required");
        if (subChatView.getAmount() == null || subChatView.getAmount().length() < 2)
            return showErr("Amount Required");
        if (subChatView.getText() == null || subChatView.getText().length() < 4)
            return showErr("Message Required");
        return true;
    }

    private void bindView() {
        imageView = findViewById(R.id.imgBody);
        map = findViewById(R.id.mapBody);
        contact = findViewById(R.id.contactBody);
        amountBody = findViewById(R.id.amountBody);
        message_body = findViewById(R.id.message_body);
        submit = findViewById(R.id.submit);
    }

    /**
     * Will get the offered Quantity from the seller
     *
     * @param context
     */
    private void setAmount(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Amount:");
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            subChatView.setAmount(input.getText().toString().trim() + " PKR");
            amountBody.setText(subChatView.getAmount());
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    /**
     * Will get the offered Quantity from the seller
     *
     * @param context
     */
    private void setContact(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Contact:");
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        int maxLength = 10;
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            subChatView.setContact("+92 " + input.getText().toString().trim());
            contact.setText(subChatView.getContact());
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    private void setMessage(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Message:");
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            subChatView.setText(input.getText().toString().trim());
            message_body.setText(subChatView.getText());
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    public void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, CODETakePic);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

            } else if (options[item].equals("Choose from Gallery")) {
                Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, CODEPickPic);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();


            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * return the image that we selected from the gallery
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case CODETakePic:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
//                        model.setBitmap(selectedImage);
                        bitmap = selectedImage;
                    }
                    break;
                case CODEPickPic:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = this.getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                model.setBitmap(BitmapFactory.decodeFile(picturePath));
                                bitmap = BitmapFactory.decodeFile(picturePath);
                                cursor.close();
                            }
                        }

                    }
                    break;
                case MAP_REQUEST_CODE:
                    String message = data.getStringExtra("MESSAGE");
                    // TODO: Do something with your extra data
                    getSubChatView().setLocation(message);
                    Glide.with(this).load(MyMapUtils.getInstance().getImgfromLongLat(message)).into(map);


            }
        }
    }

}
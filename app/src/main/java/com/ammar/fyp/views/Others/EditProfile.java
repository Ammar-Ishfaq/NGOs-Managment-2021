package com.ammar.fyp.views.Others;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ammar.fyp.Interfaces.imgUploadResponse;
import com.ammar.fyp.Interfaces.response;
import com.ammar.fyp.ModelClasses.ModelUser;
import com.ammar.fyp.ModelClasses.UserAfterLogin;
import com.ammar.fyp.ModuleSendingDetailForm.SelectLocationPickerActivity;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.FirebaseCrud;
import com.ammar.fyp.Tools.MyMapUtils;
import com.ammar.fyp.Tools.mProgressDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfile extends AppCompatActivity {
    private CircularImageView
            ImageView;
    private android.widget.ImageView
            mapprofile;
    private TextInputLayout
            mFirstName,
            mLastName,
            mPhoneNumber,
            mPhoneJazzCash,
            mPhoneEasyPaissaCash,
            mLoginEmail;
    private TextView
            mTextViewDOB;
    private RadioGroup
            RegisterMainRadio,
            AccountType;

    private final int CODETakePic = 0;
    private final int CODEPickPic = 1;
    private final int MAP_REQUEST_CODE = 10;

    private ModelUser newUpdatedModel;
    private Bitmap newImage = null;
    private mProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        newUpdatedModel = new ModelUser();
        progressDialog = new mProgressDialog(this);
        newUpdatedModel = UserAfterLogin.getInstance().getModelUser();

        setBindView();
        setListner();
        populateOldData();
    }


    private void setListner() {

        RegisterMainRadio.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb1:
                    newUpdatedModel.setGender("Male");
                    break;
                case R.id.rb2:
                    newUpdatedModel.setGender("Female");
                    break;
                case R.id.rb3:
                    newUpdatedModel.setGender("Other");
                    break;
            }
        });
    }


    private void setBindView() {
        ImageView = findViewById(R.id.ImageView);
        mFirstName = findViewById(R.id.mFirstName);
        mLastName = findViewById(R.id.mLastName);
        mLoginEmail = findViewById(R.id.mLoginEmail);
        mTextViewDOB = findViewById(R.id.mTextViewDOB);
        mapprofile = findViewById(R.id.mapprofile);
        RegisterMainRadio = findViewById(R.id.RegisterMainRadio);
        mPhoneNumber = findViewById(R.id.mPhoneNumber);
        mPhoneJazzCash = findViewById(R.id.mPhoneJazzCash);
        mPhoneEasyPaissaCash = findViewById(R.id.mPhoneEasyPaissaCash);
        AccountType = findViewById(R.id.AccountType);
    }

    private void populateOldData() {
        Glide.with(this).load(UserAfterLogin.getInstance().getModelUser().getImage()).into(ImageView);
        mFirstName.getEditText().setText(UserAfterLogin.getInstance().getModelUser().getFirstName());
        mLastName.getEditText().setText(UserAfterLogin.getInstance().getModelUser().getLastName());
        mLoginEmail.getEditText().setText(UserAfterLogin.getInstance().getModelUser().getEmail());
        mTextViewDOB.setText(UserAfterLogin.getInstance().getModelUser().getDateOfBirth());
        Glide.with(this).load(MyMapUtils.getInstance().getImgfromLongLat(UserAfterLogin.getInstance().getModelUser().getLonglat())).into(mapprofile);
//      Gender Selection
        String gender = UserAfterLogin.getInstance().getModelUser().getGender();
        int temp = 0;
        if (gender.equals("Male")) temp = 0;
        else if (gender.equals("Female")) temp = 1;
        else temp = 2;
        RegisterMainRadio.check(RegisterMainRadio.getChildAt(temp).getId());
//      Account Selection
        String accounttype = UserAfterLogin.getInstance().getModelUser().getGender();
        temp = 0;
        if (accounttype.equals("NGO")) temp = 0;
        else temp = 1;
        AccountType.check(AccountType.getChildAt(temp).getId());

        mPhoneNumber.getEditText().setText(UserAfterLogin.getInstance().getModelUser().getNumberPersonal());
        mPhoneJazzCash.getEditText().setText(UserAfterLogin.getInstance().getModelUser().getNumberJazzCash());
        mPhoneEasyPaissaCash.getEditText().setText(UserAfterLogin.getInstance().getModelUser().getNumberEasyPaissa());


    }

    public void SelectLocation(View view) {
        Intent intent = new Intent(this, SelectLocationPickerActivity.class);
        startActivityForResult(intent, MAP_REQUEST_CODE);// Activity is started with particular requestCode

    }

    public void OpenDatePicker(View view) {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(R.string.mDateOfBirth);
        MaterialDatePicker<Long> picker = builder.build();
        picker.show(getSupportFragmentManager(), picker.toString());
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // user has selected a date
                // format the date and set the text of the input box to be the selected date
                // right now this format is hard-coded, this will change
                ;
                // Get the offset from our timezone and UTC.
                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;

                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Date date = new Date(selection + offsetFromUTC);

                mTextViewDOB.setText(simpleFormat.format(date));
                newUpdatedModel.setDateOfBirth(mTextViewDOB.getText().toString());
            }
        });
    }

    public void updateProfile(View view) {
        progressDialog.show();
        if (newImage != null)
            FirebaseCrud.getInstance().uploadImage(newUpdatedModel.getEmail(), newImage, new imgUploadResponse() {
                @Override
                public void onSuccess(String urlImage) {
                    newUpdatedModel.setImage(urlImage);
                    FirebaseCrud.getInstance().replaceOldImageWithNew(UserAfterLogin.getInstance().getModelUser().getEmail(), urlImage);
                    updateProfile();
                }

                @Override
                public void onFail(String msg) {
                    progressDialog.dismiss();
                }
            });
        else
            updateProfile();

    }

    /**
     * Final function that will update the user profile
     */
    private void updateProfile() {

        newUpdatedModel.setFirstName(mFirstName.getEditText().getText().toString().trim());
        newUpdatedModel.setLastName(mLastName.getEditText().getText().toString().trim());
        newUpdatedModel.setNumberJazzCash(mPhoneJazzCash.getEditText().getText().toString().trim());
        newUpdatedModel.setNumberEasyPaissa(mPhoneEasyPaissaCash.getEditText().getText().toString().trim());
//        location will be set as it will changed
//       Gender  will be set as it will changed
//       Image will be set as it will changed and will update in above portion
        FirebaseCrud.getInstance().updateProfile(newUpdatedModel, new response() {
            @Override
            public void onSuccess(String msg) {
                progressDialog.dismiss();
            }

            @Override
            public void onFail(String msg) {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * Dialog Box asking for the image selection
     */
    public void selectImage(View view) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CODETakePic);

            } else if (options[item].equals("Choose from Gallery")) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, CODEPickPic);

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
                        ImageView.setImageBitmap(selectedImage);
                        newImage = selectedImage;
                    }
                    break;
                case CODEPickPic:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                ImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                newImage = BitmapFactory.decodeFile(picturePath);
                                cursor.close();
                            }
                        }

                    }
                    break;
                case MAP_REQUEST_CODE:
                    String message = data.getStringExtra("MESSAGE");
                    // TODO: Do something with your extra data
                    newUpdatedModel.setLonglat(message);//location is set to the new model
                    Glide.with(this).load(MyMapUtils.getInstance().getImgfromLongLat(message)).into(mapprofile);
                    break;
            }
        }
    }

    public void cancel(View view) {
        finish();
    }
}
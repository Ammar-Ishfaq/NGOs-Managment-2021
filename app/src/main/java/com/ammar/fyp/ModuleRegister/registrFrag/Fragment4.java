package com.ammar.fyp.ModuleRegister.registrFrag;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.fyp.Interfaces.inter;
import com.ammar.fyp.ModuleRegister.Register;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.ImageUtils;
import com.ammar.fyp.Tools.PermissionManagerUtil;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Fragment4 extends Fragment {
    private View view;


    public static Model model;
    private final int CODETakePic = 0;
    private final int CODEPickPic = 1;

    private PermissionManagerUtil permissionManagerUtil;
    private ImageUtils imageUtils;

    private CircularImageView ImageView;
    private Button rRegisterSelectImage, chooseDOB;
    private TextView mTextViewDOB;
    private RadioGroup RegisterMainRadio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_select_image, container, false);
        bindView();
        setListner();
        populateData();
        return view;
    }

    private void populateData() {
        ImageView.setImageBitmap(model.getBitmap());
        mTextViewDOB.setText(model.getDOB());
    }


    private boolean setError(TextInputLayout textInputLayout, @Nullable String msg) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(msg);
        return false;
    }

    public static Model getModel() {
        return model;
    }

    private void setListner() {
        rRegisterSelectImage.setOnClickListener(v -> {
            permissionManagerUtil = new PermissionManagerUtil(getActivity());
            permissionManagerUtil.CheckStorage(new inter() {
                @Override
                public void allow(String allow) {
                    permissionManagerUtil.CheckCamera(new inter() {
                        @Override
                        public void allow(String allow) {
                            selectImage();

                        }

                        @Override
                        public void disallow(String disAllow) {

                        }
                    });
                }

                @Override
                public void disallow(String disAllow) {

                }
            });

//            if (!permissionManagerUtil.checkPermission()) {
//                permissionManagerUtil.requestPermission();
//            } else {
//
//                selectImage();
//            }
        });
        chooseDOB.setOnClickListener(v -> {
            OpenDatePicker();
        });
        RegisterMainRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        model.setGender("Male");
                        break;
                    case R.id.rb2:
                        model.setGender("Female");
                        break;
                    case R.id.rb3:
                        model.setGender("Other");
                        break;
                }
            }
        });
    }

    /**
     * Simply bind the views
     */
    private void bindView() {
        if (model == null) model = new Model();

        imageUtils = new ImageUtils();
        ImageView = view.findViewById(R.id.ImageView);
        rRegisterSelectImage = view.findViewById(R.id.rRegisterSelectImage);
        chooseDOB = view.findViewById(R.id.chooseDOB);
        mTextViewDOB = view.findViewById(R.id.mTextViewDOB);
        RegisterMainRadio = view.findViewById(R.id.RegisterMainRadio);

    }

    /**
     * Dialog Box asking for the image selection
     */
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        model.setBitmap(selectedImage);
                    }
                    break;
                case CODEPickPic:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                ImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                model.setBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    private void OpenDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(R.string.mDateOfBirth);
        MaterialDatePicker<Long> picker = builder.build();
        picker.show(getFragmentManager(), picker.toString());
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
                model.setDOB(mTextViewDOB.getText().toString());
            }
        });
    }

    /**
     * Simply an validator for the current fragment
     *
     * @param register
     * @return
     */
    public boolean checkValidation(Register register) {
        if (model.getBitmap() == null) {
            Toast.makeText(register, "Please Select the Image", Toast.LENGTH_SHORT).show();
            return false;
        } else if (model.getDOB().length() < 8) {
            Toast.makeText(register, "Please Select Date of Birth", Toast.LENGTH_SHORT).show();
            return false;
        } else if (model.getGender().length() < 4) {
            Toast.makeText(register, "Please Choose Gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Model Class that holds the current Fragment Data
     */
    public static class Model {
        private static Bitmap bitmap;
        private static String DOB = "";
        private static String Gender = "Male";


        public String getDOB() {
            return DOB;
        }

        public void setDOB(String DOB) {
            this.DOB = DOB;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }
}

package com.ammar.fyp.ModuleRegister.registrFrag;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ammar.fyp.Interfaces.imgUploadResponse;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.Firebase;
import com.ammar.fyp.Tools.mProgressDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import androidx.fragment.app.Fragment;

public class Fragment7 extends Fragment {
    private static final String TAG = "fragment6";
    private View view;
    private Firebase firebase;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;//NUMBER VERIFY FRAGMETN
    private Fragment4 fragment4;
    private Fragment5 fragment5;
    private Fragment6 fragment6;
    private mProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_7, container, false);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
        fragment5 = new Fragment5();
        fragment6 = new Fragment6();

        firebase = new Firebase();
        progressDialog = new mProgressDialog(view.getContext());
        progressDialog.show();
        CreateNewAccount(
                fragment1.getModelObject().getEmail(),
                fragment1.getModelObject().getPassword(),
                new imgUploadResponse() {
                    @Override
                    public void onSuccess(String urlImage) {
                        HashMap<String, String> user = new HashMap<>();
                        user.put("firstName", fragment1.getModelObject().getFirstName());
                        user.put("lastName", fragment1.getModelObject().getLastName());
                        user.put("email", fragment1.getModelObject().getEmail());
                        user.put("password", fragment1.getModelObject().getConfirmPassword());

                        user.put("numberPersonal", "+" + fragment2.getModel().getCcppersonal() + fragment2.getModel().getPersonal());
                        user.put("numberEasyPaissa", "+" + fragment2.getModel().getCcpeasypaisa() + fragment2.getModel().getEasypaisa());
                        user.put("numberJazzCash", "+" + fragment2.getModel().getCcpjazzcash() + fragment2.getModel().getJazzcash());

//        user.put("img", "fragment4.getModel().getBitmap()");
                        user.put("dateOfBirth", fragment4.getModel().getDOB());
                        user.put("gender", fragment4.getModel().getGender());

                        user.put("accountType", fragment5.getModel().getAccountType());

                        user.put("longlat", fragment6.getModel().getLongLat());

                        user.put("image", urlImage);
                        user.put("isAllow", "true");
                        String tkn = FirebaseInstanceId.getInstance().getToken();
                        user.put("deviceToken", tkn);

                        String email = fragment1.getModelObject().getEmail();
                        //email = email.substring(0, email.indexOf("@"));
                        email = email.replace(".", ",");


                        firebase.getmRef().child("User").child(email).setValue(user);
                        Log.d(TAG, "Successfully Created");
                        Toast.makeText(getActivity(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }

                    /**
                     *
                     * @param msg Error Massage will be shown here
                     */
                    @Override
                    public void onFail(String msg) {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }
        );
        return view;
    }

    private void createUser(imgUploadResponse obj) {
        UploadImage(fragment4.getModel().getBitmap(), obj);


    }


    /**
     * Will get the byte array of the image
     *
     * @param bitmap
     * @return
     */
    private byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

    private void UploadImage(Bitmap bitmap, imgUploadResponse obj) {
//        disable_upload();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
        String email = fragment1.getModelObject().getEmail();
        //email = email.substring(0, email.indexOf("@"));
        email = email.replace
                (".", ",");

        final StorageReference imageRef = storageReferenceProfilePic.child(email + ".jpg");

        imageRef.putBytes(getByteArray(bitmap))
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "onSuccess: uri= " + uri.toString());
                    String img_url = uri.toString();
                    obj.onSuccess(img_url);

                }))
                .addOnFailureListener(exception ->
                        obj.onFail("Failed To Create User" + "\nErr: " + exception))
                .addOnProgressListener(taskSnapshot -> {
                    //calculating progress percentage
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        //displaying percentage in progress dialog
//                        mProgressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                });
    }

    private void CreateNewAccount(String email, String password, imgUploadResponse obj) {
        Log.d(TAG, "Email:" + email + " password: " + password);
        firebase.getmAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        createUser(obj);
//                            getActivity().getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE).edit().putString("Log", "log").apply();
                    } else {
                        obj.onFail("Failed To Create User");
                        Log.d(TAG, "Failed Created");

                    }

                });
    }


}

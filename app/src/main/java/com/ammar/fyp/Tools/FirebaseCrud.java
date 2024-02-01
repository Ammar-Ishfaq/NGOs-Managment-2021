package com.ammar.fyp.Tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ammar.fyp.Interfaces.getDetailbyEmail;
import com.ammar.fyp.Interfaces.getNGOList;
import com.ammar.fyp.Interfaces.getSingleUserDetailByEmail;
import com.ammar.fyp.Interfaces.imgUploadResponse;
import com.ammar.fyp.Interfaces.isNGO;
import com.ammar.fyp.Interfaces.response;
import com.ammar.fyp.ModelClasses.ModelUser;
import com.ammar.fyp.ModelClasses.SubChatView;
import com.ammar.fyp.ModelClasses.UserAfterLogin;
import com.ammar.fyp.ModuleDirection.FollowRoute;
import com.ammar.fyp.Services.SendNotifications;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import timber.log.Timber;

/**
 * <h1>Note </h1>
 * <h2>email1 Represents to the DONOR is this Chat</h2>
 */
public class FirebaseCrud {
    private Firebase firebase;
    private static final String TAG = "FirebaseCrud";
    private static final FirebaseCrud ourInstance = new FirebaseCrud();

    public static FirebaseCrud getInstance() {
        return ourInstance;
    }

    public FirebaseCrud() {
        if (firebase == null) firebase = new Firebase();
    }

    public void isBlockUser(response response) {
        FirebaseDatabase.getInstance().getReference().child("User").child(UserAfterLogin.getInstance().getModelUser().getEmail().replace(".", ",")).child("isAllow").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.getValue().toString().equals("false")) {
                        response.onSuccess("yess block");


                    } else
                        response.onFail("not block");
                } else
                    response.onFail("User Not Exist");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * @param email e.g: 'ammarishfaq25@gmail,com'
     * @param obj
     */
    public void CheckAccountType(String email, isNGO obj) {
        String TAG = "CheckAccountType";
        Log.d(TAG, "Email " + email);
        firebase.getmRef().child("User").child(email).addListenerForSingleValueEvent(new ValueEventListener() {//changed to value Event Listner
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserAfterLogin.getInstance().getModelUser();
                    ModelUser modelUser = snapshot.getValue(ModelUser.class);//will populate the model class of the current logined user.
                    UserAfterLogin.getInstance().setModelUser(modelUser);
//                    String AccountType = snapshot.child("accountType").getValue().toString();
                    String AccountType = modelUser.getAccountType();
                    if (AccountType.equals("NGO")) obj.NGO("NGO Account");
                    else obj.DONOR("DONOR Account");
                } else obj.Fail("User Not Exist");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                obj.Fail("Database err: " + error);
            }
        });
    }

    public void getNGOs(getNGOList obj) {
        String TAG = "NGOList";
        List<ModelUser> AllNgo = new ArrayList<>();
        firebase.getmRef().child("User").orderByChild("accountType").equalTo("NGO").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    obj.onFail("Not Any NGO Found");
                    return;
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Timber.d("snapShot: " + dataSnapshot);
                    ModelUser modelUser = dataSnapshot.getValue(ModelUser.class);
                    AllNgo.add(modelUser);
                }
                obj.NGOList(AllNgo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                obj.onFail("DataBase Error ");
            }
        });
    }

    /**
     * * <h1>Will Start the Sample Chat with the NGO</h1>
     *
     * @param DonorEmail
     * @param NGOEmail
     */
    public void startChatWithNGO(String DonorEmail, String NGOEmail, response obj) {
//        Combining wemail for seperating node top as the uniqe
        String tDonorEmail = DonorEmail.replace(".", ",");
        String tNGOEmail = NGOEmail.replace(".", ",");
        String combinEmail = tDonorEmail + " and " + tNGOEmail;
        if (Config.isDebug) {//this is for the debiging purpose so i make it clear that not to print it after the debug mode is off
            Log.d("Cmbine_Email", "Email: " + combinEmail);
        }
        firebase.getmRef().child("Chat/subView").child(combinEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            obj.onFail("Already Messaged");
                        } else {
//Getting DONOR/email1 Details for main Window chaT
                            getAccountDetailsByEmail(DonorEmail, new getSingleUserDetailByEmail() {
                                @Override
                                public void onSuccess(ModelUser modelUser) {
                                    HashMap<String, String> emailInfo = new HashMap<>();
                                    emailInfo.put("combineEmail", combinEmail);
                                    emailInfo.put("blockBy", "none");

                                    emailInfo.put("name1", modelUser.getFirstName() + " " + modelUser.getLastName());
                                    emailInfo.put("email1", modelUser.getEmail());
                                    emailInfo.put("image1", modelUser.getImage());
//Getting NGO/email2 Details for main Window chaT
                                    getAccountDetailsByEmail(NGOEmail, new getSingleUserDetailByEmail() {
                                        @Override
                                        public void onSuccess(ModelUser modelUser1) {
                                            emailInfo.put("name2", modelUser1.getFirstName() + " " + modelUser1.getLastName());
                                            emailInfo.put("email2", modelUser1.getEmail());
                                            emailInfo.put("image2", modelUser1.getImage());
//Setting the hi msg to the new starting chat
                                            firebase.getmRef().child("Chat/mainView").child(combinEmail).setValue(emailInfo)
                                                    .addOnSuccessListener(aVoid12 -> {
                                                        HashMap<String, String> hiMsg = new HashMap<>();
                                                        hiMsg.put("text", "Hi!");
                                                        hiMsg.put("time", "" + new Date().getTime());
                                                        hiMsg.put("isEmail1", "1");//use 1 as short bcz i've email1 for the donor
                                                        hiMsg.put("reciverImage", null);
                                                        hiMsg.put("reciverLocation", null);
                                                        hiMsg.put("reciverContact", null);
                                                        hiMsg.put("reciverAmount", null);
                                                        firebase.getmRef().child("Chat/subView").child(combinEmail).push().setValue(hiMsg)
                                                                .addOnSuccessListener(aVoid1 -> {
                                                                    obj.onSuccess("Successfully Message");
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    obj.onFail("Failed,Try Again");
                                                                });
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        obj.onFail("Failed,Try Again");
                                                    });
                                        }

                                        @Override
                                        public void onfail(String msg1) {
                                            obj.onFail("Failed,Try Again");

                                            if (Config.isDebug) {
                                                Log.d("onFail", "failed msg:" + msg1);
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onfail(String msg) {
                                    obj.onFail("Failed,Try Again");
                                    if (Config.isDebug) {
                                        Log.d("onFail", "failed msg:" + msg);
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        obj.onFail("Failed,Try Again");

                    }
                });
    }

    /**
     * @param email e.g amamrishfaq25@gmail,com  note: replace . with ,
     */
    public void getDetailsByMail(String email, getDetailbyEmail obj) {
        try {
            firebase.getmRef().child("User").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (!snapshot.exists()) {
                            obj.onFail("User Not Found");
                            return;
                        }
//                Log.e("snapshotcheck",""+snapshot.toString());
                        String temp =
                                snapshot.child("firstName").getValue().toString() + "\n" +
                                        snapshot.child("numberPersonal").getValue().toString() + "\n" +
                                        snapshot.child("email").getValue().toString();
                        Log.d("tasd", "temp: " + temp);
                        obj.onSuccess(temp);
                    } catch (Exception e) {
                        obj.onFail("User Not Found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    obj.onSuccess("DataBase Error");
                }
            });
        } catch (Exception e) {
            obj.onFail("User Not Found");

        }
    }

    /**
     * @param email <p>
     *              * Simply pass  the Email e.g:
     *              * ->ammarishfaq25@gmail.com
     *              * then the code will auto translate it to the
     *              * ->ammarishfaq25@gmail,com
     *              * </p>
     * @param obj
     */
    private void getAccountDetailsByEmail(String email, getSingleUserDetailByEmail obj) {
        email = email.replace(".", ",");
        firebase.getmRef().child("User").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    obj.onfail("User Not Found");
                    return;

                }
                ModelUser modelUser = snapshot.getValue(ModelUser.class);
                obj.onSuccess(modelUser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                obj.onfail("Error: " + error);
            }
        });


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

    /**
     * @param email  or the name unique for the image to be uploadeed
     * @param bitmap
     * @param obj
     */
    public void uploadImage(String email, Bitmap bitmap, imgUploadResponse obj) {
//        disable_upload();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
        //email = email.substring(0, email.indexOf("@"));
        email = email.replace
                (".", ",");

        final StorageReference imageRef = storageReferenceProfilePic.child(email + ".jpg");

        imageRef.putBytes(getByteArray(bitmap))
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
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

    public void getLastPyamentDetail(
            Context context,
            String combineEmail,
            ImageView personImage,
            ImageView map,
            TextView contact,
            TextView amount,
            LinearLayout paymentDetialLayout
    ) {
        combineEmail = combineEmail.replace(".", ",");

        final SubChatView[] chatView = new SubChatView[1];
        final long[] i = {0};
        paymentDetialLayout.setVisibility(View.GONE);
        FirebaseDatabase.getInstance().getReference()
                .child("Chat")
                .child("subView")
                .child(combineEmail)
                .orderByChild("image").startAt("http")//querry for filtering only the accounts details
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            i[0]++;
                            if (i[0] == snapshot.getChildrenCount()) {

                                chatView[0] = dataSnapshot.getValue(SubChatView.class);
                                Glide.with(context).load(chatView[0].getImage()).into(personImage);
                                Glide.with(context).load(MyMapUtils.getInstance().getImgfromLongLat(chatView[0].getLocation())).into(map);
                                contact.setText(chatView[0].getContact());
                                amount.setText(chatView[0].getAmount());

                                map.setOnClickListener(v -> {
                                    Intent intent
                                            = new Intent(v.getContext(), FollowRoute.class);
                                    intent.putExtra("longlat", chatView[0].getLocation());
                                    v.getContext().startActivity(intent);
//                                    Snackbar.make(v, "location: " + chatView[0].getLocation(), 1000).show();
                                });
                                new ImageUtils().enablePopUpZoom(context, personImage, chatView[0].getImage());//will enable the image to be zoom
                                paymentDetialLayout.setVisibility(View.VISIBLE);
                            }
//                            Log.d(TAG, "onDataChange: " + dataSnapshot + "Child: " + snapshot.getChildrenCount() + " i: " + i[0]);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void sendNotification(Context activity, String msgToEmail, String msgbyEmail, String msg) {
        msgToEmail = msgToEmail.replace(".", ",");
        FirebaseDatabase.getInstance().getReference().child("User").child(msgToEmail).child("deviceToken").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SendNotifications sendNotifications = new SendNotifications();
                sendNotifications.SendNotifications(snapshot.getValue().toString(), msgbyEmail, msg, activity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateProfile(ModelUser user, response response) {
        String nodeEmail = user.getEmail().replace(".", ",");
        FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(nodeEmail)
                .setValue(user)
                .addOnFailureListener(e -> response.onFail("Failed to Update Profile"))
                .addOnSuccessListener(aVoid -> response.onSuccess("Profile Update Successfully"));
    }

    public void replaceOldImageWithNew(String email, String newImgUrl) {
        FirebaseDatabase.getInstance().getReference()
                .child("Chat")
                .child("mainView")
                .orderByChild("email1")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            Log.d(TAG, "onDataChange: dataSnapshot: " + dataSnapshot);
                            dataSnapshot.child("image1").getRef().setValue(newImgUrl);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference()
                .child("Chat")
                .child("mainView")
                .orderByChild("email2")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            Log.d(TAG, "onDataChange: dataSnapshot: " + dataSnapshot);
                            dataSnapshot.child("image2").getRef().setValue(newImgUrl);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getPhoneNumber(String email, response response) {
        FirebaseDatabase.getInstance().getReference().child("User").child(email.replace(".", ",")).child("numberPersonal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    response.onSuccess(snapshot.getValue().toString());
                else
                    response.onFail("Fail to Call");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                response.onFail("Error could not Continue \n" + error);

            }
        });
    }

}

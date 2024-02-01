package com.ammar.fyp.ModuleChat.subViewChat;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ammar.fyp.Interfaces.response;
import com.ammar.fyp.ModelClasses.SubChatView;
import com.ammar.fyp.ModelClasses.UserAfterLogin;
import com.ammar.fyp.ModuleSendingDetailForm.send_a_ChatForm;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.Config;
import com.ammar.fyp.Tools.FirebaseCrud;
import com.ammar.fyp.Tools.ImageUtils;
import com.ammar.fyp.Tools.mProgressDialog;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class chat extends AppCompatActivity {
    RecyclerView recyclerView;
    Query query1;
    private chatRecycularView adapter;
    private com.ammar.fyp.Tools.mProgressDialog mProgressDialog;
    private TextView
            infotv,
            headerEmail;
    private LinearLayout infoLayoutDfChat;
    private String
            chatCombineEmail,
            Email,
            imageUrl;
    private EditText textMessage;
    int isEmail1;
    private ImageView headerImage;
    private boolean isJustAccount = false;
    private Dialog dialogChatLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        isJustAccount = getIntent().getBooleanExtra("isJustAccount", false);
        recyclerView = findViewById(R.id.chatRecycularView);
        infotv = findViewById(R.id.DonorChatFragmentInfotv);
        infoLayoutDfChat = findViewById(R.id.infoLayoutDfChat);

        mProgressDialog = new mProgressDialog(this);
        mProgressDialog.show();

        textMessage = findViewById(R.id.editText);
        headerImage = findViewById(R.id.subChatImage);
        headerEmail = findViewById(R.id.subChatEmail);


        isEmail1 = getIntent().getIntExtra("isEmail1", 404);
        chatCombineEmail = getIntent().getStringExtra("combineEmail");
        Email = getIntent().getStringExtra("headerEmail");
        imageUrl = getIntent().getStringExtra("imageUrl");

        if (UserAfterLogin.getInstance().getModelUser().getAccountType().equals("NGO"))
            findViewById(R.id.attachedForm).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.attachedForm).setVisibility(View.GONE);


        headerEmail.setText(Email);
        Glide
                .with(this)
                .load(imageUrl)
                .into(headerImage);

        new ImageUtils().enablePopUpZoom(this, headerImage, imageUrl);//will enable the image to be zoom


    }

    @Override
    public void onStart() {
        super.onStart();
        loadDelayDialogForChat();
        if (isJustAccount) {
            query1 = FirebaseDatabase.getInstance().getReference().child("Chat").child("subView").child(chatCombineEmail).orderByChild("image").startAt("http");
            findViewById(R.id.sendLayout).setVisibility(View.GONE);
        } else {
            query1 = FirebaseDatabase.getInstance().getReference().child("Chat").child("subView").child(chatCombineEmail);
            findViewById(R.id.sendLayout).setVisibility(View.VISIBLE);
        }
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loadRecycularViewData();
                    hideNoChatMessage();
                } else {
                    mProgressDialog.dismiss();
                    dialogChatLoader.dismiss();
                    showNoChatMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                infotv.setText("Error: " + error);
                mProgressDialog.dismiss();

            }
        });


    }

    private void loadDelayDialogForChat() {
        dialogChatLoader = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogChatLoader.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogChatLoader.setContentView(R.layout.chat_delay_dialog);
        Window window = dialogChatLoader.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialogChatLoader.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
        dialogChatLoader.show();


    }

    boolean isRecyclerScrollable() {
        if (adapter.getItemCount() > 9) // recycuylarview threashhold Value
            return true;
        else
            return false;
//        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        RecyclerView.Adapter adapter = recyclerView.getAdapter();
//        if (layoutManager == null || adapter == null) return false;
//
//        return layoutManager.findLastCompletelyVisibleItemPosition() < adapter.getItemCount() - 1;
    }

    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems - 1);
    }

    private void hideNoChatMessage() {
        infotv.setVisibility(View.GONE);
        infoLayoutDfChat.setVisibility(View.GONE);

    }

    private void showNoChatMessage() {
        infotv.setVisibility(View.VISIBLE);
        infoLayoutDfChat.setVisibility(View.VISIBLE);
        findViewById(R.id.chatScrollToBottom).setVisibility(View.GONE);
        infotv.setText("Oops!\nIt's Look like there's no Payment done yet.");

    }

    /**
     * <p>Manage the Loading of data to the recycularview so that it continue to the work</p>
     */
    private void loadRecycularViewData() {
        FirebaseRecyclerOptions<SubChatView> options =
                new FirebaseRecyclerOptions.Builder<SubChatView>()
                        .setQuery(query1, SubChatView.class)
                        .build();
        if (Config.isDebug)
            Log.d("Options", " data : " + options);

        adapter = new chatRecycularView(this, options, isEmail1);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);

        adapter.startListening();

        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new chatRecycularView.OnItemClickListener() {
            @Override
            public void onImageClick(View v, int position, ImageView imageView) {
                Toast.makeText(chat.this, "Image cardView click", Toast.LENGTH_SHORT).show();
            }

            /**
             * Hide the loader as the data is fetching succesfully from the firebase

             */
            @Override
            public void showingEnable() {
//                new Handler().postDelayed(() -> recyclerView.smoothScrollToPosition(adapter.getItemCount()), 500);
                mProgressDialog.dismiss();
            }

            @Override
            public void onChildChange() {
                recyclerView.smoothScrollToPosition(adapter.getItemCount());

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    findViewById(R.id.chatScrollToBottom).setVisibility(View.GONE);
                    if (isLastVisible()) dialogChatLoader.dismiss();

                } else if (dy < 0) {
                    // Scrolling down
                    findViewById(R.id.chatScrollToBottom).setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });
        new Handler().postDelayed(() -> {
            if (!recyclerView.canScrollVertically(-1) || !recyclerView.canScrollVertically(1)) {
                dialogChatLoader.dismiss();
                findViewById(R.id.chatScrollToBottom).setVisibility(View.GONE);
            }
        }, 2000);

    }

    public void sendMessage(View view) {
        if (TextUtils.isEmpty(textMessage.getText())) {
            return;
        }
        HashMap<String, String> msg = new HashMap<>();
        msg.put("text", textMessage.getText().toString());
        msg.put("isEmail1", "" + isEmail1);
        msg.put("time", "" + new Date().getTime());
        query1.getRef().push().setValue(msg);
        FirebaseCrud.getInstance().sendNotification(
                view.getContext(),
                Email,
                UserAfterLogin.getInstance().getModelUser().getEmail(),
                textMessage.getText().toString()
        );
        textMessage.setText("");


    }

    public void back(View view) {
        finish();
    }

    public void scrollToBottom(View view) {
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
    }

    public void openSendingMoneyForm(View view) {
        Intent intent = new Intent(this, send_a_ChatForm.class);
        intent.putExtra("chatCombineEmail", chatCombineEmail);
        intent.putExtra("isEmail1", isEmail1);
        intent.putExtra("Email", Email);
        startActivity(intent);
    }

    public void callUser(View view) {
        FirebaseCrud.getInstance().getPhoneNumber(headerEmail.getText().toString(), new response() {
            @Override
            public void onSuccess(String phoneNumber) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                startActivity(intent);
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(chat.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
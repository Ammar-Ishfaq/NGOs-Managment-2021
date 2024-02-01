package com.ammar.fyp.ModuleAccounts.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.fyp.ModelClasses.MainChatView;
import com.ammar.fyp.ModelClasses.UserAfterLogin;
import com.ammar.fyp.ModuleChat.subViewChat.chat;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.Config;
import com.ammar.fyp.Tools.mProgressDialog;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class mainAccount extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private Query query1;
    private mainAdapterFirebase adapter;
    private TextView infotv;
    private LinearLayout infoLayoutDfChat;
    private mProgressDialog mProgressDialog;
    private String isEmail1 = "email1";
    private androidx.appcompat.widget.SearchView searchMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_account, container, false);

        recyclerView = view.findViewById(R.id.chatRecycularview);
        infotv = view.findViewById(R.id.DonorChatFragmentInfotv);
        infoLayoutDfChat = view.findViewById(R.id.infoLayoutDfChat);
        searchMessage = view.findViewById(R.id.searchMessage);
        mProgressDialog = new mProgressDialog(view.getContext());
        mProgressDialog.show();
        recyclerView.requestFocus();
        setListnerForQuery();
        return view;
    }

    private void setListnerForQuery() {
        searchMessage.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                String temp = query;
//                String emil = UserAfterLogin.getInstance().getModelUser().getEmail();
//                ;
//                if (UserAfterLogin.getInstance().getModelUser().getAccountType().equals("NGO")) {
//                    emil = query + " and " + emil;
//                    emil = emil.replace(".", ",");
//                    Toast.makeText(view.getContext(), "Combine email : " + emil, Toast.LENGTH_SHORT).show();
//                    query1 = FirebaseDatabase.getInstance().getReference().child("Chat").child("mainView").orderByChild("combineEmail").endAt(emil);
//
//                } else {
//                    emil = emil + " and " + query;
//                    emil = emil.replace(".", ",");
//                    Toast.makeText(view.getContext(), "Combine email : " + emil, Toast.LENGTH_SHORT).show();
//                    query1 = FirebaseDatabase.getInstance().getReference().child("Chat").child("mainView").orderByChild("combineEmail").startAt(emil);
//
//                }
//                startWithQuerey(query1);
                loadRecycularViewData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        searchMessage.findViewById(R.id.search_close_btn)
                .setOnClickListener(v -> {
                    hideKeyboard();
                    recyclerView.requestFocus();
                    loadRecycularViewData(null);
                    searchMessage.setQuery("", false);
                    searchMessage.setIconified(true);

                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (UserAfterLogin.getInstance().getModelUser().getAccountType().equals("NGO"))
            isEmail1 = "email2";

        query1 = FirebaseDatabase.getInstance().getReference().child("Chat").child("mainView").orderByChild(isEmail1).equalTo(UserAfterLogin.getInstance().getModelUser().getEmail());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String seachquers = searchMessage.getQuery().toString();
                    if (seachquers.length() < 1)
                        seachquers = null;
                    loadRecycularViewData(seachquers);
                    hideNoChatMessage();
                } else {
                    infotv.setText("It Look Like\nYou Don't have any Chat yet");
                    mProgressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                infotv.setText("Error: " + error);
                mProgressDialog.dismiss();

            }
        });


    }

    /**
     * Will hide the Layout and make the Chat Visible
     */
    private void hideNoChatMessage() {
        infotv.setVisibility(View.GONE);
        infoLayoutDfChat.setVisibility(View.GONE);

    }

    /**
     * <p>Manage the Loading of data to the recycularview so that it continue to the work</p>
     */
    private void loadRecycularViewData(String searchingContent) {
        FirebaseRecyclerOptions<MainChatView> options =
                new FirebaseRecyclerOptions.Builder<MainChatView>()
                        .setQuery(query1, MainChatView.class)
                        .build();
        if (Config.isDebug)
            Log.d("Options", " data : " + options);

        adapter = new mainAdapterFirebase(view.getContext(), options, isEmail1, searchingContent);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter.startListening();

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new mainAdapterFirebase.OnItemClickListener() {

            @Override
            public void onImageClick(View v, int position, int isEmail1, String combineEmail, String chatWithEmail, String imageUrl) {
                Toast.makeText(view.getContext(), "Image cardView click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), chat.class);
                intent.putExtra("isEmail1", isEmail1);
                intent.putExtra("combineEmail", combineEmail);
                intent.putExtra("headerEmail", chatWithEmail);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("isJustAccount", true);
                startActivity(intent);
            }

            /**
             * Hide the loader as the data is fetching succesfully from the firebase
             */
            @Override
            public void showingEnable() {
                mProgressDialog.dismiss();
            }
        });
    }

    private void hideKeyboard() {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

package com.ammar.fyp.ModuleDONOR;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.fyp.R;
import com.ammar.fyp.Tools.Firebase;
import com.ammar.fyp.views.Others.EditProfile;
import com.ammar.fyp.views.login_and_registration.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DFprofile extends Fragment {
    private View view;
    private TextView profileLogout, editProfile, profileHelpLine;
    private Firebase firebase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.d_frag_profile, container, false);
        firebase = new Firebase();
        profileLogout = view.findViewById(R.id.logouttextview);
        editProfile = view.findViewById(R.id.profiletextview);
        profileHelpLine = view.findViewById(R.id.helplinetextview);
        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfile.class);
            getActivity().startActivity(intent);
        });
        profileHelpLine.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+923234164199", null));
            startActivity(intent);
        });
        profileLogout.setOnClickListener(v -> {
            firebase.getmAuth().signOut();
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });
        return view;
    }
}

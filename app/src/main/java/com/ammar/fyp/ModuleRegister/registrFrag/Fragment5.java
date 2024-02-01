package com.ammar.fyp.ModuleRegister.registrFrag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ammar.fyp.R;
import com.ammar.fyp.ModuleRegister.Register;

import androidx.fragment.app.Fragment;

public class Fragment5 extends Fragment {
    private static final String TAG = "fragment5";
    private boolean isNGO = false;
    private View view;
    private ImageView RegisterDonor, RegisterNGO;


    private static Model model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_select_donor_or_n_g_o, container, false);

        bindView();
        setListner();
        populateData();
        return view;
    }

    private void populateData() {
        if (model.getAccountType().length() > 0) {
            if (model.getAccountType().equals("NGO")) {
                isNGO = true;
            } else {
                isNGO = false;

            }
            handle();

        }
    }

    /**
     *
     */
    private void setListner() {
        RegisterNGO.setOnClickListener(v -> {
            Log.d(TAG, "Card clicked");
            isNGO = true;
            handle();
        });
        RegisterDonor.setOnClickListener(v -> {
            isNGO = false;
            handle();
        });
    }

    private void handle() {
        RegisterNGO.setBackgroundColor(isNGO ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
        RegisterDonor.setBackgroundColor(isNGO ? getResources().getColor(R.color.white) : getResources().getColor(R.color.black));

        model.setAccountType(isNGO ? "NGO" : "DONOR");
    }

    /**
     *
     */
    private void bindView() {
        if (model == null) model = new Model();
        RegisterDonor = view.findViewById(R.id.RegisterDonor);
        RegisterNGO = view.findViewById(R.id.RegisterNGO);
    }

    /**
     * @param register
     * @return
     */
    public boolean CheckValidation(Register register) {
        return (model.getAccountType().length() > 0);
    }

    public static class Model {
        private static String AccountType = "";

        public String getAccountType() {
            return AccountType;
        }

        public void setAccountType(String accountType) {
            AccountType = accountType;
        }
    }

    public static Model getModel() {
        return model;
    }}

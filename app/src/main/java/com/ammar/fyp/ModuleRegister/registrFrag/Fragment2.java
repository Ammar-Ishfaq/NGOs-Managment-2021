package com.ammar.fyp.ModuleRegister.registrFrag;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ammar.fyp.R;
import com.ammar.fyp.ModuleRegister.Register;
import com.google.android.material.textfield.TextInputLayout;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {
    private View view;
    private static CountryCodePicker
            ccpPersonal,
            ccpjaz,
            ccpeasy;


    public static Fragment2Model model;
    private static TextInputLayout//statuic is so that the data can be fetched succesfully
            personal,
            jazz,
            easypaissa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.register2, container, false);
        bindView();
        setListner();
        populateData();
        return view;
    }

    public static Fragment2Model getModel() {
        return model;
    }

    private void populateData() {
//        ccpPersonal.setDefaultCountryUsingNameCodeAndApply(model.getCcppersonal());
        ccpPersonal.setCountryForPhoneCode(model.getCcppersonal());
        ccpeasy.setCountryForPhoneCode(92);
        ccpjaz.setCountryForPhoneCode(92);

        easypaissa.getEditText().setText(model.getEasypaisa());
        jazz.getEditText().setText(model.getJazzcash());
        personal.getEditText().setText(model.getPersonal());
    }

    public boolean checkValidation(Register register) {
        if (model.getPersonal().length() < 10) return setError(personal, "Not valid Number");
        if (model.getEasypaisa().length() < 10) return setError(easypaissa, "Not valid Number");
        if (model.getJazzcash().length() < 10) return setError(jazz, "Not valid Number");
        else return true;
    }

    private boolean setError(TextInputLayout textInputLayout, @Nullable String msg) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(msg);
        return false;
    }

    private void setListner() {
        personal.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                model.setPersonal(s.toString().trim());
            }
        });
        easypaissa.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                model.setEasypaisa(s.toString().trim());
            }
        });
        jazz.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                model.setJazzcash(s.toString().trim());
            }
        });
        ccpPersonal.setOnCountryChangeListener(selectedCountry -> {
            Log.d("tagname", "selected=" + ccpPersonal.getSelectedCountryCodeAsInt());
            model.setCcppersonal(ccpPersonal.getSelectedCountryCodeAsInt());
        });
    }

    private void bindView() {
        if (model == null) model = new Fragment2Model();

        ccpPersonal = view.findViewById(R.id.ccpPersonal);
        ccpjaz = view.findViewById(R.id.ccpjaz);
        ccpeasy = view.findViewById(R.id.ccpeasy);

        personal = view.findViewById(R.id.personal);
        jazz = view.findViewById(R.id.jazz);
        easypaissa = view.findViewById(R.id.easypaissa);
    }


    public static class Fragment2Model {
        private static String personal = "";//initailize so that not null conditiopn use
        private static int ccppersonal = 92;//initailize so that not null conditiopn use
        private static String easypaisa = "";
        private static String ccpeasypaisa = "92";
        private static String jazzcash = "";
        private static String ccpjazzcash = "92";

        public int getCcppersonal() {
            return ccppersonal;
        }

        public void setCcppersonal(int ccppersonal) {
            Fragment2Model.ccppersonal = ccppersonal;
        }

        public String getCcpeasypaisa() {
            return ccpeasypaisa;
        }

        public void setCcpeasypaisa(String ccpeasypaisa) {
            Fragment2Model.ccpeasypaisa = ccpeasypaisa;
        }

        public String getCcpjazzcash() {
            return ccpjazzcash;
        }

        public void setCcpjazzcash(String ccpjazzcash) {
            Fragment2Model.ccpjazzcash = ccpjazzcash;
        }


        public String getPersonal() {
            return personal;
        }

        public void setPersonal(String personal) {
            Fragment2Model.personal = personal;
        }

        public String getEasypaisa() {
            return easypaisa;
        }

        public void setEasypaisa(String easypaisa) {
            Fragment2Model.easypaisa = easypaisa;
        }

        public String getJazzcash() {
            return jazzcash;
        }

        public void setJazzcash(String jazzcash) {
            Fragment2Model.jazzcash = jazzcash;
        }


    }
}

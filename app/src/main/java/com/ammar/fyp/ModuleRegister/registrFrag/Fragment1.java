package com.ammar.fyp.ModuleRegister.registrFrag;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ammar.fyp.Interfaces.getDetailbyEmail;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.FirebaseCrud;
import com.ammar.fyp.Tools.validatorUtils;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment1 extends Fragment {
    private static final String TAG = "fragment1";
    private View view;
    private static TextInputLayout
            RegisterFirstName,
            RegisterLastName,
            RegisterPassword,
            RegisterConfirmPassword,
            RegisterEmail;
    private static model model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.register1, container, false);
        bindView();
        setListner();
        populateData();
        return view;
    }

    /**
     * Model class object
     *
     * @return
     */
    public model getModelObject() {
        return model;
    }

    /**
     * Populating the data to the fields from getter setter ; if user move back then it will make the fields same as they populate
     */
    private void populateData() {
        RegisterFirstName.getEditText().setText(model.getFirstName());
        RegisterLastName.getEditText().setText(model.getLastName());
        RegisterEmail.getEditText().setText(model.getEmail());
        RegisterPassword.getEditText().setText(model.getPassword());
        RegisterConfirmPassword.getEditText().setText(model.getConfirmPassword());
    }

    /**
     * Simply the Listner to the edit text
     */
    private void setListner() {
        RegisterFirstName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "String Edit Text: " + s.toString().trim());
                model.setFirstName(s.toString().trim());
            }
        });
        RegisterLastName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "String Edit Text: " + s.toString().trim());
                model.setLastName(s.toString().trim());
            }
        });
        RegisterEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "String Edit Text: " + s.toString().trim());
                model.setEmail(s.toString().trim());
                FirebaseCrud.getInstance().getDetailsByMail(s.toString().trim().replace(".", ","), new getDetailbyEmail() {
                    @Override
                    public void onSuccess(String details) {
                        model.setEmail("");
                        setError(RegisterEmail, "Email Already Used Try a new one");
                    }

                    @Override
                    public void onFail(String msg) {
                      RegisterEmail.setErrorEnabled(false);

                    }
                });
            }
        });
        RegisterPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "String Edit Text: " + s.toString().trim());
                model.setPassword(s.toString().trim());
            }
        });
        RegisterConfirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "String Edit Text: " + s.toString().trim());
                model.setConfirmPassword(s.toString().trim());
            }
        });
    }

    /**
     * Simply Asigning the view id's
     */
    private void bindView() {
        if (model == null) {
            model = new model();
        }
        RegisterFirstName = view.findViewById(R.id.RegisterFirstName);
        RegisterLastName = view.findViewById(R.id.RegisterLastName);
        RegisterEmail = view.findViewById(R.id.RegisterEmail);
        RegisterPassword = view.findViewById(R.id.RegisterPassword);
        RegisterConfirmPassword = view.findViewById(R.id.RegisterConfirmPassword);
    }

    /**
     * This will allow the main next button to hold the validation against this page
     *
     * @param register
     * @return
     */
    public boolean checkValidation(Activity register) {
        validatorUtils validatorUtils = new validatorUtils();
        Log.d(TAG, "Email:" + model.getEmail());
        if (model != null) {
            if (model.getFirstName().isEmpty())
                return setError(RegisterFirstName, "Required");
            else if (model.getLastName().isEmpty())
                return setError(RegisterLastName, "Required");
            else if (model.getEmail().isEmpty() || !validatorUtils.isValidEmailId(model.getEmail()))
                return setError(RegisterEmail, "Invalid Email Format");
            else if (model.getPassword().isEmpty())
                return setError(RegisterPassword, "Not be Empty");
            else if (model.getPassword().length() < 6)
                return setError(RegisterPassword, "Too Short Password");
            else if (model.getConfirmPassword().isEmpty())
                return setError(RegisterConfirmPassword, "Not be Empty");
            else if (model.getConfirmPassword().length() < 6)
                return setError(RegisterConfirmPassword, "Too Short Password");
            else if (!model.getPassword().equals(model.getConfirmPassword())) {
                setError(RegisterConfirmPassword, "Password not Matched");
                return setError(RegisterPassword, "Password not Matched");
            }
        }
        return true;

    }

    private boolean setError(TextInputLayout textInputLayout, @Nullable String msg) {
        textInputLayout.setErrorEnabled(true);
        if (msg == null)
            textInputLayout.setError("Invalid Input");
        else
            textInputLayout.setError(msg);
        return false;
    }

    public static class model {
        private static String firstName = "";//initailize so that not null conditiopn use
        private static String lastName = "";
        private static String email = "";
        private static String password = "";
        private static String confirmPassword = "";

        public String getFirstName() {

            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
    }


}

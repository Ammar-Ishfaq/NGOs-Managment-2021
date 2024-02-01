package com.ammar.fyp.Tools;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * All firebase Functions will be done With the following Object
 */
public class Firebase {
    private static FirebaseAuth mAuth = null;
    private static DatabaseReference mRef = null;

    /**
     * <h3>Speciallay for the firebase Auth</h3>
     * Will return the Firebase Auth
     *
     * @return
     */
    public FirebaseAuth getmAuth() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    /**
     * <h3>For the Firebase database Reference</h3>
     * Will return the root reference of the firebase database
     *
     * @return
     */
    public DatabaseReference getmRef() {
        if (mRef == null) {
            mRef = FirebaseDatabase.getInstance().getReference();
        }
        return mRef;
    }


}

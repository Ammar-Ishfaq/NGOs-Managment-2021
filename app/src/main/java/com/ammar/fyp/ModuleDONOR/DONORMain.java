package com.ammar.fyp.ModuleDONOR;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ammar.fyp.ModuleAccounts.main.mainAccount;
import com.ammar.fyp.ModuleChat.mainViewChat.mainChatFragment;
import com.ammar.fyp.R;
import com.ammar.fyp.Tools.mainPermissionUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class DONORMain extends AppCompatActivity {
    DFrag_Nearby_NGO nearby = new DFrag_Nearby_NGO();
    DFnotifications dFnotifications = new DFnotifications();
    DFprofile dFprofile = new DFprofile();
    mainChatFragment mainChatFragment = new mainChatFragment();
    private mainPermissionUtils mainPermissionUtils;
    mainAccount account = new mainAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_d_o_n_o_r_main);
        mainPermissionUtils = new mainPermissionUtils(this);
        mainPermissionUtils.CheckAllPermissionNeeded();//maintain all the permissions for that app

        loadFragment(mainChatFragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nearby:
                        loadFragment(nearby);
                        break;
                    case R.id.chat:
                        loadFragment(mainChatFragment);
                        break;
                    case R.id.profile:
                        loadFragment(dFprofile);
                        break;
                    case R.id.account:
                        loadFragment(account);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }


    /**
     * Simply Load the fragment on the index position
     *
     * @return
     */
    public boolean loadFragment(Fragment frag) {
        if (frag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.Donor_frame, frag); // replace a Fragment with Frame Layout
            transaction.commit(); // commit the changes
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainPermissionUtils.CheckAllPermissionNeeded();//check that all the permissions are granted r  not

//        }
    }
}
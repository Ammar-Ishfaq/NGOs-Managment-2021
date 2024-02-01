package com.ammar.fyp.ModuleNGO;

import android.os.Bundle;
import android.view.MenuItem;

import com.ammar.fyp.ModuleAccounts.main.mainAccount;
import com.ammar.fyp.ModuleChat.mainViewChat.mainChatFragment;
import com.ammar.fyp.ModuleDONOR.DFprofile;
import com.ammar.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class NGOMain extends AppCompatActivity {
    mainChatFragment mainChatFragment = new mainChatFragment();
    DFprofile dFprofile = new
            DFprofile();
    mainAccount account = new mainAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_g_o_main);
        loadFragment(mainChatFragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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

            transaction.replace(R.id.NGOFrame, frag); // replace a Fragment with Frame Layout
            transaction.commit(); // commit the changes
            return true;
        }
        return false;
    }
}
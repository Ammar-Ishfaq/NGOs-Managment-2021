package com.ammar.fyp.Tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.ammar.fyp.Interfaces.inter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.appcompat.app.AppCompatActivity;

public class PermissionManagerUtil extends AppCompatActivity {
    private Activity activity;
    private static final int all_permissions_ok_CODE = 100;

    /**
     * @param activity for dialog showing activity required
     */
    public PermissionManagerUtil(Activity activity) {
        this.activity = activity;
    }


    /**
     * @param inter object responseing
     */
    public void CheckStorage(inter inter) {

        Dexter.withContext(activity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        inter.allow("Allow");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        inter.disallow("disAllow");

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                        inter.disallow("disAllow");

                    }
                })
                .check();
    }

    public void CheckCamera(inter inter) {
        Dexter.withContext(activity)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        inter.allow("Allow");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        inter.disallow("disAllow");

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                        inter.disallow("disAllow");

                    }
                })
                .check();
    }

    public void CheckLocation(inter inter) {
        Dexter.withContext(activity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        inter.allow("Allow");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        inter.disallow("disAllow");

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                        inter.disallow("disAllow");

                    }
                })
                .check();
    }

    public void CheckGpsStatus() {
        LocationManager locationManager;
        boolean GpsStatus;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GpsStatus == true) {
//            textview.setText("GPS Is Enabled");
        } else {
            // TODO Auto-generated method stub
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
//            textview.setText("GPS Is Disabled");
        }
    }
}

package com.ammar.fyp.Tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import androidx.appcompat.app.AlertDialog;

public class mainPermissionUtils {

    public mainPermissionUtils(Activity activity) {
        this.activity = activity;
    }

    private Activity activity;
    private final int GPS_requestCode = 111;

    /**
     * This will mangage all the required permission
     * <ul>
     *   <li>Storage : for the getting image from the gallery</li>
     *   <li>Camera : for getting pictures from the gallery</li>
     *   <li>Location : for accessing GPS </li>
     *   <li>GPS : for navigation purpose</li>
     * </ul>
     */
    public void CheckAllPermissionNeeded() {
        Dexter
                .withContext(activity)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    Log.d("registeras", "All permission granted");
                    CheckGpsStatus();

                }

                // check for permanent deial of any permission
                if (report.isAnyPermissionPermanentlyDenied()) {
                    // show alert dialog navigating to settings
                    showSettingsDialog();
                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, final PermissionToken token) {
                showSeconodSettingsDialog();
            }

        }).withErrorListener(dexterError ->
                showSeconodSettingsDialog()).onSameThread().check();


    }

    private void showSeconodSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to work properly. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to work properly. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            activity.finish();
        });
        builder.setCancelable(false);
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }


    private void CheckGpsStatus() {
        LocationManager locationManager;
        boolean GpsStatus;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GpsStatus == false) {
            //"GPS Is Disabled" :: Enable it And get yourself
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Need GPS ON");
            builder.setMessage("GPS must be enable to work this app properly");
            builder.setPositiveButton("Turn On", (dialog, which) -> {
                Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent1, GPS_requestCode);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.cancel();
                activity.finish();
            });
            builder.setCancelable(false);
            builder.show();

        }
    }

}

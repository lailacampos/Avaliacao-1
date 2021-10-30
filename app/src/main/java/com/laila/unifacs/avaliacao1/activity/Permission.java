package com.laila.unifacs.avaliacao1.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Permission {

    public static Boolean[] checkPermission(Activity activity, String permission) {

        final Boolean[] isPermissionGranted = {false};

        Dexter.withContext(activity)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        // Logic for if the user grants permission
                        Toast.makeText(activity.getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                        isPermissionGranted[0] = true;

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // Logic for if user denies permission
                        Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                        Toast.makeText(activity.getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        activity.startActivity(intent);

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        // If the user neither grants nor denies permission, the alert box should keep on being shown
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
        return isPermissionGranted;
    }
}

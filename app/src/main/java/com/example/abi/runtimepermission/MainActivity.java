package com.example.abi.runtimepermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission
            .ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};

    Button prmsnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prmsnBtn = findViewById(R.id.main_permission_btn);

        prmsnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (arePermissionsEnabled()){
                        // permission granted continue the flow
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        requestMultiplePermission();
                    }
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMultiplePermission() {
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : permissions){
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                remainingPermissions.add(permission);
            }
        }
        requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), 101);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean arePermissionsEnabled() {
        for (String permission : permissions){
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101 ){
            for (int i=0; i<grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])){
                        new AlertDialog.Builder(this)
                                .setMessage("Permission Needed")
                                .setPositiveButton("Allow", (dialog, which) ->
                                requestMultiplePermission())
                                .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss()).create().show();
                    }
                    return;
                }
            }
        }
    }
}

package com.isupatches.wisefysample.ui;

import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.isupatches.wisefy.WiseFy;
import com.isupatches.wisefy.callbacks.GetNearbyAccessPointsCallbacks;
import com.isupatches.wisefysample.R;
import com.isupatches.wisefysample.constants.Permissions;
import com.isupatches.wisefysample.util.PermissionUtil;


import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private PermissionUtil permissionUtil;

    private WiseFy wiseFy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wiseFy = new WiseFy.brains(this).logging(true).getSmarts();
        permissionUtil = PermissionUtil.getInstance();

        if (checkForPermissions()) {
            getNearbyAccessPoints();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wiseFy.dump();
    }

    private boolean checkForPermissions() {
        return isPermissionGranted(ACCESS_COARSE_LOCATION, R.string.access_coarse_location_rationale, Permissions.ACCESS_COARSE_LOCATION_RESULT_CODE);
    }

    public boolean isPermissionGranted(String permission, int rationaleResId, int requestCode) {
        if (permissionUtil.permissionNotGranted(this, permission)) {
            if (permissionUtil.shouldShowPermissionRationale(this, permission)) {
                // TODO - Display dialog or rationale for requesting permission here
            } else {
                permissionUtil.requestPermissions(this, new String[]{permission}, requestCode);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Permissions.ACCESS_COARSE_LOCATION_RESULT_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Access course location permission granted");
                    // Continue WiseFy logic here
                    getNearbyAccessPoints();
                } else {
                    Log.e(TAG, "Access course location permission denied");
                    // TODO - Display permission error here
                }
                break;
            default:
                Log.wtf(TAG, "Weird permission requested, not handled");
                // TODO - Display permission error here
                break;
        }
    }

    private void getNearbyAccessPoints() {
        wiseFy.getNearbyAccessPoints(true, new GetNearbyAccessPointsCallbacks() {
            @Override
            public void getNearbyAccessPointsWiseFyFailure(Integer integer) {

            }

            @Override
            public void retrievedNearbyAccessPoints(List<ScanResult> list) {
                // You should see this populate with results after approving the
                // the ACCESS_COARSE_LOCATION permission
                Log.d(TAG, "List: " + list.toString());
            }
        });
    }
}
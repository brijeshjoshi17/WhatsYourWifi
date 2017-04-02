package brijeshjoshi.whatsyourwifi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.Locale;

/**
 * Created by brijeshjoshi on 4/1/17.
 */

class PermissionManager {
    private final Activity mActivity;
    private static final String[] APP_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
    };

    PermissionManager(Activity activity) {
        mActivity = activity;
    }

    boolean processPermissionResult(String[] permissions, int[] grantResults) {
        if (permissions.length != grantResults.length) {
            LogHelper.logError("Permissions requested don't match results");
            return false;
        }

        for (int i = 0; i < grantResults.length; i++) {
            int result = grantResults[i];
            if (result != PackageManager.PERMISSION_GRANTED) {
                LogHelper.logWarn(String.format(Locale.getDefault(), "%s not granted",
                        permissions[i]));
                return false;
            }
        }

        return true;
    }

    boolean hasPermissionsForApp() {
        for (String permission : APP_PERMISSIONS) {
            if (!hasPermission(mActivity, permission)) {
                return false;
            }
        }
        return true;
    }

    void requestAppPermissions(final int requestCode) {
        ActivityCompat.requestPermissions(mActivity, APP_PERMISSIONS, requestCode);
    }

    private static boolean hasPermission(final Context context, final String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager
                .PERMISSION_GRANTED;
    }
}

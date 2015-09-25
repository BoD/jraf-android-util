package org.jraf.android.util.app.permission;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Utility class dealing with permissions.
 */
public class PermissionUtil {
    /**
     * Determine whether you have been granted a list of permissions.
     *
     * @param permissions The names of the permissions being checked.
     * @return {@code true} if all the permissions are granted, {@code false} if at least one is not granted.
     */
    public static boolean areAllGranted(@NonNull Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }

    /**
     * Determine whether a particular permission was granted by the user.
     * This should be called from within {@link Activity#onRequestPermissionsResult}.
     *
     * @param permissions The requested permissions as passed to {@link Activity#onRequestPermissionsResult}.
     * @param grantResults The grant results for the corresponding permissions as passed to {@link Activity#onRequestPermissionsResult}.
     * @param permission The permission to test.
     * @return {@code true} if the permission was granted, {@code false} otherwise.
     */
    public static boolean wasGranted(@NonNull String[] permissions, @NonNull int[] grantResults, @NonNull String permission) {
        for (int i = 0; i < permissions.length; i++) {
            if (!permissions[i].equals(permission)) continue;
            return grantResults[i] == PackageManager.PERMISSION_GRANTED;
        }
        // The permission was not even found in the array, treat this as granted
        return true;
    }

    public static void requestMissingPermissions(Activity activity, int requestCode, String... permissions) {
        ArrayList<String> permissionsToAsk = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToAsk.add(permission);
            }
        }
        if (permissionsToAsk.isEmpty()) return;
        ActivityCompat.requestPermissions(activity, permissionsToAsk.toArray(new String[permissionsToAsk.size()]), requestCode);
    }
}
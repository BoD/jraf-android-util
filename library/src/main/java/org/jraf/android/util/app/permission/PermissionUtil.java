package org.jraf.android.util.app.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
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
}

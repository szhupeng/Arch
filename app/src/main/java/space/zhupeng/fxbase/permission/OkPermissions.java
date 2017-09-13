package space.zhupeng.fxbase.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhupeng
 * @date 2017/9/12
 */

public class OkPermissions {
    /**
     * Callback interface to receive the results of {@code EasyPermissions.requestPermissions()}
     * calls.
     */
    public interface PermissionCallback extends ActivityCompat.OnRequestPermissionsResultCallback {

        void onPermissionsGranted(int requestCode, List<String> permissions);

        void onPermissionsDenied(int requestCode, List<String> permissions);

    }

    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context     the calling context.
     * @param permissions one ore more permissions
     * @return true if all permissions are already granted, false if at least one permission is not
     * yet granted.
     */
    public static boolean checkPermissions(Context context, @NonNull String[] permissions) {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        // Null context may be passed if we have detected Low API (less than M) so getting
        // to this point with a null context should not be possible.
        if (context == null) {
            throw new IllegalArgumentException("Can't check permissions for null context");
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    /**
     * Request permissions from an Activity with standard OK/Cancel buttons.
     *
     * @see #requestPermissions(Activity, String, int, int, int, String...)
     */
    public static void requestPermissions(
            @NonNull Activity activity, @NonNull String rationale,
            int requestCode, @NonNull String[] permissions) {
        requestPermissions(activity, rationale, android.R.string.ok, android.R.string.cancel,
                requestCode, permissions);
    }

    /**
     * Request permissions from a Support Fragment with standard OK/Cancel buttons.
     *
     * @see #requestPermissions(Activity, String, int, int, int, String...)
     */
    public static void requestPermissions(
            @NonNull Fragment fragment, @NonNull String rationale,
            int requestCode, @NonNull String[] permissions) {

        requestPermissions(fragment, rationale, android.R.string.ok, android.R.string.cancel,
                requestCode, permissions);
    }

    /**
     * Request a set of permissions, showing rationale if the system requests it.
     *
     * @param activity    requesting context.
     * @param rationale   a message explaining why the application needs this set of permissions,
     *                    will be displayed if the user rejects the request the first time.
     * @param positive    custom text for positive button
     * @param negative    custom text for negative button
     * @param requestCode request code to track this request, must be &lt; 256.
     * @param permissions a set of permissions to be requested.
     */
    public static void requestPermissions(
            @NonNull Activity activity, @NonNull String rationale,
            @StringRes int positive, @StringRes int negative,
            int requestCode, @NonNull String[] permissions) {

        if (checkPermissions(activity, permissions)) return;

        PermissionsDispatcher.dispatch(activity, rationale, positive, negative, requestCode, permissions);
    }

    /**
     * Request permissions from a Support Fragment.
     *
     * @see #requestPermissions(Activity, String, int, int, int, String...)
     */
    public static void requestPermissions(
            @NonNull Fragment fragment, @NonNull String rationale,
            @StringRes int positive, @StringRes int negative,
            int requestCode, @NonNull String[] permissions) {
        if (checkPermissions(fragment.getContext(), permissions)) return;

        PermissionsDispatcher.dispatch(fragment, rationale, positive, negative, requestCode, permissions);
    }

    /**
     * Handle the result of a permission request, should be called from the calling {@link
     * Activity}'s {@link ActivityCompat.OnRequestPermissionsResultCallback#onRequestPermissionsResult(int,
     * String[], int[])} method.
     * <p>
     * If any permissions were granted or denied, the {@code object} will receive the appropriate
     * callbacks through {@link PermissionCallback} and methods annotated with {@link
     * OnPermissionsGranted} will be run if appropriate.
     *
     * @param requestCode  requestCode argument to permission result callback.
     * @param permissions  permissions argument to permission result callback.
     * @param grantResults grantResults argument to permission result callback.
     * @param receivers    an array of objects that have a method annotated with {@link
     *                     OnPermissionsGranted} or implement {@link PermissionCallback}.
     */
    public static void onRequestPermissionsResult(int requestCode,
                                                  @NonNull String[] permissions,
                                                  @NonNull int[] grantResults,
                                                  @NonNull Object... receivers) {
        // Make a collection of granted and denied permissions from the request.
        List<String> granted = new ArrayList<>();
        List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }

        // iterate through all receivers
        for (Object object : receivers) {
            // Report granted permissions, if any.
            if (!granted.isEmpty()) {
                if (object instanceof PermissionCallback) {
                    ((PermissionCallback) object).onPermissionsGranted(requestCode, granted);
                }
            }

            // Report denied permissions, if any.
            if (!denied.isEmpty()) {
                if (object instanceof PermissionCallback) {
                    ((PermissionCallback) object).onPermissionsDenied(requestCode, denied);
                }
            }

            // If 100% successful, call annotated methods
            if (!granted.isEmpty() && denied.isEmpty()) {
                invokeAnnotatedMethods(object, requestCode);
            }
        }
    }

    /**
     * Find all methods annotated with {@link OnPermissionsGranted} on a given object with the
     * correct requestCode argument.
     *
     * @param object      the object with annotated methods.
     * @param requestCode the requestCode passed to the annotation.
     */
    private static void invokeAnnotatedMethods(@NonNull Object object, int requestCode) {
        Class clazz = object.getClass();
        if (isUsingAndroidAnnotations(object)) {
            clazz = clazz.getSuperclass();
        }

        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                OnPermissionsGranted ann = method.getAnnotation(OnPermissionsGranted.class);
                if (ann != null) {
                    // Check for annotated methods with matching request code.
                    if (ann.value() == requestCode) {
                        // Method must be void so that we can invoke it
                        if (method.getParameterTypes().length > 0) {
                            throw new RuntimeException(
                                    "Cannot execute method " + method.getName() + " because it is non-void method and/or has input parameters.");
                        }

                        try {
                            // Make method accessible if private
                            if (!method.isAccessible()) {
                                method.setAccessible(true);
                            }
                            method.invoke(object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Determine if the project is using the AndroidAnnotations library.
     */
    private static boolean isUsingAndroidAnnotations(@NonNull Object object) {
        if (!object.getClass().getSimpleName().endsWith("_")) {
            return false;
        }
        try {
            Class clazz = Class.forName("org.androidannotations.api.view.HasViews");
            return clazz.isInstance(object);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
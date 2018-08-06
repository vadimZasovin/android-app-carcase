package com.imogene.android.carcase.commons.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Created by Admin on 11.04.2017.
 */

public final class AppUtils {

    private AppUtils(){}

    public static final class Commons{

        private Commons(){}

        public static boolean checkApiLevel(int requiredApiLevel){
            return Build.VERSION.SDK_INT >= requiredApiLevel;
        }

        public static boolean checkNetworkAvailability(Context context){
            ConnectivityManager manager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null
                    && networkInfo.isConnected()
                    && networkInfo.isAvailable();
        }

        public static void openSoftKeyboard(Activity activity){
            View currentFocus = activity.getCurrentFocus();
            if(currentFocus != null){
                InputMethodManager manager = (InputMethodManager)activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(currentFocus, 0);
            }
        }

        public static void openSoftKeyboard(Fragment fragment){
            openSoftKeyboard(fragment.getActivity());
        }

        public static void closeSoftKeyboard(Activity activity){
            View currentFocus = activity.getCurrentFocus();
            if(currentFocus != null){
                InputMethodManager manager = (InputMethodManager)activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }

        public static void closeSoftKeyboard(Fragment fragment){
            closeSoftKeyboard(fragment.getActivity());
        }
    }

    public static final class Graphics{

        private Graphics(){}

        public static int convertDpsInPixels(Context context, int dps){
            Resources resources = context.getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            return dps * displayMetrics.densityDpi / 160;
        }

        public static float convertDpsInPixels(Context context, float dps){
            Resources resources = context.getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            return dps * displayMetrics.density;
        }
    }

    public static final class Permissions{

        public static final int RESULT_HAS_PERMISSION = 0;
        public static final int RESULT_HAS_NOT_PERMISSION = 1;
        public static final int RESULT_SHOW_EXPLANATION = 2;

        private Permissions(){}

        public static boolean checkPermission(Context context, String permission){
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }

        public static int requestPermission(Activity activity, int requestCode,
                                            boolean withExplanation, String permission){
            if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                if(withExplanation && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
                    return RESULT_SHOW_EXPLANATION;
                }else {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                    return RESULT_HAS_NOT_PERMISSION;
                }
            }else {
                return RESULT_HAS_PERMISSION;
            }
        }

        public static int requestPermission(Fragment fragment, int requestCode,
                                            boolean withExplanation, String permission){
            Context context = fragment.getActivity();
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                if(withExplanation && fragment.shouldShowRequestPermissionRationale(permission)){
                    return RESULT_SHOW_EXPLANATION;
                }else {
                    fragment.requestPermissions(new String[]{permission}, requestCode);
                    return RESULT_HAS_NOT_PERMISSION;
                }
            }else {
                return RESULT_HAS_PERMISSION;
            }
        }
    }
}
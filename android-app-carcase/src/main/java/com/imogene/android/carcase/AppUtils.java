package com.imogene.android.carcase;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
}
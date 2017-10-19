package com.imogene.android.carcase.commons;

import android.app.Application;
import android.content.Context;

/**
 * Created by Admin on 13.04.2017.
 */

public class BaseApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
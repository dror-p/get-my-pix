package com.example.getmypix;

import android.content.Context;
import android.net.ConnectivityManager;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class GetMyPixApplication extends MultiDexApplication {
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static Context getContext(){
        return context;
    }
}


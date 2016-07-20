package com.tricycle.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtils {
	public static boolean isNetworkAvailable(Context context) {  
        ConnectivityManager connectivity = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (connectivity != null) {  
            NetworkInfo info = connectivity.getActiveNetworkInfo();  
            if (info != null && info.isConnected())   
            {
                if (info.getState() == NetworkInfo.State.CONNECTED)   
                {  
                    return true;  
                }  
            }  
        }  
        return false;  
    }  
}

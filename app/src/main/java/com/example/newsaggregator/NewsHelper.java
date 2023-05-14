package com.example.newsaggregator;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class NewsHelper {

    private static HashMap<String, String> colorHashmap = new HashMap<>();
    // For Checking Network Connection is Available or Not
    public static Boolean isNetworkConnected(Activity activity){
        ConnectivityManager connectivityManager = activity.getSystemService(ConnectivityManager.class);
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        if(network != null && network.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    // Color
    public static HashMap<String, String> textColor(HashSet<String> menuCategorySet){
        String[] colorArray =  new String[]{
                "#0000FF", "#8A2BE2", "#A52A2A", "#DEB887", "#5F9EA0", "#7FFF00",
                "#D2691E", "#FF7F50", "#6495ED", "#DC143C", "#00008B", "#008B8B"
        };
        List<String> colorList = new ArrayList<>(Arrays.asList(colorArray));
        int i = 0 ;
        for(String has : menuCategorySet){
            colorHashmap.put(has, colorList.get(i));
            i++;
        }
        return  colorHashmap;
    }

}

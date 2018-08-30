package com.example.android.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String PROVIDED_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    NetworkUtils() {
    }

    public static URL getJsonURL() {
        URL jsonURL = null;
        //retrieve Json String
        try {
            jsonURL = new URL(PROVIDED_URL);
        } catch (MalformedURLException error) {
            error.printStackTrace();
        }
        return jsonURL;
    }

    public static String fetchJSONString(URL jsonURL) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) jsonURL.openConnection();
        try {
            //start connection
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            //get data per line
            if (scanner.hasNextLine()) {
                StringBuilder data = new StringBuilder();
                while (scanner.hasNextLine()) {
                    data.append(scanner.nextLine());
                }
                return data.toString();
            } else
                return null;
        } finally {
            //end connection
            connection.disconnect();
        }
    }

    //checks for internet connection, returns true if available
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}

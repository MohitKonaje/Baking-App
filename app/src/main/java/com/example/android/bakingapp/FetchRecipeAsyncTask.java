package com.example.android.bakingapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.bakingapp.dataobjects.Recipe;
import com.example.android.bakingapp.utils.JsonUtils;
import com.example.android.bakingapp.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class FetchRecipeAsyncTask extends AsyncTaskLoader<ArrayList<Recipe>> {
    public FetchRecipeAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<Recipe> loadInBackground() {

        //get the url leading to json data
        URL jsonURL = NetworkUtils.getJsonURL();
        //fetch json string using HttpURLConnection
        String jsonString = "";
        try {
            jsonString = NetworkUtils.fetchJSONString(jsonURL);
        } catch (IOException error) {
            error.printStackTrace();
        }
        return JsonUtils.retrieveDataFromJSON(jsonString);
    }
}
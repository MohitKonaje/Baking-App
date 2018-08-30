package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.dataobjects.Ingredients;
import com.example.android.bakingapp.utils.AppKeys;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WidgetIngredientListAdapter extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private ArrayList<Ingredients> mIngredients;

    StackRemoteViewsFactory(Context passedContext) {
        this.mContext = passedContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        //fetch Ingredients list from shared preference
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String ingredientsJsonString = preferences.getString(AppKeys.SHARED_PREFERENCE_INGREDIENT_KEY, "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredients>>() {
        }.getType();
        mIngredients = gson.fromJson(ingredientsJsonString, type);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (mIngredients == null) {
            return 0;
        }
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        //set values for each remote view of an ingredient
        RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_item);
        row.setTextViewText(R.id.ingredient_item_name_text_view, mIngredients.get(position).ingredient);
        row.setTextViewText(R.id.ingredient_item_quantity_text_view, String.valueOf(mIngredients.get(position).quantity));
        row.setTextViewText(R.id.ingredient_item_measure_text_view, mIngredients.get(position).measure);
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}



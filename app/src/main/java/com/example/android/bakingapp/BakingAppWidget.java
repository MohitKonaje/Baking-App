package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.android.bakingapp.dataobjects.Ingredients;
import com.example.android.bakingapp.dataobjects.Recipe;
import com.example.android.bakingapp.dataobjects.Steps;
import com.example.android.bakingapp.utils.AppKeys;

import java.util.ArrayList;


public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent widgetClickIntent = PendingIntent.getActivity(context, AppKeys.WIDGET_TO_MAIN_ACTIVITY_REQUEST_CODE, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_recipe_title, widgetClickIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(AppKeys.ADD_TO_WIDGET)) {
            //receive data from broadcast package
            Recipe mRecipe = intent.getParcelableExtra(AppKeys.SELECTED_RECIPE_PARCEL_KEY);
            ArrayList<Steps> mSteps = intent.getParcelableArrayListExtra(AppKeys.STEPS_KEY);
            ArrayList<Ingredients> mIngredients = intent.getParcelableArrayListExtra(AppKeys.INGREDIENTS_KEY);

            //set views of the widget
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            views.setTextViewText(R.id.widget_recipe_title, mRecipe.name);
            views.setTextViewText(R.id.widget_recipe_ingredient, String.valueOf(mIngredients.size()));
            views.setTextViewText(R.id.widget_recipe_steps, String.valueOf(mSteps.size()));

            //set remote adapter for ingredients list
            Intent ingredientStack = new Intent(context, WidgetIngredientListAdapter.class);
            ingredientStack.putParcelableArrayListExtra(AppKeys.INGREDIENTS_KEY, mIngredients);
            views.setRemoteAdapter(R.id.ingredients_list_view, ingredientStack);

            //open Recipe Detail Activity of the Recipe set on widget when user taps on widget
            Intent recipeDetailActivityIntent = new Intent(context, RecipeDetailActivity.class);
            recipeDetailActivityIntent.setAction(AppKeys.FROM_WIDGET_CLICK);
            recipeDetailActivityIntent.putExtra(AppKeys.SELECTED_RECIPE_PARCEL_KEY, mRecipe);
            recipeDetailActivityIntent.putParcelableArrayListExtra(AppKeys.STEPS_KEY, mSteps);
            recipeDetailActivityIntent.putParcelableArrayListExtra(AppKeys.INGREDIENTS_KEY, mIngredients);

            // user should return to recipe select activity if the user presses back button
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(recipeDetailActivityIntent);

            PendingIntent widgetClickPendingIntent = stackBuilder.getPendingIntent(AppKeys.WIDGET_TO_RECIPE_DETAIL_ACTIVITY_REQUEST_CODE,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.baking_app_widget_container, widgetClickPendingIntent);

            //get instance of app widget manager
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //get id of the widget currently in use
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidget.class));

            //notify widget to change ingredients
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients_list_view);

            //finally update widget with updated views
            appWidgetManager.updateAppWidget(appWidgetIds, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}


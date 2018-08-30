package com.example.android.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.bakingapp.dataobjects.Ingredients;
import com.example.android.bakingapp.dataobjects.Recipe;
import com.example.android.bakingapp.dataobjects.Steps;
import com.example.android.bakingapp.utils.AppKeys;
import com.example.android.bakingapp.utils.JsonUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements SelectStepFragment.SelectedStepHandler, StepDetailFragment.stepNavigationButtonClickHandler {
    private Recipe mRecipe;
    private ArrayList<Steps> mSteps;
    private ArrayList<Ingredients> mIngredients;
    private int stepId;
    // a boolean used to identify the device
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent receivingIntent = getIntent();
        setContentView(R.layout.activity_recipe_detail);

        //find out which action led to this activity
        String action = receivingIntent.getAction();
        if (action != null) {
            switch (action) {
                case (AppKeys.FROM_MAIN_ACTIVITY):
                    //get Selected recipe from parcel by MainActivity
                    mRecipe = (Recipe) receivingIntent.getParcelableExtra(AppKeys.SELECTED_RECIPE_PARCEL_KEY);
                    //fetch Ingredients and steps for current recipe
                    mIngredients = JsonUtils.getIngredients(mRecipe.ingredientsJson);
                    mSteps = JsonUtils.getSteps(mRecipe.stepsJson);
                    //set step at first step if clicked for first time
                    stepId = 0;
                    break;

                case (AppKeys.FROM_WIDGET_CLICK):
                    //get Selected recipe from parcel by MainActivity
                    mRecipe = (Recipe) receivingIntent.getParcelableExtra(AppKeys.SELECTED_RECIPE_PARCEL_KEY);
                    mSteps = receivingIntent.getParcelableArrayListExtra(AppKeys.STEPS_KEY);
                    mIngredients = receivingIntent.getParcelableArrayListExtra(AppKeys.INGREDIENTS_KEY);
                    //set step at first step if clicked for first time
                    stepId = 0;
                    break;
            }
        }

        // if this Linear layout is inflated it would mean the app is running on a bigger screen device like tablet
        if (getResources().getBoolean(R.bool.isTablet)) {
            mTwoPane = true;
        }

        ActionBar actonBar = getSupportActionBar();
        actonBar.setTitle(mRecipe.name);

        if (savedInstanceState == null) {
            //create a fragment for steps menu list
            SelectStepFragment selectStepFragment = SelectStepFragment.newSelectStepFragmentInstance(mSteps, mIngredients, stepId);
            final FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // add step menu to activity layout
            fragmentTransaction.add(R.id.step_select_menu_fragment_container, selectStepFragment);

            if (mTwoPane) {
                //if app is in tablet mode inflate menu and detail fragments
                StepDetailFragment detailFragment = StepDetailFragment.newStepDetailFragmentInstance(mSteps, stepId);
                fragmentTransaction.add(R.id.step_detail_container, detailFragment);
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.widget_button) {
            //add recipe in a SharedPreference  which would help in retrieving ingredients list
            Gson gson = new Gson();
            String ingredientsJsonString = gson.toJson(mIngredients);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit().putString(AppKeys.SHARED_PREFERENCE_INGREDIENT_KEY, ingredientsJsonString).commit();

            //create broadcast intent for widget provider receiver
            Intent updateWidgetIntent = new Intent(this, BakingAppWidget.class);
            updateWidgetIntent.setAction(AppKeys.ADD_TO_WIDGET);
            updateWidgetIntent.putExtra(AppKeys.SELECTED_RECIPE_PARCEL_KEY, mRecipe);
            updateWidgetIntent.putParcelableArrayListExtra(AppKeys.STEPS_KEY, mSteps);
            updateWidgetIntent.putParcelableArrayListExtra(AppKeys.INGREDIENTS_KEY, mIngredients);
            sendBroadcast(updateWidgetIntent);
            return true;
        }
        return false;
    }

    @Override
    public void onStepSelected(int selectedStep) {
        stepId = selectedStep;
        //if app is running on phone let a separate activity open the step details
        if (!mTwoPane) {
            Intent stepDetailIntent = new Intent(this, StepDetailActivity.class);
            stepDetailIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            stepDetailIntent.setAction(AppKeys.RECIPE_DETAIL_ACTIVITY);
            stepDetailIntent.putExtra(AppKeys.SELECTED_RECIPE_PARCEL_KEY, mRecipe);
            stepDetailIntent.putParcelableArrayListExtra(AppKeys.STEPS_KEY, mSteps);
            stepDetailIntent.putExtra(AppKeys.STEP_ID_KEY, stepId);
            startActivity(stepDetailIntent);
        } else {
            updateMenuFragment();
            updateDetailFragment();
        }
    }

    @Override
    public void onButtonClick(int ClickedStepId) {
        //update step select menu fragment with current step
        stepId = ClickedStepId;
        if (mTwoPane)
            updateMenuFragment();
    }

    private void updateMenuFragment() {
        // add step menu to activity layout
        SelectStepFragment selectStepFragment = SelectStepFragment.newSelectStepFragmentInstance(mSteps, mIngredients, stepId);
        getSupportFragmentManager().beginTransaction().replace(R.id.step_select_menu_fragment_container, selectStepFragment).commit();
    }

    private void updateDetailFragment() {
        //update step details fragment to show selected step
        StepDetailFragment detailFragment = StepDetailFragment.newStepDetailFragmentInstance(mSteps, stepId);
        getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_container, detailFragment).commit();
    }
}

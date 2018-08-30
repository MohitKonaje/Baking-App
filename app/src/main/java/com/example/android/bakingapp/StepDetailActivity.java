package com.example.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.android.bakingapp.dataobjects.Recipe;
import com.example.android.bakingapp.dataobjects.Steps;
import com.example.android.bakingapp.utils.AppKeys;

import java.util.ArrayList;


public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.stepNavigationButtonClickHandler {

    private ArrayList<Steps> mSteps;
    private int stepId;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_step);
        Intent receivingIntent = getIntent();

        //find out which action led to this activity
        if (receivingIntent.getAction().equals(AppKeys.RECIPE_DETAIL_ACTIVITY)) {
            //get recipe
            mRecipe = receivingIntent.getParcelableExtra(AppKeys.SELECTED_RECIPE_PARCEL_KEY);
            //get Selected recipe from parcel by MainActivity
            mSteps = receivingIntent.getParcelableArrayListExtra(AppKeys.STEPS_KEY);
            //set step at first step if clicked for first time
            stepId = receivingIntent.getIntExtra(AppKeys.STEP_ID_KEY, 0);
        }

        android.support.v7.app.ActionBar actonBar = getSupportActionBar();
        actonBar.setTitle(mRecipe.name);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //run on full screen mode id phone is in landscape mode
            actonBar.hide();
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        //if phone is rotated restore state in fragment will load content
        if (savedInstanceState == null) {
            //set fragment
            StepDetailFragment detailFragment = StepDetailFragment.newStepDetailFragmentInstance(mSteps, stepId);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_fragment_container, detailFragment)
                    .commit();
        }
    }

    @Override
    public void onButtonClick(int newStepId) {
        stepId = newStepId;
    }
}

package com.example.android.bakingapp;

import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.dataobjects.Recipe;
import com.example.android.bakingapp.utils.AppKeys;
import com.example.android.bakingapp.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeSelectAdapter.recipeSelectOnClickHandler
        , LoaderCallbacks<ArrayList<Recipe>> {
    //recipe items
    private static final String RECIPE_PARCEL_KEY = "RECIPE_KEY";
    private static ArrayList<Recipe> mRecipe;
    //idling resource for espresso testing
    CountingIdlingResource mainActivityIdlingResource = new CountingIdlingResource("RECIPE_LOADER");
    //view objects
    @BindView(R.id.select_recipe_Recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_message_text)
    TextView mLoadMessageText;
    RecipeSelectAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialise views
        ButterKnife.bind(this);

        //if app is running on a tablet then set grid view layout of recipes
        if (findViewById(R.id.tablet_recipe_select_linear_layout) != null) {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
        } else {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
        }
        //create instance of adapter
        mAdapter = new RecipeSelectAdapter(this, this);

        // if there is data in savedInstanceState we need not have to download recipe again
        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_PARCEL_KEY)) {
            setDataVisibility(true);
            mRecipe = savedInstanceState.getParcelableArrayList(RECIPE_PARCEL_KEY);
            mAdapter.setData(mRecipe);
        } else {
            startRecipeFetchAsyncTask();
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_select_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_button) {
            startRecipeFetchAsyncTask();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startRecipeFetchAsyncTask() {
        // check for internet connection
        if (NetworkUtils.isInternetAvailable(this)) {
            setDataVisibility(false);
            //increment idling resource for espresso
            mainActivityIdlingResource.increment();
            // start an async task to fetch data
            getSupportLoaderManager().initLoader(0, null, this).startLoading();
        } else {
            //show error if network not available
            Toast.makeText(this, R.string
                    .no_internet_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickHandleMethod(Recipe selectedRecipe) {
        Intent recipeDetailIntent = new Intent(this, RecipeDetailActivity.class);

        //store selected recipe in a parcel for the next activity
        recipeDetailIntent.putExtra(AppKeys.SELECTED_RECIPE_PARCEL_KEY, selectedRecipe);
        recipeDetailIntent.setAction(AppKeys.FROM_MAIN_ACTIVITY);
        startActivity(recipeDetailIntent);
    }

    private void setDataVisibility(boolean visible) {
        if (visible) {
            mLoadMessageText.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mLoadMessageText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // add Recipes in Parcelable
        outState.putParcelableArrayList(RECIPE_PARCEL_KEY, mRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public android.support.v4.content.Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        return new FetchRecipeAsyncTask(this);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        mRecipe = data;
        //remove loading text
        setDataVisibility(true);
        mAdapter.setData(data);

        //decrement idling resource counter
        if (!mainActivityIdlingResource.isIdleNow())
            mainActivityIdlingResource.decrement();
    }

    @VisibleForTesting
    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<Recipe>> loader) {
    }

    public IdlingResource getIdlingResource() {
        return mainActivityIdlingResource;
    }
}



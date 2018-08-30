package com.example.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.dataobjects.Ingredients;
import com.example.android.bakingapp.dataobjects.Steps;
import com.example.android.bakingapp.utils.AppKeys;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SelectStepFragment extends Fragment implements SelectStepAdapter.StepItemClickListener {
    @BindView(R.id.total_ingredients_value)
    TextView mTotalIngredients;
    @BindView(R.id.select_step_recycler_view)
    RecyclerView mStepSelectRecyclerView;
    private ArrayList<Steps> mSteps;
    private ArrayList<Ingredients> mIngredients;
    private int stepId;
    private Unbinder unbinder;
    private SelectedStepHandler mCallback;

    public SelectStepFragment() {
        // Required empty public constructor
    }

    public static SelectStepFragment newSelectStepFragmentInstance(ArrayList<Steps> mSteps, ArrayList<Ingredients> mIngredients, int stepId) {
        //creates a fragment instance of this class with required data as parameters
        Bundle recipeValues = new Bundle();
        recipeValues.putParcelableArrayList(AppKeys.STEPS_KEY, mSteps);
        recipeValues.putParcelableArrayList(AppKeys.INGREDIENTS_KEY, mIngredients);
        recipeValues.putInt(AppKeys.STEP_ID_KEY, stepId);

        SelectStepFragment returnInstance = new SelectStepFragment();
        returnInstance.setArguments(recipeValues);
        return returnInstance;
    }

    @Override
    public void onAttach(Context context) {
        // throws exception if interface is'nt implemented
        super.onAttach(context);
        try {
            mCallback = (SelectedStepHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context mContext = getContext();
        final View rootView = inflater.inflate(R.layout.fragment_select_step, container, false);

        //fetch steps and ingredients from parent activity
        Bundle recipeValues = getArguments();
        if (recipeValues != null) {
            mSteps = recipeValues.getParcelableArrayList(AppKeys.STEPS_KEY);
            mIngredients = recipeValues.getParcelableArrayList(AppKeys.INGREDIENTS_KEY);
            stepId = recipeValues.getInt(AppKeys.STEP_ID_KEY);

            unbinder = ButterKnife.bind(this, rootView);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            mStepSelectRecyclerView.setLayoutManager(mLayoutManager);
            //store at least 15 recipes before recycling views
            mStepSelectRecyclerView.setItemViewCacheSize(15);
            mStepSelectRecyclerView.setHasFixedSize(true);
            SelectStepAdapter mAdapter = new SelectStepAdapter(mContext, mSteps, stepId, this);
            mStepSelectRecyclerView.setAdapter(mAdapter);

        }
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTotalIngredients.setText(String.valueOf(mIngredients.size()));
        //scroll to the selected step
        mStepSelectRecyclerView.getLayoutManager().scrollToPosition(stepId);
    }

    @Override
    public void onStepClick(int clickedStepId) {
        mCallback.onStepSelected(clickedStepId);
    }

    public interface SelectedStepHandler {
        //implement click listener which is to be handled by Recipe Detail Activity
        void onStepSelected(int updatedStepId);
    }
}
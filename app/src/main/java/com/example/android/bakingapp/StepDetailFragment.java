package com.example.android.bakingapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.dataobjects.Steps;
import com.example.android.bakingapp.utils.AppKeys;
import com.example.android.bakingapp.utils.NetworkUtils;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StepDetailFragment extends Fragment  {

    //keys used for exo player
    private static final String SEEKBAR_POSITION = "SEEKBAR_POSITION";
    private static final String PLAYER_STATUS = "PLAYER_STATUS";
    // using ButterKnife to initialise and bind views
    @BindView(R.id.short_description_text)
    TextView mShortDescriptionTextView;
    @BindView(R.id.description_text)
    TextView mDescriptionTextView;
    @BindView(R.id.next_button)
    Button mNextButton;
    @BindView(R.id.previous_button)
    Button mPreviousButton;
    @BindView(R.id.video_player)
    SimpleExoPlayerView mStepVideoView;
    private ArrayList<Steps> mSteps;
    private stepNavigationButtonClickHandler mClickHandler;
    private int stepId;
    private SimpleExoPlayer mExoPlayer;
    private Unbinder unbinder;
    private long seekBarPosition;
    private boolean playWhenReady;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newStepDetailFragmentInstance(ArrayList<Steps> mSteps, int stepId) {
        //create a fragment object of this class with required data in parameter of this method
        Bundle recipeValues = new Bundle();
        recipeValues.putParcelableArrayList(AppKeys.STEPS_KEY, mSteps);
        recipeValues.putInt(AppKeys.STEP_ID_KEY, stepId);

        StepDetailFragment returnInstance = new StepDetailFragment();
        returnInstance.setArguments(recipeValues);
        return returnInstance;
    }


    @Override
    public void onAttach(Context context) {
        // throws exception if interface is'nt implemented
        super.onAttach(context);
        try {
            mClickHandler = (stepNavigationButtonClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //fetch steps and ingredients from parent activity
        Bundle recipeValues = getArguments();
        if (recipeValues != null) {
            mSteps = recipeValues.getParcelableArrayList(AppKeys.STEPS_KEY);
            stepId = recipeValues.getInt(AppKeys.STEP_ID_KEY);
        }

        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //if device is rotated or set restore step data
        if (savedInstanceState != null) {
            stepId = savedInstanceState.getInt(AppKeys.STEP_ID_KEY);
            seekBarPosition = savedInstanceState.getLong(SEEKBAR_POSITION, 0);
            playWhenReady = savedInstanceState.getBoolean(PLAYER_STATUS, false);
        }
        bindViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Util.SDK_INT > 23){
            initializePlayer();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(Util.SDK_INT <= 23|| mExoPlayer==null){
            initializePlayer();
        }
            //set the seekbar and player play state if the player was already initialized
            mExoPlayer.seekTo(seekBarPosition);
            mExoPlayer.setPlayWhenReady(playWhenReady);
    }

    @Override
    public void onPause() {
        super.onPause();
        //save current seek bar of player
        seekBarPosition = mExoPlayer.getCurrentPosition();
        //save current player ready status
        playWhenReady = mExoPlayer.getPlayWhenReady();

        if(Util.SDK_INT <= 23){
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Util.SDK_INT > 23){
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void bindViews() {
        mShortDescriptionTextView.setText(mSteps.get(stepId).shortDescription);
        mDescriptionTextView.setText(mSteps.get(stepId).description);
        mPreviousButton.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
        // users should not be able to go to previous or next step if they they are on first or last step
        if (stepId == 0) {
            mPreviousButton.setVisibility(View.GONE);
        } else if (stepId == mSteps.size() - 1) {
            mNextButton.setVisibility(View.GONE);
        }
    }

    private void initializePlayer() {
        if (mExoPlayer != null) {
            releasePlayer();
        }

        if (mExoPlayer == null) {
            //extract video uri for video
            Uri stepVideoUri = Uri.parse(mSteps.get(stepId).videoURL);

            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mStepVideoView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            //if video uri is empty or the user does not have access to internet load the default image
            if (!(NetworkUtils.isInternetAvailable(getContext())) || stepVideoUri.toString().isEmpty()) {
                mStepVideoView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.default_food_image));
            }
            MediaSource mediaSource = new ExtractorMediaSource(stepVideoUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    @OnClick(R.id.next_button)
    public void nextButtonClick() {
        /* if next button is pressed we set the array index for next view binding,
            even if user clicks an invalid button the user should stay in the same step*/
        if (stepId < mSteps.size()) {
            stepId++;
            mClickHandler.onButtonClick(stepId);
        }
        bindViews();
        initializePlayer();
    }

    @OnClick(R.id.previous_button)
    public void previousButtonClick() {
        if (stepId > 0) {
            stepId--;
            mClickHandler.onButtonClick(stepId);
        }
        bindViews();
        initializePlayer();
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            //stop player
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(AppKeys.STEP_ID_KEY, stepId);
        outState.putLong(SEEKBAR_POSITION, seekBarPosition);
        outState.putBoolean(PLAYER_STATUS, playWhenReady);
    }

    public interface stepNavigationButtonClickHandler {
        void onButtonClick(int newStepId);
    }

}

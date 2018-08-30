package com.example.android.bakingapp.utils;

public final class AppKeys {
    //activity keys
    public static final String FROM_MAIN_ACTIVITY = "MAIN_ACTIVITY";
    public static final String RECIPE_DETAIL_ACTIVITY = "RECIPE_DETAIL_ACTIVITY";
    public static final String FROM_WIDGET_CLICK = "com.example.android.bakingapp.from_widget_click";
    public static final String ADD_TO_WIDGET = "com.example.android.bakingapp.add_to_widget";

    //parcel keys
    public static final String SELECTED_RECIPE_PARCEL_KEY = "SELECTED_RECIPE_KEY";
    public static final String STEP_ID_KEY = "STEP_ID_KEY";

    //ArrayList Parcel Keys
    public static final String STEPS_KEY = "STEPS";
    public static final String INGREDIENTS_KEY = "INGREDIENTS";

    //request codes
    public static final int WIDGET_TO_MAIN_ACTIVITY_REQUEST_CODE = 12;
    public static final int WIDGET_TO_RECIPE_DETAIL_ACTIVITY_REQUEST_CODE = 13;

    //shared preference keys
    public static final String SHARED_PREFERENCE_INGREDIENT_KEY = "SHARED_PREFERENCE_INGREDIENT_KEY";
}

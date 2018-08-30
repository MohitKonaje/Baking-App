package com.example.android.bakingapp.utils;

import com.example.android.bakingapp.dataobjects.Ingredients;
import com.example.android.bakingapp.dataobjects.Recipe;
import com.example.android.bakingapp.dataobjects.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
    private static final String JSON_ID_KEY = "id";
    private static final String JSON_NAME_KEY = "name";
    private static final String JSON_INGREDIENTS_KEY = "ingredients";
    private static final String JSON_STEPS_KEY = "steps";
    private static final String JSON_SERVINGS_KEY = "servings";
    private static final String JSON_IMAGE_KEY = "image";
    private static final String JSON_QUANTITY_KEY = "quantity";
    private static final String JSON_MEASURE_KEY = "measure";
    private static final String JSON_INGREDIENT_KEY = "ingredient";
    private static final String JSON_SHORT_DESCRIPTION_KEY = "shortDescription";
    private static final String JSON_DESCRIPTION_KEY = "description";
    private static final String JSON_VIDEO_URL_KEY = "videoURL";
    private static final String JSON_THUMBNAIL_KEY = "thumbnailURL";

    public static ArrayList<Recipe> retrieveDataFromJSON(String jsonString) {
        ArrayList<Recipe> returnRecipe = new ArrayList<Recipe>();
        try {
            JSONArray recipesJSONArray = new JSONArray(jsonString);
            for (int currentRecipe = 0; currentRecipe < recipesJSONArray.length(); currentRecipe++) {
                JSONObject selectedRecipe = recipesJSONArray.getJSONObject(currentRecipe);
                int id = selectedRecipe.getInt(JSON_ID_KEY);
                String name = selectedRecipe.getString(JSON_NAME_KEY);
                String ingredients = selectedRecipe.getString(JSON_INGREDIENTS_KEY);
                String steps = selectedRecipe.getString(JSON_STEPS_KEY);
                int servings = selectedRecipe.getInt(JSON_SERVINGS_KEY);
                String image = selectedRecipe.getString(JSON_IMAGE_KEY);
                returnRecipe.add(new Recipe(id, name, ingredients, steps, servings, image));
            }
            return returnRecipe;
        } catch (JSONException error) {
            error.printStackTrace();
        }
        return null;
    }

    //collects json and sends ingredients
    public static ArrayList<Ingredients> getIngredients(String ingredientsArrayString) {
        ArrayList<Ingredients> individualIngredients = new ArrayList<Ingredients>();
        try {
            JSONArray ingredients = new JSONArray(ingredientsArrayString);
            for (int currentIngredient = 0; currentIngredient < ingredients.length(); currentIngredient++) {
                JSONObject selectedIngredient = ingredients.getJSONObject(currentIngredient);
                int quantity = selectedIngredient.getInt(JSON_QUANTITY_KEY);
                String measure = selectedIngredient.getString(JSON_MEASURE_KEY);
                String ingredient = selectedIngredient.getString(JSON_INGREDIENT_KEY);
                individualIngredients.add(new Ingredients(quantity, measure, ingredient));
            }
        } catch (JSONException error) {
            error.printStackTrace();
        }
        //return empty ingredients if no ingredients found
        return individualIngredients;
    }

    //collects json and sends steps
    public static ArrayList<Steps> getSteps(String stepsArrayString) {
        ArrayList<Steps> individualSteps = new ArrayList<Steps>();
        try {
            JSONArray steps = new JSONArray(stepsArrayString);
            for (int currentStep = 0; currentStep < steps.length(); currentStep++) {
                JSONObject selectedStep = steps.getJSONObject(currentStep);
                int id = selectedStep.getInt(JSON_ID_KEY);
                String shortDescription = selectedStep.getString(JSON_SHORT_DESCRIPTION_KEY);
                String description = selectedStep.getString(JSON_DESCRIPTION_KEY);
                String videoURL = selectedStep.getString(JSON_VIDEO_URL_KEY);
                String thumbnailURL = selectedStep.getString(JSON_THUMBNAIL_KEY);
                individualSteps.add(new Steps(id, shortDescription, description, videoURL, thumbnailURL));
            }
        } catch (JSONException error) {
            error.printStackTrace();
        }
        //return collected steps
        return individualSteps;
    }
}

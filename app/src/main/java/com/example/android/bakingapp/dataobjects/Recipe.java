package com.example.android.bakingapp.dataobjects;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
    public final int id;
    public final String name;
    public final String ingredientsJson;
    public final String stepsJson;
    public final int servings;
    public final String image;

    public Recipe(int passedId, String passedName, String passedIngredients, String passedSteps, int passedServings, String passedImage) {
        this.id = passedId;
        this.name = passedName;
        this.ingredientsJson = passedIngredients;
        this.stepsJson = passedSteps;
        this.servings = passedServings;
        this.image = passedImage;
    }

    //parcelable implementation

    private Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredientsJson = in.readString();
        stepsJson = in.readString();
        servings = in.readInt();
        image = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(ingredientsJson);
        parcel.writeString(stepsJson);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };


}

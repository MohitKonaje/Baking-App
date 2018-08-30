package com.example.android.bakingapp.dataobjects;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {
    public final int quantity;
    public final String measure;
    public final String ingredient;

    public Ingredients(int passedQuantity, String passedMeasure, String passedIngredient) {
        this.quantity = passedQuantity;
        this.measure = passedMeasure;
        this.ingredient = passedIngredient;
    }

    private Ingredients(Parcel in) {
        quantity = in.readInt();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }
}

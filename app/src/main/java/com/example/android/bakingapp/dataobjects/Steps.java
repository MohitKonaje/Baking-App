package com.example.android.bakingapp.dataobjects;

import android.os.Parcel;
import android.os.Parcelable;

public class Steps implements Parcelable {
    public final int id;
    public final String shortDescription;
    public final String description;
    public final String videoURL;
    public final String thumbnailURL;

    public Steps(int passedId, String passedShortDescription, String passedDescription, String passedVideoURL, String passedThumbnailURL) {
        this.id = passedId;
        this.shortDescription = passedShortDescription;
        this.description = passedDescription;
        this.videoURL = passedVideoURL;
        this.thumbnailURL = passedThumbnailURL;
    }

    private Steps(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }
}

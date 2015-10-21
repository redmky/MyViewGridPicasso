package com.redmky.myviewgridpicasso;

/**
 * Created by redmky on 10/24/2015.
 */
public class MovieByIdInfo implements android.os.Parcelable {

    public static final android.os.Parcelable.Creator<MovieByIdInfo>
            CREATOR = new android.os.Parcelable.Creator<MovieByIdInfo>() {

        @Override
        public MovieByIdInfo createFromParcel(android.os.Parcel source) {
            return new MovieByIdInfo(source);
        }

        @Override
        public MovieByIdInfo[] newArray(int size) {
            return new MovieByIdInfo[size];
        }
    };
    public String id;
    public String key;
    public String mode;

    public MovieByIdInfo(String id, String key, String mode) {

        this.id = id;
        this.key = key;
        this.mode = mode;
    }

    public MovieByIdInfo(android.os.Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.mode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Storing the Movie data to Parcel object
    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(mode);
    }


}

package com.example.mapsdevicesfeatures.Operations;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

// Brenna Pavlinchak
// AD3 - C202504
// PhotoData

public class PhotoData implements Parcelable
{
    private final Uri photoUri;
    private final String note;
    private final double latitude;
    private final double longitude;

    public PhotoData(Uri photoUri, String note, double latitude, double longitude)
    {
        this.photoUri = photoUri;
        this.note = note;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected PhotoData(Parcel in)
    {
        photoUri = in.readParcelable(Uri.class.getClassLoader());
        note = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<PhotoData> CREATOR = new Creator<PhotoData>()
    {
        @Override
        public PhotoData createFromParcel(Parcel in)
        {
            return new PhotoData(in);
        }

        @Override
        public PhotoData[] newArray(int size)
        {
            return new PhotoData[size];
        }
    };

    public Uri getPhotoUri()
    {
        return photoUri;
    }

    public String getNote()
    {
        return note;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags)
    {
        dest.writeParcelable(photoUri, flags);
        dest.writeString(note);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
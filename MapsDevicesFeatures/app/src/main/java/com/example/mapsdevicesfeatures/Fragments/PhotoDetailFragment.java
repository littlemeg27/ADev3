package com.example.mapsdevicesfeatures.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mapsdevicesfeatures.Operations.PhotoData;
import com.example.mapsdevicesfeatures.R;
import java.util.Locale;

// Brenna Pavlinchak
// AD3 - C202504
// PhotoDetailFragment

public class PhotoDetailFragment extends Fragment
{
    private static final String ARG_PHOTO_DATA = "photo_data";
    private PhotoData photoData;

    public static PhotoDetailFragment newInstance(PhotoData photoData)
    {
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTO_DATA, photoData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            photoData = getArguments().getParcelable(ARG_PHOTO_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);

        ImageView photoImage = view.findViewById(R.id.detail_photo);
        TextView noteText = view.findViewById(R.id.detail_note);
        TextView locationText = view.findViewById(R.id.detail_location);

        if (photoData != null)
        {
            photoImage.setImageURI(photoData.getPhotoUri());
            noteText.setText(photoData.getNote());
            locationText.setText(String.format(Locale.US, "Lat: %.6f, Lon: %.6f",
                    photoData.getLatitude(), photoData.getLongitude()));
        }

        return view;
    }
}
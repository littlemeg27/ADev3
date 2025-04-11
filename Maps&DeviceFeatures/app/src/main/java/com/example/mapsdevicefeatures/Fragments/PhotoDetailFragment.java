package com.example.mapsdevicefeatures.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.mapsdevicefeatures.Operations.PhotoData;
import com.example.mapsdevicefeatures.R;
import java.util.Locale;

public class PhotoDetailFragment extends Fragment
{
    private static final String ARG_PHOTO_DATA = "photo_data";
    private PhotoData photoData;

    public PhotoDetailFragment()
    {
        // Required empty public constructor
    }

    public static PhotoDetailFragment newInstance(PhotoData photoData) {
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTO_DATA, photoData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            photoData = getArguments().getParcelable(ARG_PHOTO_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);

        ImageView photoImage = view.findViewById(R.id.photo_image);
        TextView noteText = view.findViewById(R.id.note_text);
        TextView locationText = view.findViewById(R.id.location_text);

        if (photoData != null)
        {
            photoImage.setImageURI(photoData.getPhotoUri());
            noteText.setText(getString(R.string.photo_note, photoData.getNote()));
            locationText.setText(String.format(Locale.US,
                    getString(R.string.photo_location),
                    photoData.getLatitude(),
                    photoData.getLongitude()));
        }

        return view;
    }
}
package com.example.mapsdevicefeatures.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mapsdevicefeatures.Operations.MainActivity;
import com.example.mapsdevicefeatures.Operations.PhotoData;
import com.example.mapsdevicefeatures.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener
{
    private MapView mapView;
    private GoogleMap googleMap;
    private static final List<PhotoData> photoList = new ArrayList<>();

    public MapFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Button takePhotoButton = view.findViewById(R.id.take_photo_button);
        takePhotoButton.setOnClickListener(v -> ((MainActivity) requireActivity()).switchToCameraFragment());

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map)
    {
        googleMap = map;
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.setOnInfoWindowClickListener(this);

        for (PhotoData photo : photoList)
        {
            LatLng position = new LatLng(photo.getLatitude(), photo.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title("Photo")
                    .snippet(photo.getNote()));
        }

        if (!photoList.isEmpty())
        {
            LatLng firstPhoto = new LatLng(photoList.get(0).getLatitude(), photoList.get(0).getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPhoto, 15));
        }
        else
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.590647, -81.304510), 10));
        }
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker)
    {
        for (PhotoData photo : photoList)
        {
            if (photo.getLatitude() == marker.getPosition().latitude && photo.getLongitude() == marker.getPosition().longitude)
            {
                PhotoDetailFragment detailFragment = PhotoDetailFragment.newInstance(photo);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            }
        }
    }

    @Override public void onResume() { super.onResume(); mapView.onResume(); }
    @Override public void onPause() { super.onPause(); mapView.onPause(); }
    @Override public void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }

    public static void addPhoto(PhotoData photo)
    {
        photoList.add(photo);
    }
}
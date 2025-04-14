package com.example.mapsdevicefeatures.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mapsdevicefeatures.Operations.MainActivity;
import com.example.mapsdevicefeatures.Operations.PhotoData;
import com.example.mapsdevicefeatures.Operations.PhotoDataEntity;
import com.example.mapsdevicefeatures.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

        new Thread(() ->
        {
            List<PhotoDataEntity> entities = MainActivity.database.photoDao().getAllPhotos();
            photoList.clear();
            for (PhotoDataEntity entity : entities)
            {
                photoList.add(new PhotoData(
                        Uri.parse(entity.photoUri),
                        entity.note,
                        entity.latitude,
                        entity.longitude
                ));
            }
            requireActivity().runOnUiThread(() ->
            {
                if (googleMap != null)
                {
                    googleMap.clear();
                    for (PhotoData photo : photoList)
                    {
                        LatLng position = new LatLng(photo.getLatitude(), photo.getLongitude());
                        googleMap.addMarker(new MarkerOptions()
                                .position(position)
                                .title("Photo")
                                .snippet(photo.getNote()));
                    }
                }
            });
        }).start();

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
            FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            locationClient.getLastLocation().addOnSuccessListener(location ->
            {
                if (location != null)
                {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                }
                else
                {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.590647, -81.304510), 10));
                }
            });
        }
        else
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.590647, -81.304510), 10));
        }

        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
        {
            @Override
            public View getInfoWindow(@NonNull Marker marker)
            {
                return null;
            }

            @Override
            public View getInfoContents(@NonNull Marker marker)
            {
                View view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_info_window, null, false);
                TextView title = view.findViewById(R.id.info_title);
                TextView snippet = view.findViewById(R.id.info_snippet);

                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());

                return view;
            }
        });
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker)
    {
        for (PhotoData photo : photoList)
        {
            if (Math.abs(photo.getLatitude() - marker.getPosition().latitude) < 0.00001 && Math.abs(photo.getLongitude() - marker.getPosition().longitude) < 0.00001)
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

    public static void addPhoto(PhotoData photo) {
        photoList.add(photo);
    }
}
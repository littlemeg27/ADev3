package com.example.mapsdevicesfeatures.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.mapsdevicesfeatures.Operations.AppDatabase;
import com.example.mapsdevicesfeatures.Operations.PhotoData;
import com.example.mapsdevicesfeatures.Operations.PhotoDataEntity;
import com.example.mapsdevicesfeatures.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

// Brenna Pavlinchak
// AD3 - C202504
// MapFragment

public class MapFragment extends Fragment implements OnMapReadyCallback
{
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String> requestLocationPermissionLauncher;
    private List<PhotoDataEntity> photos;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestLocationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted ->
                {
                    if (isGranted)
                    {
                        if (mMap != null)
                        {
                            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                            {
                                mMap.setMyLocationEnabled(true);
                                getDeviceLocation();
                                loadPhotoMarkers();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null)
        {
            mapFragment.getMapAsync(this);
        }
        else
        {
            Toast.makeText(requireContext(), "Map fragment not found", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(marker ->
        {
            String note = marker.getTitle();

            for (PhotoDataEntity photo : photos)
            {
                if (photo.note.equals(note))
                {
                    PhotoData photoData = new PhotoData(
                            Uri.parse(photo.photoUri),
                            photo.note,
                            photo.latitude,
                            photo.longitude
                    );
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, PhotoDetailFragment.newInstance(photoData))
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
            }
            return false;
        });

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mMap.setMyLocationEnabled(true);
            getDeviceLocation();
            loadPhotoMarkers();
        }
        else
        {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void getDeviceLocation()
    {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location ->
            {
                if (location != null)
                {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
                }
                else
                {
                    Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadPhotoMarkers()
    {
        new Thread(() ->
        {
            photos = AppDatabase.getDatabase(requireContext()).photoDao().getAllPhotos();
            requireActivity().runOnUiThread(() ->
            {
                mMap.clear();
                for (PhotoDataEntity photo : photos)
                {
                    LatLng position = new LatLng(photo.latitude, photo.longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(photo.note));
                }
            });
        }).start();
    }

    public static void addPhoto(@SuppressWarnings("unused") PhotoData photo)
    {
        // Handled by database and refreshed on resume
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mMap != null)
        {
            loadPhotoMarkers();
        }
    }
}
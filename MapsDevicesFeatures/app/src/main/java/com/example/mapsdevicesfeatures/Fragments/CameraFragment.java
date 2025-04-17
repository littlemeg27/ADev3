package com.example.mapsdevicesfeatures.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.mapsdevicesfeatures.Operations.MainActivity;
import com.example.mapsdevicesfeatures.Operations.PhotoData;
import com.example.mapsdevicesfeatures.Operations.PhotoDataEntity;
import com.example.mapsdevicesfeatures.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Brenna Pavlinchak
// AD3 - C202504
// CameraFragment

public class CameraFragment extends Fragment
{
    private Uri photoUri;
    private ImageView photoPreview;
    private EditText noteInput;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<String> requestLocationPermissionLauncher;

    public CameraFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        if (photoUri != null)
                        {
                            photoPreview.setImageURI(photoUri);
                        }
                        else
                        {
                            Toast.makeText(requireContext(), "Failed to load photo", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Photo capture cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

        requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted ->
                {
                    if (isGranted)
                    {
                        dispatchTakePictureIntent();
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
                    }
                });

        requestLocationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted ->
                {
                    if (isGranted)
                    {
                        savePhoto();
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        photoPreview = view.findViewById(R.id.photo_preview);
        noteInput = view.findViewById(R.id.note_input);
        Button captureButton = view.findViewById(R.id.capture_button);
        Button saveButton = view.findViewById(R.id.save_button);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        captureButton.setOnClickListener(v ->
        {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            {
                dispatchTakePictureIntent();
            }
            else
            {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });
        saveButton.setOnClickListener(v -> savePhoto());

        return view;
    }

    private void dispatchTakePictureIntent()
    {
        Log.d("CameraFragment", "Attempting to dispatch camera intent");

        if (!requireContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
        {
            Log.e("CameraFragment", "No camera hardware available");
            Toast.makeText(requireContext(), "No camera hardware available", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> activities = requireContext().getPackageManager().queryIntentActivities(takePictureIntent, 0);

        if (activities.isEmpty())
        {
            Log.e("CameraFragment", "No activities found to handle camera intent");
            Toast.makeText(requireContext(), "No camera app available", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("CameraFragment", "Found " + activities.size() + " activities to handle camera intent");

        for (ResolveInfo info : activities)
        {
            Log.d("CameraFragment", "Activity: " + info.activityInfo.packageName);
        }

        File photoFile;

        try
        {
            photoFile = createImageFile();
            photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.mapsdevicesfeatures.mappingphotos.fileprovider",
                    photoFile
            );
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            Log.d("CameraFragment", "Launching camera intent with URI: " + photoUri);
            takePictureLauncher.launch(takePictureIntent);
        }
        catch (IOException ex)
        {
            Log.e("CameraFragment", "Error creating file", ex);
            Toast.makeText(requireContext(), "Error creating file", Toast.LENGTH_SHORT).show();
        }
        catch (IllegalArgumentException ex)
        {
            Log.e("CameraFragment", "FileProvider error", ex);
            Toast.makeText(requireContext(), "FileProvider error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (storageDir == null)
        {
            throw new IOException("Storage directory not available");
        }
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void savePhoto()
    {
        if (photoUri == null)
        {
            Toast.makeText(requireContext(), "Please take a photo first", Toast.LENGTH_SHORT).show();
            return;
        }

        String note = noteInput.getText().toString().trim();
        if (note.isEmpty())
        {
            Toast.makeText(requireContext(), "Please enter a note", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location ->
                {
                    if (location != null)
                    {
                        PhotoData photo = new PhotoData(photoUri, note, location.getLatitude(), location.getLongitude());
                        MapFragment.addPhoto(photo);

                        new Thread(() ->
                        {
                            PhotoDataEntity entity = new PhotoDataEntity(
                                    photo.getPhotoUri().toString(),
                                    photo.getNote(),
                                    photo.getLatitude(),
                                    photo.getLongitude()
                            );
                            MainActivity.database.photoDao().insert(entity);
                        }).start();

                        Toast.makeText(requireContext(), "Photo saved", Toast.LENGTH_SHORT).show();
                        ((MainActivity) requireActivity()).switchToMapFragment();
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Location error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
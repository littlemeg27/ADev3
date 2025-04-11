package com.example.mapsdevicefeatures.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.example.mapsdevicefeatures.Operations.MainActivity;
import com.example.mapsdevicefeatures.Operations.PhotoData;
import com.example.mapsdevicefeatures.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraFragment extends Fragment
{
    private Uri photoUri;
    private ImageView photoPreview;
    private EditText noteInput;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<Intent> takePictureLauncher;
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
                    if (result.getResultCode() == requireActivity().RESULT_OK)
                    {
                        photoPreview.setImageURI(photoUri);
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

        captureButton.setOnClickListener(v -> dispatchTakePictureIntent());
        saveButton.setOnClickListener(v -> savePhoto());

        return view;
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null)
        {
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch (IOException ex)
            {
                Toast.makeText(requireContext(), "Error creating file", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null)
            {
                photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.mapsdevicefeatures.fileprovider", // Updated to match package
                        photoFile
                );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                takePictureLauncher.launch(takePictureIntent);
            }
        }
    }

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

        fusedLocationClient.getLastLocation().addOnSuccessListener(location ->
        {
            if (location != null)
            {
                PhotoData photo = new PhotoData(photoUri, note, location.getLatitude(), location.getLongitude());
                MapFragment.addPhoto(photo);
                Toast.makeText(requireContext(), "Photo saved", Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).switchToMapFragment();
            }
            else
            {
                Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
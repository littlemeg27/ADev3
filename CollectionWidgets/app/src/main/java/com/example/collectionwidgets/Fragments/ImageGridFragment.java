package com.example.collectionwidgets.Fragments;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.collectionwidgets.Model.ImageData;
import com.example.collectionwidgets.Operations.FlipperWidgetProvider;
import com.example.collectionwidgets.R;

import java.util.ArrayList;
import java.util.List;

// Brenna Pavlinchak
// AD3 - C202504
// ImageGridFragment

public class ImageGridFragment extends Fragment
{
    private GridView gridView;
    private Button refreshButton;
    private ImageAdapter adapter;
    private final List<ImageData> imageList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_image_grid, container, false);

        gridView = view.findViewById(R.id.image_grid);
        refreshButton = view.findViewById(R.id.refresh_button);
        adapter = new ImageAdapter();
        gridView.setAdapter(adapter);

        loadImages();

        refreshButton.setOnClickListener(v ->
        {
            loadImages();
            FlipperWidgetProvider.updateAllWidgets(requireContext());
        });

        gridView.setOnItemClickListener((parent, v, position, id) ->
        {
            ImageData image = imageList.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(image.getUri(), "image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        });

        return view;
    }

    private void loadImages()
    {
        imageList.clear();
        ContentResolver resolver = requireContext().getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = resolver.query(uri, projection, null, null, null);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                Uri imageUri = Uri.withAppendedPath(uri, String.valueOf(id));
                imageList.add(new ImageData(imageUri, name));
            }
            cursor.close();
        }

        adapter.notifyDataSetChanged();
    }

    private class ImageAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return imageList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return imageList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.widget_stack_item, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.widget_image);
            ImageData image = imageList.get(position);
            Glide.with(requireContext())
                    .load(image.getUri())
                    .centerCrop()
                    .into(imageView);
            return convertView;
        }
    }
}
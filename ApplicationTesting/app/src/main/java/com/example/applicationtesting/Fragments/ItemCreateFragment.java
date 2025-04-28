package com.example.applicationtesting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.applicationtesting.data.StorageUtil;
import com.example.applicationtesting.databinding.FragmentItemCreateBinding;
import com.example.applicationtesting.model.Item;
import java.util.UUID;

public class ItemCreateFragment extends Fragment {
    private FragmentItemCreateBinding binding;
    private StorageUtil storageUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemCreateBinding.inflate(inflater, container, false);
        storageUtil = new StorageUtil();

        binding.buttonSave.setOnClickListener(v -> {
            String name = binding.editTextName.getText().toString();
            String description = binding.editTextDescription.getText().toString();
            if (!name.isEmpty() && !description.isEmpty()) {
                Item item = new Item(UUID.randomUUID().toString(), name, description);
                storageUtil.saveItem(item);
                requireActivity().onBackPressed();
            }
        });

        return binding.getRoot();
    }
}
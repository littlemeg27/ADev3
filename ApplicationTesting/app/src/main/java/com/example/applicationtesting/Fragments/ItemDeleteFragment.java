package com.example.applicationtesting.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.applicationtesting.data.StorageUtil;
import com.example.applicationtesting.databinding.FragmentItemDeleteBinding;

public class ItemDeleteFragment extends Fragment {
    private FragmentItemDeleteBinding binding;
    private StorageUtil storageUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemDeleteBinding.inflate(inflater, container, false);
        storageUtil = new StorageUtil();

        binding.buttonDelete.setOnClickListener(v -> {
            String id = binding.editTextId.getText().toString();
            if (!id.isEmpty()) {
                storageUtil.deleteItem(id);
                requireActivity().onBackPressed();
            }
        });

        return binding.getRoot();
    }
}
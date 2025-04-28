package com.example.applicationtesting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.applicationtesting.R;
import com.example.applicationtesting.data.StorageUtil;
import com.example.applicationtesting.databinding.FragmentItemListBinding;

public class ItemListFragment extends Fragment {
    private FragmentItemListBinding binding;
    private StorageUtil storageUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemListBinding.inflate(inflater, container, false);
        storageUtil = new StorageUtil();

        // Display items
        updateItemList();

        // Navigate to Create Fragment
        binding.buttonCreate.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_list_to_create));

        // Navigate to Delete Fragment
        binding.buttonDelete.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_list_to_delete));

        return binding.getRoot();
    }

    private void updateItemList() {
        StringBuilder itemText = new StringBuilder();
        for (Item item : storageUtil.loadItems()) {
            itemText.append(item.getName()).append(": ").append(item.getDescription()).append("\n");
        }
        binding.textViewItems.setText(itemText.toString());
    }
}

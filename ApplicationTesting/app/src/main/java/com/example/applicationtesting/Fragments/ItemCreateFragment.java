package com.example.applicationtesting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.applicationtesting.Model.Item;
import com.example.applicationtesting.Util.StorageUtil;
import com.example.applicationtesting.R;
import com.example.applicationtesting.databinding.FragmentItemCreateBinding;
import java.util.UUID;

public class ItemCreateFragment extends Fragment
{
    private FragmentItemCreateBinding binding;
    private StorageUtil storageUtil;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentItemCreateBinding.inflate(inflater, container, false);
        storageUtil = new StorageUtil();
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_item_create, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.action_save)
        {
            String name = binding.editTextName.getText().toString();
            String description = binding.editTextDescription.getText().toString();

            if (!name.isEmpty() && !description.isEmpty())
            {
                Item newItem = new Item(UUID.randomUUID().toString(), name, description);
                storageUtil.saveItem(newItem);
                Navigation.findNavController(requireView()).popBackStack();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
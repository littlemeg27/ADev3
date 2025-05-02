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
import com.example.applicationtesting.Util.StorageUtil;
import com.example.applicationtesting.R;
import com.example.applicationtesting.databinding.FragmentItemDeleteBinding;

// Brenna Pavlinchak
// AD3 - C202504
// ItemDeleteFragment

public class ItemDeleteFragment extends Fragment
{
    private FragmentItemDeleteBinding binding;
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
        binding = FragmentItemDeleteBinding.inflate(inflater, container, false);
        storageUtil = new StorageUtil();
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_item_delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.action_delete)
        {
            String id = binding.editTextId.getText().toString();
            if (!id.isEmpty())
            {
                storageUtil.deleteItem(id);
                Navigation.findNavController(requireView()).popBackStack();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
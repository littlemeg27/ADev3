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
import com.example.applicationtesting.databinding.FragmentItemListBinding;

public class ItemListFragment extends Fragment
{
    private FragmentItemListBinding binding;
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
        binding = FragmentItemListBinding.inflate(inflater, container, false);
        storageUtil = new StorageUtil();

        updateItemList();

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_item_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int itemId = item.getItemId();

        if (itemId == R.id.action_create)
        {
            Navigation.findNavController(requireView()).navigate(R.id.action_list_to_create);
            return true;

        }
        else if (itemId == R.id.action_delete)
        {
            Navigation.findNavController(requireView()).navigate(R.id.action_list_to_delete);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateItemList()
    {
        StringBuilder itemText = new StringBuilder();

        for (Item item : storageUtil.loadItems())
        {
            itemText.append("ID: ").append(item.getId())
                    .append(", ").append(item.getName())
                    .append(": ").append(item.getDescription())
                    .append("\n");
        }
        binding.textViewItems.setText(itemText.toString());
    }
}
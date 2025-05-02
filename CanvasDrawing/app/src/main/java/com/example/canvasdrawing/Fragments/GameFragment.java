package com.example.canvasdrawing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import com.example.canvasdrawing.Operations.MainActivity;
import com.example.canvasdrawing.R;

// Brenna Pavlinchak
// AD3 - C202504
// GameFragment

public class GameFragment extends Fragment implements MenuProvider
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner());
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater)
    {
        menuInflater.inflate(R.menu.game_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem)
    {
        if (menuItem.getItemId() == R.id.action_inventory)
        {
            MainActivity activity = (MainActivity) getActivity();

            if (activity != null)
            {
                activity.loadFragment(new InventoryFragment(), "InventoryFragment");
                return true;
            }
        }
        return false;
    }
}
package com.example.canvasdrawing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.canvasdrawing.Operations.GameSurfaceView;
import com.example.canvasdrawing.Operations.Item;
import com.example.canvasdrawing.R;

import java.util.ArrayList;
import java.util.List;

// Brenna Pavlinchak
// AD3 - C202504
// InventoryFragment

public class InventoryFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        TextView inventoryText = view.findViewById(R.id.inventory_text);

        GameFragment gameFragment = (GameFragment) getParentFragmentManager()
                .findFragmentByTag("GameFragment");
        List<Item> foundItems = gameFragment != null
                ? ((GameSurfaceView) gameFragment.getView().findViewById(R.id.game_surface_view)).getFoundItems()
                : new ArrayList<>();

        int totalGold = 0;
        StringBuilder inventoryString = new StringBuilder("Inventory:\n\n");

        for (Item item : foundItems)
        {
            inventoryString.append(item.getName()).append(" (").append(item.getValue()).append(" gold)\n");
            totalGold += item.getValue();
        }
        inventoryString.append("\nTotal Gold: ").append(totalGold);

        inventoryText.setText(inventoryString.toString());

        return view;
    }
}
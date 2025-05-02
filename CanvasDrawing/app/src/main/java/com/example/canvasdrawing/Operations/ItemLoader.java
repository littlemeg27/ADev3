package com.example.canvasdrawing.Operations;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Brenna Pavlinchak
// AD3 - C202504
// ItemLoader

public class ItemLoader
{
    private static final String CSV_FILE = "items.csv";
    private List<Item> items = new ArrayList<>();

    public ItemLoader(Context context, int boardWidth, int boardHeight)
    {
        loadItems(context);
        assignRandomPositions(boardWidth, boardHeight);
    }

    private void loadItems(Context context)
    {
        try
        {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(CSV_FILE)));
            String line;

            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(",");

                if (parts.length == 2)
                {
                    String name = parts[0].trim();
                    int value = Integer.parseInt(parts[1].trim());
                    items.add(new Item(name, value));
                }
            }
            reader.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void assignRandomPositions(int boardWidth, int boardHeight)
    {
        Random random = new Random();

        for (Item item : items)
        {
            float x = random.nextFloat() * boardWidth;
            float y = random.nextFloat() * boardHeight;
            item.setPosition(x, y);
        }
    }

    public List<Item> getItems()
    {
        return items;
    }
}

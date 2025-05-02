package com.example.applicationtesting.Util;

import com.example.applicationtesting.Model.Item;
import java.util.ArrayList;
import java.util.List;

// Brenna Pavlinchak
// AD3 - C202504
// StorageUtil

public class StorageUtil
{
    private List<Item> items = new ArrayList<>();
    private int nextId = 1;

    public void saveItem(Item item)
    {
        Item testItem = new Item(String.valueOf(nextId++), item.getName(), item.getDescription());
        items.add(testItem);
    }

    public List<Item> loadItems()
    {
        return new ArrayList<>(items);
    }

    public void deleteItem(String id)
    {
        items.removeIf(item -> item.getId().equals(id));
    }
}
package com.example.applicationtesting.Util;


import com.example.applicationtesting.Model.Item;
import java.util.ArrayList;
import java.util.List;

public class StorageUtil {
    private List<Item> items = new ArrayList<>();

    public void saveItem(Item item) {
        items.add(item);
    }

    public List<Item> loadItems() {
        return new ArrayList<>(items);
    }

    public void deleteItem(String id) {
        items.removeIf(item -> item.getId().equals(id));
    }
}

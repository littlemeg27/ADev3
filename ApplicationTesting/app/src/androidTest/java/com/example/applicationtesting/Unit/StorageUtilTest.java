package com.example.applicationtesting.Unit;

import com.example.applicationtesting.Util.StorageUtil;
import com.example.applicationtesting.Model.Item;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class StorageUtilTest
{
    private StorageUtil storageUtil;

    @Before
    public void setUp()
    {
        storageUtil = new StorageUtil();
    }

    @Test
    public void testSaveItem()
    {
        Item item = new Item("1", "Test Item", "Test Description");
        storageUtil.saveItem(item);
        List<Item> items = storageUtil.loadItems();
        assertEquals(1, items.size());
        assertEquals("Test Item", items.get(0).getName());
    }

    @Test
    public void testLoadItems()
    {
        Item item1 = new Item("1", "Item 1", "Desc 1");
        Item item2 = new Item("2", "Item 2", "Desc 2");
        storageUtil.saveItem(item1);
        storageUtil.saveItem(item2);
        List<Item> items = storageUtil.loadItems();
        assertEquals(2, items.size());
    }

    @Test
    public void testDeleteItem()
    {
        Item item = new Item("1", "Test Item", "Test Description");
        storageUtil.saveItem(item);
        storageUtil.deleteItem("1");
        List<Item> items = storageUtil.loadItems();
        assertEquals(0, items.size());
    }
}

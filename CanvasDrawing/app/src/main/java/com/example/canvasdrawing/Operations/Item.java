package com.example.canvasdrawing.Operations;

import android.graphics.PointF;

// Brenna Pavlinchak
// AD3 - C202504
// Item

public class Item
{
    private String name;
    private int value;
    private PointF position;
    private boolean isFound;

    public Item(String name, int value)
    {
        this.name = name;
        this.value = value;
        this.position = new PointF();
        this.isFound = false;
    }

    public String getName() { return name; }
    public int getValue() { return value; }
    public PointF getPosition() { return position; }
    public void setPosition(float x, float y) { position.set(x, y); }
    public boolean isFound() { return isFound; }
    public void setFound(boolean found) { isFound = found; }
}
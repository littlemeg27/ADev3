package com.example.canvasdrawing.Operations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.canvasdrawing.R;

import java.util.ArrayList;
import java.util.List;

// Brenna Pavlinchak
// AD3 - C202504
// GameSurfaceView

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    private final SurfaceHolder holder;
    private Thread drawThread;
    private boolean isRunning;
    private final Bitmap groundTexture;
    private final Bitmap dirtHoleTexture;
    private List<Item> items;
    private final List<Item> foundItems = new ArrayList<>();
    private final List<PointF> dugSpots = new ArrayList<>();
    private final Paint textPaint;
    private static final float DIG_RADIUS = 50f;

    public GameSurfaceView(Context context)
    {
        this(context, null);
    }

    public GameSurfaceView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);

        groundTexture = BitmapFactory.decodeResource(context.getResources(), R.drawable.field);
        dirtHoleTexture = BitmapFactory.decodeResource(context.getResources(), R.drawable.hole);
        items = new ItemLoader(context, getWidth(), getHeight()).getItems();
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40f);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder)
    {
        isRunning = true;
        drawThread = new Thread(this);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height)
    {
        items = new ItemLoader(getContext(), width, height).getItems();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder)
    {
        isRunning = false;
        boolean retry = true;

        while (retry)
        {
            try
            {
                drawThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
                Log.e("GameSurfaceView", "Error joining draw thread", e);
            }
        }
    }

    @Override
    public void run()
    {
        while (isRunning)
        {
            if (!holder.getSurface().isValid()) continue;
            Canvas canvas = holder.lockCanvas();

            if (canvas != null)
            {
                drawGame(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawGame(Canvas canvas)
    {
        canvas.drawBitmap(groundTexture, null, canvas.getClipBounds(), null);

        for (PointF spot : dugSpots)
        {
            canvas.drawBitmap(dirtHoleTexture, spot.x - dirtHoleTexture.getWidth() / 2f,
                    spot.y - dirtHoleTexture.getHeight() / 2f, null);
        }
        int hiddenTreasure = items.size() - foundItems.size();
        canvas.drawText("Hidden Treasure: " + hiddenTreasure, 20, 60, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            float x = event.getX();
            float y = event.getY();
            dugSpots.add(new PointF(x, y));
            for (Item item : items) {
                if (item.isFound()) continue;
                PointF pos = item.getPosition();
                float distance = (float) Math.sqrt(Math.pow(pos.x - x, 2) + Math.pow(pos.y - y, 2));

                if (distance <= DIG_RADIUS)
                {
                    item.setFound(true);
                    foundItems.add(item);
                    Toast.makeText(getContext(), "Found: " + item.getName() + " (" + item.getValue() + " gold)", Toast.LENGTH_SHORT).show();
                }
            }
            performClick();
        }
        return true;
    }

    @Override
    public boolean performClick()
    {
        super.performClick();
        return true;
    }

    public List<Item> getFoundItems()
    {
        return foundItems;
    }
}
package com.example.collectionwidgets.Operations;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collectionwidgets.Fragments.ImageGridFragment;
import com.example.collectionwidgets.R;

// Brenna Pavlinchak
// AD3 - C202504
// MainActivity

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ImageGridFragment())
                    .commit();
        }
    }
}
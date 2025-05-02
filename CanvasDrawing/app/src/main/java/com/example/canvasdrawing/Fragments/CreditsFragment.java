package com.example.canvasdrawing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.canvasdrawing.R;

// Brenna Pavlinchak
// AD3 - C202504
// CreditsFragment

public class CreditsFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_credits, container, false);

        TextView creditsText = view.findViewById(R.id.credits_text);
        creditsText.setText("Credits\n\nDeveloped by: [Your Name]\nArt by: Full Sail University");

        return view;
    }
}

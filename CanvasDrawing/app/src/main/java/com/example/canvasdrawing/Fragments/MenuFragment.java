package com.example.canvasdrawing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.canvasdrawing.Operations.MainActivity;
import com.example.canvasdrawing.R;

// Brenna Pavlinchak
// AD3 - C202504
// MenuFragment

public class MenuFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        Button startButton = view.findViewById(R.id.start_button);
        Button creditsButton = view.findViewById(R.id.credits_button);

        startButton.setOnClickListener(v ->
        {
            MainActivity activity = (MainActivity) getActivity();

            if (activity != null)
            {
                activity.loadFragment(new GameFragment(), "GameFragment");
            }
        });

        creditsButton.setOnClickListener(v ->
        {
            MainActivity activity = (MainActivity) getActivity();

            if (activity != null)
            {
                activity.loadFragment(new CreditsFragment(), "CreditsFragment");
            }
        });

        return view;
    }
}
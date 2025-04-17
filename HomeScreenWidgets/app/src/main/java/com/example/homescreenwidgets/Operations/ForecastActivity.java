package com.example.homescreenwidgets.Operations;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.homescreenwidgets.Model.ForecastData;
import com.example.homescreenwidgets.R;

import java.util.ArrayList;
import java.util.List;

public class ForecastActivity extends AppCompatActivity {

    private LinearLayout forecastContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        forecastContainer = findViewById(R.id.forecast_container);
        String city = getIntent().getStringExtra("city");

        new FetchForecastTask().execute(city);
    }

    private class FetchForecastTask extends AsyncTask<String, Void, List<ForecastData>> {
        @Override
        protected List<ForecastData> doInBackground(String... params) {
            // Mock forecast data (replace with real API call)
            List<ForecastData> forecastData = new ArrayList<>();
            forecastData.add(new ForecastData("Tomorrow", "sunny", 26, 18));
            forecastData.add(new ForecastData("Day 2", "cloudy", 24, 17));
            forecastData.add(new ForecastData("Day 3", "rainy", 22, 16));
            return forecastData;
        }

        @Override
        protected void onPostExecute(List<ForecastData> forecastData) {
            forecastContainer.removeAllViews();
            for (ForecastData data : forecastData) {
                View forecastView = LayoutInflater.from(ForecastActivity.this)
                        .inflate(R.layout.forecast_item, forecastContainer, false);

                TextView dayView = forecastView.findViewById(R.id.forecast_day);
                ImageView iconView = forecastView.findViewById(R.id.forecast_icon);
                TextView highLowView = forecastView.findViewById(R.id.forecast_high_low);

                dayView.setText(data.getDay());
                iconView.setImageResource(getIconResource(data.getIcon()));
                highLowView.setText(String.format("High: %d°C Low: %d°C", data.getHighTemp(), data.getLowTemp()));

                forecastContainer.addView(forecastView);
            }
        }
    }

    private int getIconResource(String icon) {
        switch (icon) {
            case "cloudy":
                return R.mipmap.ic_cloudy;
            case "rainy":
                return R.mipmap.ic_rainy;
            default:
                return R.mipmap.ic_sunny;
        }
    }
}

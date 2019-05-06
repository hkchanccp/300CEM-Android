package com.example.cem300assignment.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cem300assignment.Common.Common;
import com.example.cem300assignment.Model.WeatherForecastResult;
import com.example.cem300assignment.R;
import com.squareup.picasso.Picasso;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder> {
    private WeatherForecastResult weatherForecastResult;

    // Constructor
    public WeatherForecastAdapter(WeatherForecastResult weatherForecastResult) {
        this.weatherForecastResult = weatherForecastResult;
    }

    // Provide reference to view (Bind Elements)
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_date_time, txt_description,txt_temperature;
        private ImageView img_weather;

        ViewHolder(View itemView) {
            super(itemView);
            img_weather = itemView.findViewById(R.id.img_weather);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_date_time = itemView.findViewById(R.id.txt_date_time);
            txt_temperature = itemView.findViewById(R.id.txt_temperature);
        }
    }

    // Create item view
    @Override
    public WeatherForecastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_forecast,parent,false);
        return new WeatherForecastAdapter.ViewHolder(itemView);
    }

    // Replace contents of view
    @Override
    public void onBindViewHolder(WeatherForecastAdapter.ViewHolder vh, int position) {

        Picasso.get().load("https://openweathermap.org/img/w/" +
                weatherForecastResult.list.get(position).weather.get(0).getIcon() +
                ".png").into(vh.img_weather);

        vh.txt_date_time.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResult.list.get(position).dt)));
        vh.txt_description.setText(new StringBuilder(weatherForecastResult.list.get(position).weather.get(0).getDescription()));
        vh.txt_temperature.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).main.getTemp())).append("°C"));
    }

    // Return size of data
    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }
}

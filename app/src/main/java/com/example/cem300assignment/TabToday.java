package com.example.cem300assignment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.cem300assignment.Common.Common;
import com.example.cem300assignment.Model.WeatherResult;
import com.example.cem300assignment.Retrofix.IOpenWeather;
import com.example.cem300assignment.Retrofix.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TabToday extends Fragment {

    public final int PERM_REQ = 1001;
    private Button btn_retry;
    private ImageView img_weather;
    private TextView txt_city_name, txt_humidity, txt_sunrise, txt_sunset, txt_pressure,
            txt_temperature, txt_description, txt_date_time, txt_wind, txt_Geo_coord;
    private ConstraintLayout weather_panel;
    private TableLayout weather_detail_panel;
    private ProgressBar loading;
    private String lat = "", lng = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today_weather, container, false);

        weather_panel = rootView.findViewById(R.id.weather_panel);
        weather_panel.setVisibility(View.INVISIBLE);
        weather_detail_panel = rootView.findViewById(R.id.weather_detail_panel);
        weather_detail_panel.setVisibility(View.INVISIBLE);

        img_weather = rootView.findViewById(R.id.img_weather);
        txt_city_name = rootView.findViewById(R.id.txt_city_name);
        txt_humidity = rootView.findViewById(R.id.txt_Humidity);
        txt_sunrise = rootView.findViewById(R.id.txt_Sunrise);
        txt_sunset = rootView.findViewById(R.id.txt_Sunset);
        txt_pressure = rootView.findViewById(R.id.txt_Pressure);
        txt_temperature = rootView.findViewById(R.id.txt_temperature);
        txt_description = rootView.findViewById(R.id.txt_description);
        txt_wind = rootView.findViewById(R.id.txt_wind);
        txt_Geo_coord = rootView.findViewById(R.id.txt_Geo_coord);
        txt_date_time = rootView.findViewById(R.id.txt_date_time);

        loading = rootView.findViewById(R.id.loading);
        btn_retry = rootView.findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherInfo();
            }
        });

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERM_REQ);
        } else {
            getWeatherInfo();
        }

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERM_REQ:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getWeatherInfo();
                } else {
                    Snackbar.make(getView(), "Permission Denied.", Snackbar.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getWeatherInfo() {
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location loc;
        try {
            loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lat = String.valueOf(loc.getLatitude());
            lng = String.valueOf(loc.getLongitude());
        } catch (SecurityException err){
            Log.d("Error", err.getLocalizedMessage());
            Snackbar.make(getView(), "Oops! Something goes wrong", Snackbar.LENGTH_LONG).show();
        }

        IOpenWeather mService = RetrofitClient.getInstance().create(IOpenWeather.class);
        new CompositeDisposable().add(mService.getWeatherByLatLng(lat, lng, Common.APP_ID, "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                               @Override
                               public void accept(WeatherResult weatherResult) {
                                   // Load img
                                   Picasso.get().load("https://openweathermap.org/img/w/" + weatherResult.getWeather().get(0).getIcon() +
                                           ".png").into(img_weather);

                                   // load city
                                   txt_city_name.setText(weatherResult.getName());
                                   txt_description.setText("The current weather is:");
                                   // load temperature
                                   txt_temperature.setText(weatherResult.getMain().getTemp() + "°C");
                                   // load date time
                                   txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                                   // load pressure
                                   txt_pressure.setText(weatherResult.getMain().getPressure() + " hpa");
                                   // load humidity
                                   txt_humidity.setText(weatherResult.getMain().getHumidity() + "%");
                                   // load wind speed
                                   txt_wind.setText("Spd: " + weatherResult.getWind().getSpeed() + " Deg: " + weatherResult.getWind().getDeg());
                                   // load sunrise
                                   txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                                   // load sunset
                                   txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                                   // load coordinate
                                   txt_Geo_coord.setText(weatherResult.getCoord().toString());

                                   //Display panel
                                   weather_panel.setVisibility(View.VISIBLE);
                                   weather_detail_panel.setVisibility(View.VISIBLE);
                                   loading.setVisibility(View.GONE);
                                   btn_retry.setVisibility(View.GONE);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                Log.d("Error", throwable.getMessage());
                                Snackbar.make(getView(), "Oops! Something goes wrong", Snackbar.LENGTH_LONG).show();
                            }
                        }));
    }

}
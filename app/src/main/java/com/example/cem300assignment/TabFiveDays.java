package com.example.cem300assignment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cem300assignment.Adapter.WeatherForecastAdapter;
import com.example.cem300assignment.Common.Common;
import com.example.cem300assignment.Model.WeatherForecastResult;
import com.example.cem300assignment.Retrofix.IOpenWeather;
import com.example.cem300assignment.Retrofix.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TabFiveDays extends Fragment {

    public final int PERM_REQ = 1001;
    private Button btn_retry;
    private FloatingActionButton fab;
    private TextView txt_Geo_coord;
    private TextView txt_description;
    private RecyclerView recycler_forecast;
    private String lat = "", lng = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);

        txt_Geo_coord = rootView.findViewById(R.id.txt_Geo_coord);
        txt_description = rootView.findViewById(R.id.txt_description);
        recycler_forecast = rootView.findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        btn_retry = rootView.findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecastWeatherInfo();
            }
        });

        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_forecast.smoothScrollToPosition(0);
            }
        });

        getForecastWeatherInfo();

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERM_REQ:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getForecastWeatherInfo();
                } else {
                    Snackbar.make(getView(), "Permission Denied.", Snackbar.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getForecastWeatherInfo() {
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
        new CompositeDisposable().add(mService.getForecastWeatherByLatLng(lat, lng, Common.APP_ID, "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) {
                        txt_Geo_coord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));
                        txt_description.setText(new StringBuilder(weatherForecastResult.city.name.toString()));
                        WeatherForecastAdapter adapter = new WeatherForecastAdapter(weatherForecastResult);
                        recycler_forecast.setAdapter(adapter);
                        btn_retry.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.d("Error", throwable.getMessage());
                        Snackbar.make(getView(), "Oops! Something goes wrong", Snackbar.LENGTH_LONG).show();
                    }
                }));
    }

}
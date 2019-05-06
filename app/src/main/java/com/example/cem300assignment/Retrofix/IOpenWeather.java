package com.example.cem300assignment.Retrofix;

import com.example.cem300assignment.Model.WeatherForecastResult;
import com.example.cem300assignment.Model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeather {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat, @Query("lon")
            String lng, @Query("appid") String appid, @Query("units") String units);

    @GET("forecast")
    Observable<WeatherForecastResult> getForecastWeatherByLatLng(@Query("lat") String lat, @Query("lon")
            String lng, @Query("appid") String appid, @Query("units") String units);
}

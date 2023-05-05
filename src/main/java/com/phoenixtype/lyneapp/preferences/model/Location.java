package com.phoenixtype.lyneapp.preferences.model;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.io.IOException;

public class Location {
    private LatLng coordinates;

    public Location(String city, String country) throws InterruptedException, ApiException, IOException {
        String address = city + ", " + country;

        // TODO Put the api key in configuration file
        GeoApiContext context = new GeoApiContext.Builder().apiKey("YOUR_API_KEY").build();
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();

        if (results.length > 0) {
            this.coordinates = results[0].geometry.location;
        }
    }

    public double getLatitude() {
        return this.coordinates.lat;
    }

    public double getLongitude() {
        return this.coordinates.lng;
    }
}

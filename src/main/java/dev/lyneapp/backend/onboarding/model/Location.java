package dev.lyneapp.backend.onboarding.model;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class Location {
    private LatLng coordinates;
    private String city;
    private String country;

    //TODO You may need to comment this temporarily when testing
    @Value("${google.maps.api.key}")
    private String apiKey;

    public Location(String city, String country) throws InterruptedException, ApiException, IOException {
        String address = city + ", " + country;

        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();

        if (results.length > 0) {
            this.coordinates = results[0].geometry.location;
            this.city = results[0].addressComponents[0].longName;
            this.country = results[0].addressComponents[results[0].addressComponents.length-1].longName;
        }
    }

    public double getLatitude() {
        return this.coordinates.lat;
    }

    public double getLongitude() {
        return this.coordinates.lng;
    }

    public String getCity() { return this.city; }

    public String getCountry() { return this.country; }
}

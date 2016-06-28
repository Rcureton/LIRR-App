package com.example.mom.lirrapp.Weather;

/**
 * Created by MOM on 5/22/16.
 */
public class WeatherConversion {
    public static Double convertToCelsius(String kelvin) throws NumberFormatException {
        double inKelvin;
        try {
            inKelvin = Double.parseDouble(kelvin);
        } catch (NumberFormatException e) {
            throw e;
        }
        return inKelvin - 273.15;
    }

    public static Double convertToFahrenheit(String kelvin) throws NumberFormatException {
        double inKelvin;
        try {
            inKelvin = Double.parseDouble(kelvin);
        } catch (NumberFormatException e) {
            throw e;
        }
        return (inKelvin - 273.15) * 1.8000 + 32.00;
    }

}

package com.crossover.trial.weather.controller;

import static com.crossover.trial.weather.controller.AirportController.airportDataRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.repository.WeatherRepository;
import com.google.gson.Gson;

/**
 * A Controller of the WeatherQuery API.
 *
 * @author luisfsosa@gmail.com
 */
public class WeatherQueryController {

    /**
     * Log of the Class.
     */
    public static final Logger LOGGER = Logger
            .getLogger(WeatherQueryController.class.getName());

    /** earth radius in KM */
    public static final double EARTH_RADIUS = 6372.8;

    /** shared gson json to object factory */
    public static final Gson GSON = new Gson();

    // TODO: Inject AirportDataRepository.

    /** all known Weathers */
    // TODO: Inject WeatherRepository.
    protected static WeatherRepository weatherRepository = new WeatherRepository();

    /**
     * Retrieve service health including total size of valid data points and
     * request frequency information.
     *
     * @return health stats for the service as a string
     */
    public String ping() {
        Map<String, Object> retrieveValue = new HashMap<>();
        retrieveValue.put("datasize", getSize());
        retrieveValue.put("iata_freq", getFreqs());
        retrieveValue.put("radius_freq", getHits());
        return GSON.toJson(retrieveValue);
    }

    /**
     * Retrieve total size of valid data points.
     *
     * @return total size of valid data points.
     */
    public int getSize() {
        int datasize = 0;
        AtmosphericInformation atmosphericInformation = null;
        for (AirportData airportData : airportDataRepository.findAll()) {

            atmosphericInformation = airportData.getAtmosphericInformation();
            // we only count recent readings
            if (atmosphericInformation.getCloudCover() != null
                    || atmosphericInformation.getHumidity() != null
                    || atmosphericInformation.getPressure() != null
                    || atmosphericInformation.getPrecipitation() != null
                    || atmosphericInformation.getTemperature() != null
                    || atmosphericInformation.getWind() != null) {
                // updated in the last day
                if (atmosphericInformation
                        .getLastUpdateTime() > System.currentTimeMillis()
                                - 86400000) {
                    datasize++;
                }
            }
        }

        return datasize;
    }

    /**
     * Retrieve request frequency information.
     *
     * @return request frequency information.
     */
    public Map<String, Double> getFreqs() {
        Map<String, Double> freqMap = new HashMap<>();
        // fraction of queries
        for (AirportData data : airportDataRepository.findAll()) {
            double frac = (double) weatherRepository.findAllRequestFrequency()
                    .getOrDefault(data, 0)
                    / weatherRepository.findAllRequestFrequency().size();
            freqMap.put(data.getIata(), frac);
        }
        return freqMap;
    }

    /**
     * Retrieve hits information.
     *
     * @returnhits information.
     */
    public int[] getHits() {

        int m = weatherRepository.findAllRadiusFreqMap().keySet().stream()
                .max(Double::compare).orElse(1000.0).intValue() + 1;

        int[] hist = new int[m];
        for (Map.Entry<Double, Integer> e : weatherRepository
                .findAllRadiusFreqMap().entrySet()) {
            int i = e.getKey().intValue() % 10;
            hist[i] += e.getValue();
        }

        return hist;
    }

    /**
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the
     * requested airport information and return a list of matching atmosphere
     * information.
     *
     * @param iata
     *            the iataCode
     * @param radiusString
     *            the radius in km
     *
     * @return a list of atmospheric information
     */
    public List<AtmosphericInformation> weather(String iata,
            String radiusString) {

        double radius = radiusString == null || radiusString.trim().isEmpty()
                ? 0 : Double.valueOf(radiusString);

        updateRequestFrequency(iata, radius);

        List<AtmosphericInformation> retrieveValue = new ArrayList<>();
        if (radius == 0) {
            retrieveValue.add(airportDataRepository.findOne(iata)
                    .getAtmosphericInformation());
        } else {
            AirportData airportData = airportDataRepository.findOne(iata);
            for (int i = 0; i < airportDataRepository.findAll().size(); i++) {
                if (calculateDistance(airportData,
                        airportDataRepository.findAll().get(i)) <= radius) {
                    AtmosphericInformation atmosphericInformation = airportDataRepository
                            .findAll().get(i).getAtmosphericInformation();
                    if (atmosphericInformation.getCloudCover() != null
                            || atmosphericInformation.getHumidity() != null
                            || atmosphericInformation.getPrecipitation() != null
                            || atmosphericInformation.getPressure() != null
                            || atmosphericInformation.getTemperature() != null
                            || atmosphericInformation.getWind() != null) {
                        retrieveValue.add(atmosphericInformation);
                    }
                }
            }
        }
        return retrieveValue;
    }

    /**
     * Records information about how often requests are made
     *
     * @param iata
     *            an iata code
     * @param radius
     *            query radius
     */
    public void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = airportDataRepository.findOne(iata);
        weatherRepository.findAllRequestFrequency().put(airportData,
                weatherRepository.findAllRequestFrequency()
                        .getOrDefault(airportData, 0) + 1);
        weatherRepository.findAllRadiusFreqMap().put(radius, weatherRepository
                .findAllRadiusFreqMap().getOrDefault(radius, 0));
    }

    /**
     * Haversine distance between two airports.
     *
     * @param airportData1
     *            airport 1
     * @param airportData2
     *            airport 2
     * @return the distance in KM
     */
    public double calculateDistance(AirportData airportData1,
            AirportData airportData2) {
        double deltaLatitud = Math.toRadians(
                airportData2.getLatitude() - airportData1.getLatitude());
        double deltaLongitud = Math.toRadians(
                airportData2.getLongitude() - airportData1.getLongitude());
        double a = Math.pow(Math.sin(deltaLatitud / 2), 2)
                + Math.pow(Math.sin(deltaLongitud / 2), 2)
                        * Math.cos(airportData1.getLatitude())
                        * Math.cos(airportData2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }

    /**
     * A dummy init method that loads hard coded data
     */
    public static void init() {
        airportDataRepository.findAll().clear();
        weatherRepository.findAllRequestFrequency().clear();

        airportDataRepository.addAirport("BOS", 42.364347, -71.005181);
        airportDataRepository.addAirport("EWR", 40.6925, -74.168667);
        airportDataRepository.addAirport("JFK", 40.639751, -73.778925);
        airportDataRepository.addAirport("LGA", 40.777245, -73.872608);
        airportDataRepository.addAirport("MMU", 40.79935, -74.4148747);
    }

}

package com.crossover.trial.weather.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.repository.AirportDataRepository;
import com.crossover.trial.weather.service.WeatherQueryEndpoint;
import com.google.gson.Gson;

/**
 * The Weather App REST endpoint allows clients to query, update and check
 * health stats. Currently, all data is held in memory. The end point deploys to
 * a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

    /**
     * Log of the Class.
     */
    public static final Logger LOGGER = Logger
            .getLogger(RestWeatherQueryEndpoint.class.getName());

    /** earth radius in KM */
    public static final double EARTH_RADIUS = 6372.8;

    /** shared gson json to object factory */
    public static final Gson GSON = new Gson();

    /** all known airports */
    protected static AirportDataRepository airportDataRepository = new AirportDataRepository();

    /**
     * Internal performance counter to better understand most requested
     * information, this map can be improved but for now provides the basis for
     * future performance optimizations. Due to the stateless deployment
     * architecture we don't want to write this to disk, but will pull it off
     * using a REST request and aggregate with other performance metrics
     * {@link #ping()}
     */
    public static Map<AirportData, Integer> requestFrequencyMap = new HashMap<AirportData, Integer>();

    public static Map<Double, Integer> radiusFreqMap = new HashMap<Double, Integer>();

    /**
     * Retrieve service health including total size of valid data points and
     * request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    public String ping() {
        Map<String, Object> retrieveValue = new HashMap<>();
        AtmosphericInformation atmosphericInformation = null;

        int datasize = 0;
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
        retrieveValue.put("datasize", datasize);

        Map<String, Double> freq = new HashMap<>();
        // fraction of queries
        for (AirportData data : airportDataRepository.findAll()) {
            double frac = (double) requestFrequencyMap.getOrDefault(data, 0)
                    / requestFrequencyMap.size();
            freq.put(data.getIata(), frac);
        }
        retrieveValue.put("iata_freq", freq);

        int m = radiusFreqMap.keySet().stream().max(Double::compare)
                .orElse(1000.0).intValue() + 1;

        int[] hist = new int[m];
        for (Map.Entry<Double, Integer> e : radiusFreqMap.entrySet()) {
            int i = e.getKey().intValue() % 10;
            hist[i] += e.getValue();
        }
        retrieveValue.put("radius_freq", hist);

        return GSON.toJson(retrieveValue);
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
    @Override
    public Response weather(String iata, String radiusString) {

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
        return Response.status(Response.Status.OK).entity(retrieveValue)
                .build();
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
        requestFrequencyMap.put(airportData,
                requestFrequencyMap.getOrDefault(airportData, 0) + 1);
        radiusFreqMap.put(radius, radiusFreqMap.getOrDefault(radius, 0));
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode
     *            as a string
     * @return airport data or null if not found
     */
    // public static AirportData findAirportData(String iataCode) {
    // return airportDataList.stream()
    // .filter(ap -> ap.getIata().equals(iataCode)).findFirst()
    // .orElse(null);
    // }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode
     *            as a string
     * @return airport data or null if not found
     */
    // public static int getAirportDataIdx(String iataCode) {
    // AirportData ad = findAirportData(iataCode);
    // return airportDataList.indexOf(ad);
    // }

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
        requestFrequencyMap.clear();

        airportDataRepository.addAirport("BOS", 42.364347, -71.005181);
        airportDataRepository.addAirport("EWR", 40.6925, -74.168667);
        airportDataRepository.addAirport("JFK", 40.639751, -73.778925);
        airportDataRepository.addAirport("LGA", 40.777245, -73.872608);
        airportDataRepository.addAirport("MMU", 40.79935, -74.4148747);
    }

}

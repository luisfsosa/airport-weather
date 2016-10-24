package com.crossover.trial.weather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class WeatherEndpointTest {

    private WeatherQueryEndpoint weatherQueryEndpoint = new RestWeatherQueryEndpoint();

    private WeatherCollectorEndpoint weatherCollectorEndpoint = new RestWeatherCollectorEndpoint();

    private Gson gSon = new Gson();

    private DataPoint dataPoint;

    @BeforeClass
    public static void loadAirports() {

    }

    @Before
    public void setUp() throws Exception {
        RestWeatherQueryEndpoint.init();
        dataPoint = new DataPoint.Builder().withCount(10).withFirst(10)
                .withSecond(20).withThird(30).withMean(22).build();
        weatherCollectorEndpoint.updateWeather("BOS", "wind",
                gSon.toJson(dataPoint));
        weatherQueryEndpoint.weather("BOS", "0").getEntity();
    }

    @Test
    public void testPing() throws Exception {
        String ping = weatherQueryEndpoint.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(1,
                pingResult.getAsJsonObject().get("datasize").getAsInt());
        assertEquals(5, pingResult.getAsJsonObject().get("iata_freq")
                .getAsJsonObject().entrySet().size());
    }

    @Test
    public void testGet() throws Exception {
        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) weatherQueryEndpoint
                .weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), dataPoint);
    }

    @Test
    public void testGetNearby() throws Exception {
        // check datasize response
        weatherCollectorEndpoint.updateWeather("JFK", "wind",
                gSon.toJson(dataPoint));
        dataPoint.setMean(40);
        weatherCollectorEndpoint.updateWeather("EWR", "wind",
                gSon.toJson(dataPoint));
        dataPoint.setMean(30);
        weatherCollectorEndpoint.updateWeather("LGA", "wind",
                gSon.toJson(dataPoint));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) weatherQueryEndpoint
                .weather("JFK", "200").getEntity();
        assertEquals(3, ais.size());
    }

    @Test
    public void testUpdate() throws Exception {
        DataPoint dataPoint = new DataPoint.Builder().withCount(10)
                .withFirst(10).withSecond(20).withThird(30).withMean(22)
                .build();
        weatherCollectorEndpoint.updateWeather("BOS", "wind",
                gSon.toJson(dataPoint));
        weatherQueryEndpoint.weather("BOS", "0").getEntity();

        String ping = weatherQueryEndpoint.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(1,
                pingResult.getAsJsonObject().get("datasize").getAsInt());

        DataPoint cloudCoverDp = new DataPoint.Builder().withCount(4)
                .withFirst(10).withSecond(60).withThird(100).withMean(50)
                .build();
        weatherCollectorEndpoint.updateWeather("BOS", "cloudcover",
                gSon.toJson(cloudCoverDp));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) weatherQueryEndpoint
                .weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), dataPoint);
        assertEquals(ais.get(0).getCloudCover(), cloudCoverDp);
    }

    @Test
    public void testAddAirport() throws Exception {
        AirportData airportDataCreated = (AirportData) weatherCollectorEndpoint
                .addAirport("EGLC", "51.505278", "0.055278").getEntity();

        assertNotNull(airportDataCreated);

        AirportData airportData = (AirportData) weatherCollectorEndpoint
                .getAirport("EGLC").getEntity();

        assertEquals(airportDataCreated, airportData);

        Set<AirportData> airportDataList = (Set<AirportData>) weatherCollectorEndpoint
                .getAirports().getEntity();
        assertEquals(6, airportDataList.size());
    }

    @Test
    public void testDeleteAirport() throws Exception {
        String retrieveValue = (String) weatherCollectorEndpoint
                .deleteAirport("EGLC").getEntity();

        assertEquals("ready", retrieveValue);

        AirportData airportData = (AirportData) weatherCollectorEndpoint
                .getAirport("EGLC").getEntity();

        assertNull(airportData);

        Set<AirportData> airportDataList = (Set<AirportData>) weatherCollectorEndpoint
                .getAirports().getEntity();
        assertEquals(5, airportDataList.size());
    }

}
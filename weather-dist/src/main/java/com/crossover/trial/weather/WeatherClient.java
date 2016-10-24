package com.crossover.trial.weather;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * A reference implementation for the weather client. Consumers of the REST API
 * can look at WeatherClient to understand API semantics. This existing client
 * populates the REST endpoint with dummy data useful for testing.
 *
 * @author code test administrator
 */
public class WeatherClient {

    /**
     * Log of the Class.
     */
    public static final Logger LOGGER = Logger
            .getLogger(WeatherClient.class.getName());

    /**
     * Base URL for The Server.
     */
    private static final String BASE_URI = "http://localhost:9090";

    /**
     * end point for read queries.
     */
    private WebTarget weatherQueryEndpoint;

    /**
     * end point to supply updates.
     */
    private WebTarget weatherCollectorEndpoint;

    /**
     * Default Constructor.
     */
    public WeatherClient() {
        Client client = ClientBuilder.newClient();
        weatherQueryEndpoint = client.target(BASE_URI + "/query");
        weatherCollectorEndpoint = client.target(BASE_URI + "/collect");
    }

    /**
     * A liveliness check for the collection endpoint.
     */
    public void pingCollect() {
        WebTarget path = weatherCollectorEndpoint.path("/ping");
        Response response = path.request().get();
        LOGGER.log(Level.INFO,
                "collect.ping: " + response.readEntity(String.class) + "\n");

    }

    /**
     * Retrieve the most up to date atmospheric information from the given
     * airport and other airports in the given radius.
     *
     * @param iata
     *            the three letter airport code
     */
    public void query(String iata) {
        WebTarget path = weatherQueryEndpoint.path("/weather/" + iata + "/0");
        Response response = path.request().get();
        LOGGER.log(Level.INFO,
                "query." + iata + ".0: " + response.readEntity(String.class));
    }

    /**
     * Retrieve health and status information for the the query api. Returns
     * information about how the number of datapoints currently held in memory,
     * the frequency of requests for each IATA code and the frequency of
     * requests for each radius.
     *
     */
    public void pingQuery() {
        WebTarget path = weatherQueryEndpoint.path("/ping");
        Response response = path.request().get();
        LOGGER.log(Level.INFO,
                "query.ping: " + response.readEntity(String.class));
    }

    /**
     * Populate the Weather.
     *
     * @param pointType
     *            pointType to set.
     * @param first
     *            first to set.
     * @param last
     *            last to set.
     * @param mean
     *            mean to set.
     * @param median
     *            median to set.
     * @param count
     *            count to set.
     */
    public void populate(String pointType, int first, int last, int mean,
            int median, int count) {
        WebTarget path = weatherCollectorEndpoint
                .path("/weather/BOS/" + pointType);
        DataPoint dataPoint = new DataPoint.Builder().withFirst(first)
                .withThird(last).withMean(mean).withSecond(median)
                .withCount(count).build();
        path.request().post(Entity.entity(dataPoint, "application/json"));
    }

    /**
     * Exit Operation.
     */
    public void exit() {
        try {
            weatherCollectorEndpoint.path("/exit").request().get();
        } catch (Throwable t) {
            // LOGGER.log(Level.SEVERE, null, t);
        }
    }

    /**
     * Retrieve all Airport from a file.
     *
     */
    public void loadAllAirports() {

        ClassLoader classLoader = AirportLoader.class.getClassLoader();
        File airportDataFile = new File(
                classLoader.getResource("airports.dat").getFile());

        if (airportDataFile == null || !airportDataFile.exists()
                || airportDataFile.length() == 0) {
            LOGGER.log(Level.SEVERE, airportDataFile + " is not a valid input");
            System.exit(1);
        }

        AirportLoader airportLoader = new AirportLoader();

        try {
            airportLoader.upload(new FileInputStream(airportDataFile));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, null, e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }

    }

    /**
     * Main Method.
     *
     * @param args
     */
    public static void main(String[] args) {
        WeatherClient wc = new WeatherClient();

        wc.loadAllAirports();

        wc.pingCollect();
        wc.populate("wind", 0, 10, 6, 4, 20);

        wc.query("BOS");
        wc.query("JFK");
        wc.query("EWR");
        wc.query("LGA");
        wc.query("MMU");

        wc.pingQuery();
        wc.exit();
        LOGGER.log(Level.INFO, "complete");
        System.exit(0);
    }
}

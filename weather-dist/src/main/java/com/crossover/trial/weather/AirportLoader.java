package com.crossover.trial.weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * A simple airport loader which reads a file from disk and sends entries to the
 * webservice
 *
 * TODO: Implement the Airport Loader
 * 
 * @author code test administrator
 */
public class AirportLoader {

    /**
     * Log of the Class.
     */
    public static final Logger LOGGER = Logger
            .getLogger(AirportLoader.class.getName());

    /**
     * Base URL for The Server.
     */
    private static final String BASE_URI = "http://localhost:9090";

    /**
     * end point for read queries
     */
    private WebTarget weatherQueryEndpoint;

    /**
     * end point to supply updates
     */
    private WebTarget weatherCollectorEndpoint;

    public AirportLoader() {
        Client client = ClientBuilder.newClient();
        weatherQueryEndpoint = client.target(BASE_URI + "/query");
        weatherCollectorEndpoint = client.target(BASE_URI + "/collect");
    }

    public void upload(InputStream airportDataStream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(airportDataStream));
        String line = null;
        String[] columns = null;
        String airport = null;
        WebTarget path = null;

        while ((line = reader.readLine()) != null) {
            columns = line.split(",");
            if (columns.length > 7) {
                airport = columns[4].replace("\"", "");
                path = weatherCollectorEndpoint.path("/airport/" + airport + "/"
                        + columns[6] + "/" + columns[7]);
                path.request().post(Entity.entity("", "application/json"));
            }
        }

    }

    @SuppressWarnings("unchecked")
    public Set<String> getAirports() {
        WebTarget path = weatherCollectorEndpoint.path("/airports");
        Response response = path.request().get();
        return response.readEntity(Set.class);
    }

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = null;
        File airportDataFile = null;

        if (args == null || args.length == 0) {
            classLoader = AirportLoader.class.getClassLoader();
            airportDataFile = new File(
                    classLoader.getResource("airports.dat").getFile());
        } else {
            airportDataFile = new File(args[0]);
        }

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

        Set<String> airportDataset = airportLoader.getAirports();

        for (String airportData : airportDataset) {
            LOGGER.log(Level.INFO, "Airpot: " + airportData);
        }

        System.exit(0);
    }
}

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

import com.crossover.trial.weather.util.CsvSplitter;

/**
 * A simple airport loader which reads a file from disk and sends entries to the
 * webservice
 *
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
     * end point to supply updates
     */
    private WebTarget weatherCollectorEndpoint;

    public AirportLoader() {
        Client client = ClientBuilder.newClient();
        weatherCollectorEndpoint = client.target(BASE_URI + "/collect");
    }

    public void upload(File airportDataFile) throws IOException {

        InputStream airportDataStream = new FileInputStream(airportDataFile);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(airportDataStream));
        String line = null;

        while ((line = reader.readLine()) != null) {
            processLine(line);
        }

    }

    private void processLine(String line) {

        WebTarget path = null;

        String[] columns = CsvSplitter.split(line);
        if (columns.length <= 7) {
            return;
        }

        String pathString = extractRequestPath(columns);
        path = weatherCollectorEndpoint.path(pathString);

        path.request().post(Entity.text(""));

    }

    private String extractIataCode(String[] columns) {
        return columns[4].replace("\"", "");
    }

    private String extractRequestPath(String[] columns) {
        String iataCode = extractIataCode(columns);
        String latitude = columns[6];
        String longitude = columns[7];
        return "/airport/" + iataCode + "/" + latitude + "/" + longitude;
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
            airportLoader.upload(airportDataFile);
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

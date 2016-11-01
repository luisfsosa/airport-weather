
package com.crossover.trial.weather.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.exception.AirportException;
import com.crossover.trial.weather.repository.AirportDataRepository;

/**
 * A Controller of the Airport Data.
 *
 * @author luisfsosa@gmail.com
 */
public class AirportController {

    /**
     * Log of the Class.
     */
    public static final Logger LOGGER = Logger
            .getLogger(AirportController.class.getName());

    // TODO: Inject AirportDataRepository.
    /**
     * all known airports.
     */
    protected static AirportDataRepository airportDataRepository = new AirportDataRepository();

    /**
     * Return a list of known airports.
     *
     * @return current list of known airports.
     */
    public Set<String> getAirports() {
        Set<String> retval = new HashSet<>();
        for (AirportData airportData : airportDataRepository.findAll()) {
            retval.add(airportData.getIata());
        }
        return retval;
    }

    /**
     * Retrieve airport data, including latitude and longitude for a particular
     * airport.
     *
     * @param iata
     *            the 3 letter airport code
     * @return current airport data for airport code.
     */
    public AirportData getAirport(String iata) {
        AirportData airportData = airportDataRepository.findOne(iata);
        return airportData;
    }

    /**
     * Add a new airport to the known airport list.
     *
     * @param iata
     *            the 3 letter airport code of the new airport
     * @param latString
     *            the airport's latitude in degrees as a string [-90, 90]
     * @param longString
     *            the airport's longitude in degrees as a string [-180, 180]
     * @return current airport data created.
     *
     * @throws AirportException
     *             Exception of Airport.
     */
    public AirportData addAirport(String iata, String latString,
            String longString) throws AirportException {

        Double latitude = null;
        Double longitude = null;

        validateAirportParameters(iata, latString, longString);

        try {
            latitude = Double.valueOf(latString);
            longitude = Double.valueOf(longString);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Wrong airport coordinates latString = "
                    + latString + ", longString = " + longString);
            throw new AirportException("Wrong airport coordinates latString = "
                    + latString + ", longString = " + longString);
        }

        validateLatitudAndLongitud(latitude, longitude);

        AirportData airportData = airportDataRepository.addAirport(iata,
                latitude, longitude);
        return airportData;
    }

    /**
     * Remove an airport from the known airport list.
     *
     * @param iata
     *            the 3 letter airport code
     *
     * @throws AirportException
     *             Exception of Airport.
     */
    public void deleteAirport(String iata) throws AirportException {

        if (iata == null || iata.length() != 3) {
            LOGGER.log(Level.SEVERE, "Bad parameters: iata = " + iata);
            throw new AirportException("Bad parameters: iata = " + iata);
        }
        airportDataRepository.delete(iata);
    }

    /**
     * Validate a new airport Parameters.
     *
     * @param iata
     *            the 3 letter airport code of the new airport
     * @param latString
     *            the airport's latitude in degrees as a string [-90, 90]
     * @param longString
     *            the airport's longitude in degrees as a string [-180, 180]
     *
     * @throws AirportException
     *             Exception of Airport.
     */
    private void validateAirportParameters(final String iata,
            final String latString, final String longString)
            throws AirportException {

        if (iata == null || iata.length() != 3 || latString == null
                || longString == null) {
            LOGGER.log(Level.SEVERE,
                    "Bad parameters: iata = " + iata + ", latString = "
                            + latString + ", longString = " + longString);
            throw new AirportException(
                    "Bad parameters: iata = " + iata + ", latString = "
                            + latString + ", longString = " + longString);
        }

    }

    /**
     * Validate latitude and longitude of an Airport.
     *
     * @param latitude
     *            the airport's latitude in degrees.
     * @param longitude
     *            the airport's longitude in degrees.
     *
     * @throws AirportException
     *             Exception of Airport.
     */
    private void validateLatitudAndLongitud(final Double latitude,
            final Double longitude) throws AirportException {

        if (latitude < -90 || latitude > 90 || longitude < -180
                || longitude > 180) {
            LOGGER.log(Level.SEVERE, "Wrong airport coordinates latitude = "
                    + latitude + ", longitude = " + longitude);
            throw new AirportException("Wrong airport coordinates latitude = "
                    + latitude + ", longitude = " + longitude);
        }

    }

}
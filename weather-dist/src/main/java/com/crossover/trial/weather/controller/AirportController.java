
package com.crossover.trial.weather.controller;

import static com.crossover.trial.weather.controller.WeatherQueryController.airportDataRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.exception.AirportException;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport
 * weather collection sites via secure VPN.
 *
 * @author code test administrator
 */
public class AirportController {

    /**
     * Log of the Class.
     */
    public static final Logger LOGGER = Logger
            .getLogger(AirportController.class.getName());

    /*
     * (non-Javadoc)
     *
     * @see com.crossover.trial.weather.WeatherCollectorEndpoint#getAirports()
     */
    public Set<String> getAirports() {
        Set<String> retval = new HashSet<>();
        for (AirportData airportData : airportDataRepository.findAll()) {
            retval.add(airportData.getIata());
        }
        return retval;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.crossover.trial.weather.WeatherCollectorEndpoint#getAirport(java.lang
     * .String)
     */
    public AirportData getAirport(String iata) {
        AirportData airportData = airportDataRepository.findOne(iata);
        return airportData;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.crossover.trial.weather.WeatherCollectorEndpoint#addAirport(java.lang
     * .String, java.lang.String, java.lang.String)
     */
    public AirportData addAirport(String iata, String latString,
            String longString) throws AirportException {

        Double latitude = null;
        Double longitude = null;

        if (iata == null || iata.length() != 3 || latString == null
                || longString == null) {

            LOGGER.log(Level.SEVERE,
                    "Bad parameters: iata = " + iata + ", latString = "
                            + latString + ", longString = " + longString);

            throw new AirportException(
                    "Bad parameters: iata = " + iata + ", latString = "
                            + latString + ", longString = " + longString);
        }

        try {
            latitude = Double.valueOf(latString);
            longitude = Double.valueOf(longString);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Wrong airport coordinates latString = "
                    + latString + ", longString = " + longString);
            throw new AirportException(e);
        }

        if (latitude < -90 || latitude > 90 || longitude < -180
                || longitude > 180) {
            LOGGER.log(Level.SEVERE, "Wrong airport coordinates latString = "
                    + latString + ", longString = " + longString);
            throw new AirportException("Wrong airport coordinates latString = "
                    + latString + ", longString = " + longString);
        }

        AirportData airportData = airportDataRepository.addAirport(iata,
                latitude, longitude);
        return airportData;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.crossover.trial.weather.WeatherCollectorEndpoint#deleteAirport(java.
     * lang.String)
     */
    public void deleteAirport(String iata) throws AirportException {

        if (iata == null || iata.length() != 3) {
            LOGGER.log(Level.SEVERE, "Bad parameters: iata = " + iata);
            throw new AirportException("Bad parameters: iata = " + iata);
        }
        airportDataRepository.delete(iata);
    }

}
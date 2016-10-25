package com.crossover.trial.weather.service.impl;

import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.controller.AirportController;
import com.crossover.trial.weather.controller.WeatherCollectorController;
import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.exception.AirportException;
import com.crossover.trial.weather.exception.WeatherException;
import com.crossover.trial.weather.service.WeatherCollectorEndpoint;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport
 * weather collection sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {

    /**
     * Log of the Class.
     */
    public static final Logger LOGGER = Logger
            .getLogger(RestWeatherCollectorEndpoint.class.getName());

    // TODO: Inject WeatherCollectorController.
    /**
     * A Controller of the WeatherCollector API.
     */
    private WeatherCollectorController weatherCollectorController = new WeatherCollectorController();

    // TODO: Inject AirportController.
    /**
     * A Controller of the Airport Data.
     */
    private AirportController airportController = new AirportController();

    /*
     * (non-Javadoc)
     *
     * @see com.crossover.trial.weather.WeatherCollectorEndpoint#ping()
     */
    @Override
    public final Response ping() {
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.crossover.trial.weather.WeatherCollectorEndpoint#updateWeather(java.
     * lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Response updateWeather(String iataCode, String pointType,
            final String datapointJson) {

        try {
            weatherCollectorController.updateWeather(iataCode, pointType,
                    datapointJson);
        } catch (WeatherException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e)
                    .build();
        }

        return Response.status(Response.Status.OK).build();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.crossover.trial.weather.WeatherCollectorEndpoint#getAirports()
     */
    @Override
    public Response getAirports() {
        Set<String> retval = airportController.getAirports();
        return Response.status(Response.Status.OK).entity(retval).build();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.crossover.trial.weather.WeatherCollectorEndpoint#getAirport(java.lang
     * .String)
     */
    @Override
    public Response getAirport(String iata) {
        AirportData airportData = airportController.getAirport(iata);
        return Response.status(Response.Status.OK).entity(airportData).build();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.crossover.trial.weather.WeatherCollectorEndpoint#addAirport(java.lang
     * .String, java.lang.String, java.lang.String)
     */
    @Override
    public Response addAirport(String iata, String latString,
            String longString) {

        AirportData airportData = null;
        try {
            airportData = airportController.addAirport(iata, latString,
                    longString);
        } catch (AirportException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e)
                    .build();
        }

        return Response.status(Response.Status.OK).entity(airportData).build();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.crossover.trial.weather.WeatherCollectorEndpoint#deleteAirport(java.
     * lang.String)
     */
    @Override
    public Response deleteAirport(String iata) {

        try {
            airportController.deleteAirport(iata);
        } catch (AirportException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e)
                    .build();
        }

        return Response.status(Response.Status.OK).entity("ready").build();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.crossover.trial.weather.WeatherCollectorEndpoint#exit()
     */
    @Override
    public Response exit() {
        weatherCollectorController.exit();
        return Response.noContent().build();
    }

}

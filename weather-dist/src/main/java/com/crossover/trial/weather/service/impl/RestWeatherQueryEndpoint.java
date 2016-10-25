package com.crossover.trial.weather.service.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.controller.WeatherQueryController;
import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.service.WeatherQueryEndpoint;

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


/** all known airports */
    private WeatherQueryController weatherQueryController = new WeatherQueryController();

    /**
     * Retrieve service health including total size of valid data points and
     * request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    public String ping() {
     return weatherQueryController.ping();
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

          List<AtmosphericInformation> retrieveValue =weatherQueryController.weather(iata,radiusString);

         return Response.status(Response.Status.OK).entity(retrieveValue)
                .build();
    }
}

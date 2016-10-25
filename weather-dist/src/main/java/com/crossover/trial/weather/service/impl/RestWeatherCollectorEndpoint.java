package com.crossover.trial.weather.service.impl;

import static com.crossover.trial.weather.service.impl.RestWeatherQueryEndpoint.airportDataRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.domain.DataPoint;
import com.crossover.trial.weather.domain.DataPointType;
import com.crossover.trial.weather.exception.WeatherException;
import com.crossover.trial.weather.service.WeatherCollectorEndpoint;
import com.google.gson.Gson;

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

    /**
     * shared gson json to object factory.
     */
    public static final Gson GSON = new Gson();

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

        if (iataCode == null || iataCode.length() != 3) {
            LOGGER.log(Level.SEVERE, "Bad parameters: iata = " + iataCode);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        AirportData airportData = airportDataRepository.findOne(iataCode);
        
        try {

            if (airportData == null
                    || airportData.getAtmosphericInformation() == null) {
                LOGGER.log(Level.SEVERE, "Bad parameters: iata = " + iataCode);
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            updateAtmosphericInformation(
                    airportData.getAtmosphericInformation(), pointType,
                    GSON.fromJson(datapointJson, DataPoint.class));

        } catch (WeatherException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return Response.status(Response.Status.BAD_REQUEST).build();
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
        Set<String> retval = new HashSet<>();
        for (AirportData airportData : airportDataRepository.findAll()) {
            retval.add(airportData.getIata());
        }
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
        AirportData airportData = airportDataRepository.findOne(iata);
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

        Double latitude = null;
        Double longitude = null;

        if (iata == null || iata.length() != 3 || latString == null
                || longString == null) {

            LOGGER.log(Level.SEVERE,
                    "Bad parameters: iata = " + iata + ", latString = "
                            + latString + ", longString = " + longString);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            latitude = Double.valueOf(latString);
            longitude = Double.valueOf(longString);
        } catch (NumberFormatException ex) {
            LOGGER.log(Level.SEVERE, "Wrong airport coordinates latString = "
                    + latString + ", longString = " + longString);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (latitude < -90 || latitude > 90 || longitude < -180
                || longitude > 180) {
            LOGGER.log(Level.SEVERE, "Wrong airport coordinates latString = "
                    + latString + ", longString = " + longString);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        AirportData airportData = airportDataRepository.addAirport(iata,
                latitude, longitude);
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

        if (iata == null || iata.length() != 3) {
            LOGGER.log(Level.SEVERE, "Bad parameters: iata = " + iata);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        airportDataRepository.delete(iata);
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.crossover.trial.weather.WeatherCollectorEndpoint#exit()
     */
    @Override
    public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }
    //
    // Internal support methods
    //

    /**
     * update atmospheric information with the given data point for the given
     * point type.
     *
     * @param ai
     *            the atmospheric information object to update
     * @param pointType
     *            the data point type as a string
     * @param dp
     *            the actual data point
     *
     * @throws WeatherException
     *             Exception of Weather.
     */
    public void updateAtmosphericInformation(
            final AtmosphericInformation atmosphericInfo,
            final String pointType, final DataPoint dataPoint)
            throws WeatherException {
        final DataPointType dptype = DataPointType
                .valueOf(pointType.toUpperCase());

        switch (dptype) {
        case WIND:
            if (dataPoint.getMean() >= 0) {
                atmosphericInfo.setWind(dataPoint);
                atmosphericInfo.setLastUpdateTime(System.currentTimeMillis());
            }
            break;

        case TEMPERATURE:
            if (dataPoint.getMean() >= -50 && dataPoint.getMean() < 100) {
                atmosphericInfo.setTemperature(dataPoint);
                atmosphericInfo.setLastUpdateTime(System.currentTimeMillis());
            }
            break;

        case HUMIDITY:
            if (dataPoint.getMean() >= 0 && dataPoint.getMean() < 100) {
                atmosphericInfo.setHumidity(dataPoint);
                atmosphericInfo.setLastUpdateTime(System.currentTimeMillis());
            }
            break;

        case PRESSURE:
            if (dataPoint.getMean() >= 650 && dataPoint.getMean() < 800) {
                atmosphericInfo.setPressure(dataPoint);
                atmosphericInfo.setLastUpdateTime(System.currentTimeMillis());
            }
            break;

        case CLOUDCOVER:
            if (dataPoint.getMean() >= 0 && dataPoint.getMean() < 100) {
                atmosphericInfo.setCloudCover(dataPoint);
                atmosphericInfo.setLastUpdateTime(System.currentTimeMillis());
            }
            break;

        case PRECIPITATION:
            if (dataPoint.getMean() >= 0 && dataPoint.getMean() < 100) {
                atmosphericInfo.setPrecipitation(dataPoint);
                atmosphericInfo.setLastUpdateTime(System.currentTimeMillis());
            }
            break;

        default:
            throw new IllegalStateException("couldn't update atmospheric data");
        }

    }
}

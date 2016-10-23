package com.crossover.trial.weather;

import static com.crossover.trial.weather.RestWeatherQueryEndpoint.airportData;
import static com.crossover.trial.weather.RestWeatherQueryEndpoint.atmosphericInformation;
import static com.crossover.trial.weather.RestWeatherQueryEndpoint.findAirportData;
import static com.crossover.trial.weather.RestWeatherQueryEndpoint.getAirportDataIdx;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

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
    public Response updateWeather(@PathParam("iata") final String iataCode,
            @PathParam("pointType") final String pointType,
            final String datapointJson) {
        try {
            addDataPoint(iataCode, pointType,
                    GSON.fromJson(datapointJson, DataPoint.class));
        } catch (WeatherException e) {
            e.printStackTrace();
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
        for (AirportData airportData : airportData) {
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
    public Response getAirport(@PathParam("iata") final String iata) {
        AirportData airportData = findAirportData(iata);
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
    public Response addAirport(@PathParam("iata") final String iata,
            @PathParam("lat") final String latString,
            @PathParam("long") final String longString) {
        addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
        return Response.status(Response.Status.OK).build();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.crossover.trial.weather.WeatherCollectorEndpoint#deleteAirport(java.
     * lang.String)
     */
    @Override
    public Response deleteAirport(@PathParam("iata") final String iata) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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
     * Update the airports weather data with the collected data.
     *
     * @param iataCode
     *            the 3 letter IATA code
     * @param pointType
     *            the point type {@link DataPointType}
     * @param dp
     *            a datapoint object holding pointType data
     *
     * @throws WeatherException
     *             if the update can not be completed
     */
    public void addDataPoint(final String iataCode, final String pointType,
            final DataPoint dataPoint) throws WeatherException {
        int airportDataIdx = getAirportDataIdx(iataCode);
        AtmosphericInformation atmosphericInfo = atmosphericInformation
                .get(airportDataIdx);
        updateAtmosphericInformation(atmosphericInfo, pointType, dataPoint);
    }

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

        case HUMIDTY:
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

    /**
     * Add a new known airport to our list.
     *
     * @param iataCode
     *            3 letter code
     * @param latitude
     *            in degrees
     * @param longitude
     *            in degrees
     *
     * @return the added airport
     */
    public static AirportData addAirport(final String iataCode,
            final double latitude, final double longitude) {
        AirportData airportD = new AirportData();
        airportData.add(airportD);

        AtmosphericInformation atmosphericInfo = new AtmosphericInformation();
        atmosphericInformation.add(atmosphericInfo);
        airportD.setIata(iataCode);
        airportD.setLatitude(latitude);
        airportD.setLatitude(longitude);
        return airportD;
    }
}

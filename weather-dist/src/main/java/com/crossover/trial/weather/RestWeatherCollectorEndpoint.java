package com.crossover.trial.weather;

import static com.crossover.trial.weather.RestWeatherQueryEndpoint.airportDataList;
import static com.crossover.trial.weather.RestWeatherQueryEndpoint.atmosphericInformationList;
import static com.crossover.trial.weather.RestWeatherQueryEndpoint.findAirportData;
import static com.crossover.trial.weather.RestWeatherQueryEndpoint.getAirportDataIdx;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;
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
    public Response updateWeather(String iataCode, String pointType,
            final String datapointJson) {
        try {
            addDataPoint(iataCode, pointType,
                    GSON.fromJson(datapointJson, DataPoint.class));
        } catch (WeatherException e) {
            LOGGER.log(Level.SEVERE, null, e);
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
        for (AirportData airportData : airportDataList) {
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
    public Response addAirport(String iata, String latString,
            String longString) {
        AirportData airportData = addAirport(iata, Double.valueOf(latString),
                Double.valueOf(longString));
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
        AirportData airportData = findAirportData(iata);
        airportDataList.remove(airportData);
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
        AtmosphericInformation atmosphericInfo = atmosphericInformationList
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
        AirportData airportData = new AirportData();
        airportDataList.add(airportData);

        AtmosphericInformation atmosphericInfo = new AtmosphericInformation();
        atmosphericInformationList.add(atmosphericInfo);
        airportData.setIata(iataCode);
        airportData.setLatitude(latitude);
        airportData.setLatitude(longitude);
        return airportData;
    }
}

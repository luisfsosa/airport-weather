package com.crossover.trial.weather.controller;

import static com.crossover.trial.weather.controller.WeatherQueryController.airportDataRepository;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.domain.DataPoint;
import com.crossover.trial.weather.domain.DataPointType;
import com.crossover.trial.weather.exception.WeatherException;
import com.google.gson.Gson;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport
 * weather collection sites via secure VPN.
 *
 * @author code test administrator
 */
public class WeatherCollectorController {

    /**
     * Log of the Class.
     */
    public static final Logger LOGGER = Logger
            .getLogger(WeatherCollectorController.class.getName());

    /**
     * shared gson json to object factory.
     */
    public static final Gson GSON = new Gson();

    /*
     * (non-Javadoc)
     *
     * @see
     * com.crossover.trial.weather.WeatherCollectorEndpoint#updateWeather(java.
     * lang.String, java.lang.String, java.lang.String)
     */
    public void updateWeather(String iataCode, String pointType,
            final String datapointJson) throws WeatherException {

        if (iataCode == null || iataCode.length() != 3) {
            LOGGER.log(Level.SEVERE, "Bad parameters: iata = " + iataCode);
            throw new WeatherException("Bad parameters: iata = " + iataCode);
        }

        AirportData airportData = airportDataRepository.findOne(iataCode);

        try {

            if (airportData == null
                    || airportData.getAtmosphericInformation() == null) {
                LOGGER.log(Level.SEVERE, "Bad parameters: iata = " + iataCode);
                throw new WeatherException(
                        "Bad parameters: iata = " + iataCode);
            }

            updateAtmosphericInformation(
                    airportData.getAtmosphericInformation(), pointType,
                    GSON.fromJson(datapointJson, DataPoint.class));

        } catch (WeatherException e) {
            LOGGER.log(Level.SEVERE, null, e);
            throw new WeatherException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.crossover.trial.weather.WeatherCollectorEndpoint#exit()
     */
    public void exit() {
        System.exit(0);
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

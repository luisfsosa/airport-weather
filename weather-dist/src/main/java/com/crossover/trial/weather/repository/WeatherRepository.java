package com.crossover.trial.weather.repository;

import java.util.HashMap;
import java.util.Map;

import com.crossover.trial.weather.domain.AirportData;

/**
 * A Repository of the Weather.
 *
 * @author luisfsosa@gmail.com
 */
public class WeatherRepository {

    /**
     * Internal performance counter to better understand most requested
     * information.
     */
    private final Map<AirportData, Integer> requestFrequencyMap = new HashMap<AirportData, Integer>();

    /**
     * All Radius Frequency.
     */
    private final Map<Double, Integer> radiusFreqMap = new HashMap<Double, Integer>();

    /**
     * Returns all Request Frequency.
     * 
     * @return all Request Frequency.
     */
    public Map<AirportData, Integer> findAllRequestFrequency() {
        return requestFrequencyMap;
    }

    /**
     * Returns all Radius Frequency.
     * 
     * @return all Radius Frequency.
     */
    public Map<Double, Integer> findAllRadiusFreqMap() {
        return radiusFreqMap;
    }

}

package com.crossover.trial.weather.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.NotImplementedException;

import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.domain.AtmosphericInformation;

/**
 * A Repository of the Airport Data.
 *
 * @author luisfsosa@gmail.com
 */
public class AirportDataRepository implements Repository<AirportData, String> {

    /**
     * all known airports.
     */
    private final ConcurrentHashMap<String, AirportData> airportDataMap = new ConcurrentHashMap<>();

    /*
     * (non-Javadoc)
     * 
     * @see com.crossover.trial.weather.repository.Repository#findAll()
     */
    @Override
    public List<AirportData> findAll() {
        return new ArrayList<AirportData>(airportDataMap.values());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.crossover.trial.weather.repository.Repository#delete(java.io.
     * Serializable)
     */
    @Override
    public void delete(String id) {
        airportDataMap.remove(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.crossover.trial.weather.repository.Repository#delete(java.lang.
     * Object)
     */
    @Override
    public void delete(AirportData entity) {
        airportDataMap.remove(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.crossover.trial.weather.repository.Repository#findOne(java.io.
     * Serializable)
     */
    @Override
    public AirportData findOne(String id) {
        return airportDataMap.get(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.crossover.trial.weather.repository.Repository#save(java.lang.
     * Iterable)
     */
    @Override
    public Iterable<AirportData> save(Iterable<AirportData> entities) {
        throw new NotImplementedException("Method not Implemented");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.crossover.trial.weather.repository.Repository#save(java.lang.Object)
     */
    @Override
    public AirportData save(AirportData entity) {
        return airportDataMap.put(entity.getIata(), entity);
    }

    /**
     * Add a new Airport.
     * 
     * @param iataCode
     *            the 3 letter airport code of the new airport
     * @param latString
     *            the airport's latitude in degrees as a string [-90, 90]
     * @param longString
     *            the airport's longitude in degrees as a string [-180, 180]
     * @return a Airport Created.
     */
    public AirportData addAirport(String iataCode, Double latitude,
            Double longitude) {
        AirportData airportData = new AirportData.Builder().withIata(iataCode)
                .withLatitude(latitude).withLongitude(longitude)
                .withAtmosphericInformation(new AtmosphericInformation())
                .build();
        save(airportData);
        return airportData;
    }

}

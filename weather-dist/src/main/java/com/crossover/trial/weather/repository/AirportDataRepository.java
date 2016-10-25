package com.crossover.trial.weather.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.NotImplementedException;

import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.domain.AtmosphericInformation;

public class AirportDataRepository implements Repository<AirportData, String> {

    private final ConcurrentHashMap<String, AirportData> airportDataMap = new ConcurrentHashMap<>();

    @Override
    public List<AirportData> findAll() {
        return new ArrayList<AirportData>(airportDataMap.values());
    }

    @Override
    public void delete(String id) {
        airportDataMap.remove(id);
    }

    @Override
    public void delete(AirportData entity) {
        airportDataMap.remove(entity);
    }

    @Override
    public AirportData findOne(String id) {
        return airportDataMap.get(id);
    }

    @Override
    public Iterable<AirportData> save(Iterable<AirportData> entities) {
        throw new NotImplementedException("Method not Implemented");
    }

    @Override
    public AirportData save(AirportData entity) {
        return airportDataMap.put(entity.getIata(), entity);
    }

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

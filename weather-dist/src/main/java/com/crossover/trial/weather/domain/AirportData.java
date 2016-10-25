package com.crossover.trial.weather.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportData {

    /**
     * Name of the airport
     */
    private String name;

    /**
     * Main city served by airport. May be spelled differently from name.
     */
    private String city;

    /**
     * Country or territory where airport is located.
     */
    private String country;

    /**
     * the three letter FAA code or IATA code.
     */
    private String iata;

    /**
     * 4-letter ICAO code
     */
    private String icao;

    /**
     * latitude value in degrees.
     */
    private double latitude;

    /**
     * longitude value in degrees.
     */
    private double longitude;

    /**
     * altitude value in feets
     */
    private long altitude;

    /**
     * Hours offset from UTC.
     */
    private double timeZone;

    /**
     * Day light saving time of airport
     */
    private DST zone = DST.U;

    /**
     * 
     */
    private AtmosphericInformation atmosphericInformation;

    /**
     * private constructor, use the builder to create this object.
     */
    @SuppressWarnings("unused")
    private AirportData() {
    }

    /**
     * A valid Constructor for the class.
     *
     * @param builder
     *            Builder of this Class
     */
    public AirportData(Builder builder) {
        this.name = builder.name;
        this.city = builder.city;
        this.country = builder.country;
        this.iata = builder.iata;
        this.icao = builder.icao;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.altitude = builder.altitude;
        this.timeZone = builder.timeZone;
        this.zone = builder.zone;
        this.atmosphericInformation = builder.atmosphericInformation;
    }

    /**
     * @return current name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return current city.
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     *            city to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return current country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     *            country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return current iata.
     */
    public String getIata() {
        return iata;
    }

    /**
     * @param iata
     *            iata to set.
     */
    public void setIata(final String iata) {
        this.iata = iata;
    }

    /**
     * @return current icao.
     */
    public String getIcao() {
        return icao;
    }

    /**
     * @param icao
     *            icao to set.
     */
    public void setIcao(String icao) {
        this.icao = icao;
    }

    /**
     * @return current latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     *            latitude to set.
     */
    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return current longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     *            longitude to set.
     */
    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return current altitude.
     */
    public long getAltitude() {
        return altitude;
    }

    /**
     * @param altitude
     *            altitude to set.
     */
    public void setAltitude(long altitude) {
        this.altitude = altitude;
    }

    /**
     * @return current timeZone.
     */
    public double getTimeZone() {
        return timeZone;
    }

    /**
     * @param timeZone
     *            timeZone to set.
     */
    public void setTimeZone(double timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * @return current zone.
     */
    public DST getZone() {
        return zone;
    }

    /**
     * @param zone
     *            zone to set.
     */
    public void setZone(DST zone) {
        this.zone = zone;
    }

    /**
     * @return
     */
    public AtmosphericInformation getAtmosphericInformation() {
        return atmosphericInformation;
    }

    /**
     * @param atmosphericInformation
     */
    public void setAtmosphericInformation(
            AtmosphericInformation atmosphericInformation) {
        this.atmosphericInformation = atmosphericInformation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {

        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        AirportData airportData = (AirportData) obj;
        return new EqualsBuilder().append(this.getIata(), airportData.getIata())
                .isEquals();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getIata()).hashCode();
    }

    /**
     * A Builder for Air Port Data class.
     *
     * @author code test administrator
     */
    public static class Builder {

        /**
         * Name of the airport.
         */
        private String name;

        /**
         * Main city served by airport. May be spelled differently from name.
         */
        private String city;

        /**
         * Country or territory where airport is located.
         */
        private String country;

        /**
         * the three letter IATA code.
         */
        private String iata;

        /**
         * 4-letter ICAO code.
         */
        private String icao;

        /**
         * latitude value in degrees.
         */
        private double latitude;

        /**
         * longitude value in degrees.
         */
        private double longitude;

        /**
         * altitude value in feets.
         */
        private long altitude;

        /**
         * Hours offset from UTC.
         */
        private double timeZone;

        /**
         * Day light saving time of airport.
         */
        private DST zone;

        /**
         * 
         */
        private AtmosphericInformation atmosphericInformation;

        /**
         * @param name
         *            name to set.
         * @return The Builder of AirportData Class.
         */
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * @param city
         *            city to set.
         * @return The Builder of AirportData Class.
         */
        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        /**
         * @param country
         *            country to set.
         * @return The Builder of AirportData Class.
         */
        public Builder withCountry(String country) {
            this.country = country;
            return this;
        }

        /**
         * @param iata
         *            iata to set.
         * @return The Builder of AirportData Class.
         */
        public Builder withIata(String iata) {
            this.iata = iata;
            return this;
        }

        /**
         * @param icao
         *            icao to set.
         * @return The Builder of AirportData Class.
         */
        public Builder withIcao(String icao) {
            this.icao = icao;
            return this;
        }

        /**
         * @param latitude
         *            latitude to set.
         * @return The Builder of AirportData Class.
         */
        public Builder withLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        /**
         * @param longitude
         *            longitude to set.
         * @return The Builder of AirportData Class.
         */
        public Builder withLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        /**
         * @param altitude
         *            altitude to set.
         * @return The Builder of AirportData Class.
         */
        public Builder withAltitude(long altitude) {
            this.altitude = altitude;
            return this;
        }

        /**
         * @param timeZone
         *            timeZone to set.
         * @return The Builder of AirportData Class.
         */
        public Builder withTimeZone(double timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        /**
         * @param zone
         *            zone to set.
         * @return The Builder of AirportData Class.
         */
        public Builder withZone(DST zone) {
            this.zone = zone;
            return this;
        }

        /**
         * @param atmosphericInformation
         *            atmosphericInformation to set.
         * @return The Builder of AirportData Class.
         */
        public Builder withAtmosphericInformation(
                AtmosphericInformation atmosphericInformation) {
            this.atmosphericInformation = atmosphericInformation;
            return this;
        }

        /**
         * @return The AirportData Built.
         */
        public AirportData build() {
            return new AirportData(this);
        }
    }
}

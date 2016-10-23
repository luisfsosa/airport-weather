package com.crossover.trial.weather;

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
     * the three letter IATA code.
     */
    private String iata;

    /**
     * latitude value in degrees.
     */
    private double latitude;

    /**
     * longitude value in degrees.
     */
    private double longitude;

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
}

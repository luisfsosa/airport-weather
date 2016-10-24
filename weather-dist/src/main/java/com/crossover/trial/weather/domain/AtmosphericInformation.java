package com.crossover.trial.weather.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Encapsulates sensor information for a particular location.
 *
 * @author code test administrator
 */
public class AtmosphericInformation {

    /**
     * temperature in degrees celsius.
     */
    private DataPoint temperature;

    /**
     * wind speed in km/h.
     */
    private DataPoint wind;

    /**
     * humidity in percent.
     */
    private DataPoint humidity;

    /**
     * precipitation in cm.
     */
    private DataPoint precipitation;

    /**
     * pressure in mmHg.
     */
    private DataPoint pressure;

    /**
     * cloud cover percent from 0 - 100 (integer).
     */
    private DataPoint cloudCover;

    /**
     * the last time this data was updated, in milliseconds since UTC epoch.
     */
    private long lastUpdateTime;

    /**
     * Default Constructor.
     */
    public AtmosphericInformation() {
    }

    /**
     * Constructor with all attributes.
     *
     * @param temperature
     *            temperature to set.
     * @param wind
     *            wind to set.
     * @param humidity
     *            wind to set.
     * @param percipitation
     *            wind to set.
     * @param pressure
     *            wind to set.
     * @param cloudCover
     *            wind to set.
     */
    protected AtmosphericInformation(final DataPoint temperature,
            final DataPoint wind, final DataPoint humidity,
            final DataPoint percipitation, final DataPoint pressure,
            final DataPoint cloudCover) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.precipitation = percipitation;
        this.pressure = pressure;
        this.cloudCover = cloudCover;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * @return current temperature.
     */
    public DataPoint getTemperature() {
        return temperature;
    }

    /**
     * @param temperature
     *            temperature iata to set.
     */
    public void setTemperature(final DataPoint temperature) {
        this.temperature = temperature;
    }

    /**
     * @return current wind.
     */
    public DataPoint getWind() {
        return wind;
    }

    /**
     * @param wind
     *            wind to set.
     */
    public void setWind(DataPoint wind) {
        this.wind = wind;
    }

    /**
     * @return current humidity.
     */
    public DataPoint getHumidity() {
        return humidity;
    }

    /**
     * @param humidity
     *            humidity to set.
     */
    public void setHumidity(DataPoint humidity) {
        this.humidity = humidity;
    }

    /**
     * @return current precipitation.
     */
    public DataPoint getPrecipitation() {
        return precipitation;
    }

    /**
     * @param precipitation
     *            precipitation to set.
     */
    public void setPrecipitation(DataPoint precipitation) {
        this.precipitation = precipitation;
    }

    /**
     * @return current pressure.
     */
    public DataPoint getPressure() {
        return pressure;
    }

    /**
     * @param pressure
     *            pressure to set.
     */
    public void setPressure(DataPoint pressure) {
        this.pressure = pressure;
    }

    /**
     * @return current cloudCover.
     */
    public DataPoint getCloudCover() {
        return cloudCover;
    }

    /**
     * @param cloudCover
     *            cloudCover to set.
     */
    public void setCloudCover(DataPoint cloudCover) {
        this.cloudCover = cloudCover;
    }

    /**
     * @return current lastUpdateTime.
     */
    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    /**
     * @param lastUpdateTime
     *            lastUpdateTime to set.
     */
    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
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
        AtmosphericInformation atmosphericInformation = (AtmosphericInformation) obj;
        return new EqualsBuilder().append(this.getLastUpdateTime(),
                atmosphericInformation.getLastUpdateTime()).isEquals();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getLastUpdateTime())
                .hashCode();
    }
}

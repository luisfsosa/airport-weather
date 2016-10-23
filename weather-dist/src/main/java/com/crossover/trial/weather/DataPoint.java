package com.crossover.trial.weather;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A collected point, including some information about the range of collected
 * values.
 *
 * @author code test administrator
 */
public class DataPoint {

    /**
     * The mean of the DataPoint.
     */
    private double mean;

    /**
     * The first of the DataPoint.
     */
    private int first;

    /**
     * The second of the DataPoint.
     */
    private int second;

    /**
     * The third of the DataPoint.
     */
    private int third;

    /**
     * The count of the DataPoint.
     */
    private int count;

    /**
     * A valid Constructor for the class.
     *
     * @param builder
     *            Builder of this Class
     */
    protected DataPoint(final Builder builder) {
        this.first = builder.first;
        this.second = builder.second;
        this.mean = builder.mean;
        this.third = builder.third;
        this.count = builder.count;
    }

    /** private constructor, use the builder to create this object. */
    protected DataPoint() {
    }

    /**
     * the mean of the observations.
     *
     * @return current the mean
     */
    public double getMean() {
        return mean;
    }

    /**
     * @param mean
     *            mean to set.
     */
    protected void setMean(final double mean) {
        this.mean = mean;
    }

    /**
     * 1st quartile -- useful as a lower bound.
     *
     * @return current 1st quartile
     */
    public int getFirst() {
        return first;
    }

    /**
     * @param first
     *            mean to set.
     */
    protected void setFirst(final int first) {
        this.first = first;
    }

    /**
     * 2nd quartile -- median value.
     *
     * @return current 2nd quartile
     */
    public int getSecond() {
        return second;
    }

    /**
     * @param second
     *            second to set.
     */
    protected void setSecond(final int second) {
        this.second = second;
    }

    /**
     * 3rd quartile value -- less noisy upper value.
     *
     * @return current 3rd quartile value
     */
    public int getThird() {
        return third;
    }

    /**
     * @param third
     *            third to set.
     */
    protected void setThird(final int third) {
        this.third = third;
    }

    /**
     * the total number of measurements.
     *
     * @return the total number of measurements
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count
     *            count to set.
     */
    protected void setCount(final int count) {
        this.count = count;
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
        DataPoint dataPoint = (DataPoint) obj;
        return new EqualsBuilder().append(this.getFirst(), dataPoint.getFirst())
                .append(this.getSecond(), dataPoint.getSecond())
                .append(this.getMean(), dataPoint.getMean())
                .append(this.getThird(), dataPoint.getThird())
                .append(this.getCount(), dataPoint.getCount()).isEquals();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getFirst())
                .append(this.getSecond()).append(this.getMean())
                .append(this.getThird()).append(this.getCount()).hashCode();
    }

    /**
     * A Builder for collected point class.
     *
     * @author code test administrator
     */
    public static class Builder {

        /**
         * The mean of the DataPoint.
         */
        private double mean;

        /**
         * The first of the DataPoint.
         */
        private int first;

        /**
         * The second of the DataPoint.
         */
        private int second;

        /**
         * The third of the DataPoint.
         */
        private int third;

        /**
         * The count of the DataPoint.
         */
        private int count;

        /**
         * @param mean
         *            mean to set.
         *
         * @return The Builder of DataPoint Class.
         */
        public Builder withMean(final int mean) {
            this.mean = mean;
            return this;
        }

        /**
         * @param first
         *            first to set.
         *
         * @return The Builder of DataPoint Class.
         */
        public Builder withFirst(final int first) {
            this.first = first;
            return this;
        }

        /**
         * @param second
         *            second to set.
         *
         * @return The Builder of DataPoint Class.
         */
        public Builder withSecond(final int second) {
            this.second = second;
            return this;
        }

        /**
         * @param third
         *            third to set.
         *
         * @return The Builder of DataPoint Class.
         */
        public Builder withThird(final int third) {
            this.third = third;
            return this;
        }

        /**
         * @param count
         *            count to set.
         *
         * @return The Builder of DataPoint Class.
         */
        public Builder withCount(final int count) {
            this.count = count;
            return this;
        }

        /**
         * @return The DataPoint Built.
         */
        public DataPoint build() {
            return new DataPoint(this);
        }
    }
}

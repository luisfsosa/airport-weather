package com.crossover.trial.weather.domain;

/**
 * The various DST of the an Airport.
 *
 * @author luisfsosa@gmail.com
 */
public enum DST {

    /**
     * Europe.
     */
    E("Europe"),

    /**
     * US/Canada.
     */
    A("US/Canada"),

    /**
     * South America.
     */
    S("South America"),

    /**
     * Australia.
     */
    O("Australia"),

    /**
     * New Zealand.
     */
    Z("New Zealand"),
    /**
     * None.
     */
    N("None"),

    /**
     * Unknown.
     */
    U("Unknown");

    /**
     * Name of DST.
     */
    private String name;

    /**
     * Default Constructor.
     *
     * @param name
     *            Name of DST
     */
    private DST(final String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.name;
    }
}
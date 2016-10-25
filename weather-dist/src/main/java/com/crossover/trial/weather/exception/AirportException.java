package com.crossover.trial.weather.exception;

/**
 * An internal exception marker.
 *
 * @author luisfsosa@gmail.com
 */
public class AirportException extends Exception {

    /**
     * Serial of the class.
     */
    private static final long serialVersionUID = -3717617059428945841L;

    public AirportException(String s) {
        super(s);
    }

    public AirportException(Exception e) {
        super(e);
    }
}

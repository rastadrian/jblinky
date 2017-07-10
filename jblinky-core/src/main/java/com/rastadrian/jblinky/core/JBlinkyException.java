package com.rastadrian.jblinky.core;

/**
 * Thrown if any illegal state is encountered on jBlinky.
 * Created on 6/29/17.
 *
 * @author Adrian Pena
 */
public class JBlinkyException extends RuntimeException {

    public JBlinkyException(String message) {
        super(message);
    }
}

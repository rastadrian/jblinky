package com.rastadrian.jblinky.core.probe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A Probe Status, this is returned when a Probe is verified.
 *
 * @author Adrian Pena
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    /**
     * The probe state after verification.
     */
    private State state;

    /**
     * An optional list of messages from the probe.
     */
    private String[] messages;
}

package com.rastadrian.jblinky.core.probe;

/**
 * A Probe status report.
 *
 * @author Adrian Pena
 */
public class Status {
    /**
     * The probe state after verification.
     */
    private State state;

    /**
     * An optional list of messages from the probe.
     */
    private String[] messages;

    public Status() {
    }

    public Status(State state, String[] messages) {
        this.state = state;
        this.messages = messages;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String[] getMessages() {
        return messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }
}

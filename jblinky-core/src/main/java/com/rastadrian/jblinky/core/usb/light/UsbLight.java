package com.rastadrian.jblinky.core.usb.light;

import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.probe.State;
import com.rastadrian.jblinky.core.probe.Status;
import com.rastadrian.jblinky.core.usb.UsbDevice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An abstract definition of a usb device that provides illumination.
 * Multiple implementations of usb light devices can be found on the <code>jblinky-devices</code> module.
 *
 * @author Adrian Pena
 */
public abstract class UsbLight extends UsbDevice implements Light {
    private Probe[] probes;
    private State currentState;
    private int warningCounter;
    private final ScheduledExecutorService scheduler;

    public UsbLight() {
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public void verifyProbes() {
        verifyProbes(null);
    }

    public void verifyProbes(Probe[] probes, ProbeCallback callback) {
        this.probes = probes;
        verifyProbes(callback);
    }

    public void verifyProbes(final ProbeCallback callback) {
        performScheduledTask(new Runnable() {
            public void run() {
                Map<String, Status> statuses = new HashMap<String, Status>();
                for (Probe probe : probes) {
                    warningCounter = 3;
                    boolean shouldRepeat;
                    do {
                        Status status = probe.verify();
                        shouldRepeat = handleState(status.getState());
                        currentState = status.getState();
                        statuses.put(probe.getName(), status);
                        if (callback != null) {
                            callback.onProbesUpdated(statuses);
                        }
                        sleep(1);
                    } while (shouldRepeat);
                }
            }
        });
    }

    /**
     * Turns off and disconnects from the usb light.
     */
    public void disconnect() {
        off();
        super.disconnect();
    }

    /**
     * Internally sets the jBlinky probes to the light while being created.
     *
     * @param probes the probes to be set.
     */
    void setProbes(Probe[] probes) {
        this.probes = probes;
    }

    /**
     * Retrieves the light probes, mostly for informational purposes.
     * @return the list of probes assigned to this light.
     */
    protected Probe[] getProbes() {
        return probes;
    }

    private void performScheduledTask(Runnable runnable) {
        try {
            scheduler.scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.SECONDS).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean handleState(State state) {
        if (state == null) {
            return true;
        }
        switch (state) {
            case SUCCESS:
                if (currentState != State.SUCCESS) {
                    success();
                }
                return false;
            case FAILURE:
                failure();
                return true;
            case WARNING:
                warningCounter--;
                warning();
                break;
            case IN_PROGRESS:
                warningCounter--;
                inProgress();
                break;
        }
        return !(warningCounter == 0);
    }
}

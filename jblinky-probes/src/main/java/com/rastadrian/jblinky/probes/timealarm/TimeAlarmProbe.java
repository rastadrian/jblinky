package com.rastadrian.jblinky.probes.timealarm;

import com.rastadrian.jblinky.core.light.Light;
import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.probe.ProbeLightHandler;
import com.rastadrian.jblinky.core.probe.State;
import com.rastadrian.jblinky.core.probe.Status;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Calendar;

/**
 * The Time Alarm Probe allows the monitoring of a particular time of day.
 *
 * @author Adrian Pena
 */
@Slf4j
public class TimeAlarmProbe implements Probe {
    private final int hours;
    private final int minutes;
    private final String alarmMessage;
    private final String name;

    /**
     * Create a new Time Alarm Probe that will trigger at the provided hours:minutes. When the alarm triggers,
     * it will return the provided alarmMessage as part of the Probe {@link Status}.
     *
     * @param builder the Probe's builder.
     */
    private TimeAlarmProbe(Builder builder) {
        LOGGER.info("Creating TimeAlarm Probe [{}]", builder.name);
        this.hours = builder.hours;
        this.minutes = builder.minutes;
        this.alarmMessage = builder.message;
        this.name = builder.name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Status verify(Light... lights) {
        LOGGER.debug("TimeAlarm Probe [{}]: Verifying probe...", name);
        Status status = doVerify();

        ProbeLightHandler probeLightHandler = new ProbeLightHandler();
        Arrays.stream(lights)
                .forEach(light -> probeLightHandler.accept(status.getState(), light));

        return status;
    }

    private Status doVerify() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteOfDay = calendar.get(Calendar.MINUTE);
        if (hourOfDay == hours && minuteOfDay == minutes) {
            LOGGER.debug("TimeAlarm Probe [{}]:The current time matches the alarm time [{}:{}], triggering probe.", name, hours, minutes);
            return new Status(State.IN_PROGRESS, new String[]{alarmMessage});
        }
        LOGGER.debug("TimeAlarm Probe [{}]: Current time [{}:{}] doesn't trigger alarm.", name, hourOfDay, minuteOfDay);
        return new Status(State.SUCCESS, new String[]{"No alarm yet. ⏰"});
    }

    /**
     * @return the <code>Probe</code>'s builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * The TimeAlarmProbe's builder.
     */
    public static class Builder {
        private int hours;
        private int minutes;
        private String message;
        private String name;

        private Builder() {
            //NOP
        }

        /**
         * This hour value should be in 24 hour format.
         *
         * @param hours   the alarm's hours
         * @param minutes the alarm's minutes
         * @return the builder's instance.
         */
        public Builder withTime(int hours, int minutes) {
            this.hours = hours;
            this.minutes = minutes;
            return this;
        }

        /**
         * @param message the message that should be returned when the alarm triggers.
         * @return the builder's instance.
         */
        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * @param name the Probe's name
         * @return the builder's instance.
         */
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * @return a <code>TimeAlarmProbe</code>'s instance.
         */
        public TimeAlarmProbe build() {
            if (this.name == null) {
                this.name = String.format("Time Alarm Probe @ %s:%s", hours, minutes);
            }
            return new TimeAlarmProbe(this);
        }
    }
}

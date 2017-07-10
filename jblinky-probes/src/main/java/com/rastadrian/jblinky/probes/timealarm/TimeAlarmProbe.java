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
     */
    private TimeAlarmProbe(Builder builder) {
        LOGGER.info("Creating TimeAlarm Probe [{}]", builder.name);
        this.hours = builder.hours;
        this.minutes = builder.minutes;
        this.alarmMessage = builder.message;
        this.name = builder.name;
    }

    public String getName() {
        return name;
    }

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
        return new Status(State.SUCCESS, new String[]{String.format("Alarm not triggered. Current time [%s:%s]", hourOfDay, minuteOfDay)});
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int hours;
        private int minutes;
        private String message;
        private String name;

        private Builder() {
            //NOP
        }

        public Builder withTime(int hours, int minutes) {
            this.hours = hours;
            this.minutes = minutes;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public TimeAlarmProbe build() {
            if (this.name == null) {
                this.name = String.format("Time Alarm Probe @ %s:%s", hours, minutes);
            }
            return new TimeAlarmProbe(this);
        }
    }
}

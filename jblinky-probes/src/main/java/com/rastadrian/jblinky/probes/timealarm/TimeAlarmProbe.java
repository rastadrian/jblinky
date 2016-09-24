package com.rastadrian.jblinky.probes.timealarm;

import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.probe.State;
import com.rastadrian.jblinky.core.probe.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * The Time Alarm Probe allows the monitoring of a particular time of day.
 *
 * @author Adrian Pena
 */
public class TimeAlarmProbe implements Probe {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeAlarmProbe.class);

    private final int hours;
    private final int minutes;
    private final String alarmMessage;
    private final String name;

    /**
     * Create a new Time Alarm Probe that will trigger at the provided hours:minutes. When the alarm triggers,
     * it will return the provided alarmMessage as part of the Probe {@link Status}.
     *
     * @param hours        the hour for the alarm
     * @param minutes      the minute for the alarm.
     * @param alarmMessage the alarm message to be shown when the alarm triggers.
     */
    public TimeAlarmProbe(int hours, int minutes, String alarmMessage) {
        this(String.format("Time Alarm Probe @ %s:%s", hours, minutes), hours, minutes, alarmMessage);
    }

    /**
     * Create a new Time Alarm Probe that will trigger at the provided hours:minutes. When the alarm triggers,
     * it will return the provided alarmMessage as part of the Probe {@link Status}.
     *
     * @param name         the probe's name.
     * @param hours        the hour for the alarm
     * @param minutes      the minute for the alarm.
     * @param alarmMessage the alarm message to be shown when the alarm triggers.
     */
    public TimeAlarmProbe(String name, int hours, int minutes, String alarmMessage) {
        LOGGER.info("Creating TimeAlarm Probe [{}]", name);
        this.hours = hours;
        this.minutes = minutes;
        this.alarmMessage = alarmMessage;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Status verify() {
        LOGGER.debug("TimeAlarm Probe [{}]: Verifying probe...", name);
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
}

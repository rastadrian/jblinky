package com.rastadrian.jblinky.probes.timealarm;

import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.probe.State;
import com.rastadrian.jblinky.core.probe.Status;

import java.util.Calendar;

/**
 * The Time Alarm Probe allows the monitoring of a particular time of day.
 *
 * @author Adrian Pena
 */
public class TimeAlarmProbe implements Probe {
    private final int hours;
    private final int minutes;
    private final String alarmMessage;
    private final String name;

    /**
     * Create a new Time Alarm Probe that will trigger at the provided hours:minutes. When the alarm triggers,
     * it will return the provided alarmMessage as part of the Probe {@link Status}.
     * @param hours the hour for the alarm
     * @param minutes the minute for the alarm.
     * @param alarmMessage the alarm message to be shown when the alarm triggers.
     */
    public TimeAlarmProbe(int hours, int minutes, String alarmMessage) {
        this(String.format("Time Alarm Probe @ %s:%s", hours, minutes), hours, minutes, alarmMessage);
    }

    /**
     * Create a new Time Alarm Probe that will trigger at the provided hours:minutes. When the alarm triggers,
     * it will return the provided alarmMessage as part of the Probe {@link Status}.
     * @param name the probe's name.
     * @param hours the hour for the alarm
     * @param minutes the minute for the alarm.
     * @param alarmMessage the alarm message to be shown when the alarm triggers.
     */
    public TimeAlarmProbe(String name, int hours, int minutes, String alarmMessage) {
        this.hours = hours;
        this.minutes = minutes;
        this.alarmMessage = alarmMessage;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Status verify() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteOfDay = calendar.get(Calendar.MINUTE);
        if (hourOfDay == hours && minuteOfDay == minutes) {
            return new Status(State.IN_PROGRESS, new String[]{alarmMessage});
        }
        return new Status(State.SUCCESS, new String[]{String.format("Alarm not triggered. Current time [%s:%s]", hourOfDay, minuteOfDay)});
    }
}

package com.rastadrian.jblinky.probes.timealarm;

import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.probe.State;
import com.rastadrian.jblinky.core.probe.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created on 9/27/16.
 *
 * @author Adrian Pena
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Calendar.class, TimeAlarmProbe.class})
public class TimeAlarmProbeTest {
    @Test
    public void getName_withDefaultName() throws Exception {
        String name;
        Probe probe;
        given: {
            probe = new TimeAlarmProbe(10,15, "Alarm message");
        }
        when: {
            name = probe.getName();
        }
        then: {
            assertThat(name).isEqualTo("Time Alarm Probe @ 10:15");
        }
    }

    @Test
    public void getName_withCustomName() throws Exception {
        String name;
        Probe probe;
        String receivedName;

        given: {
            name = "Custom Name";
            probe = new TimeAlarmProbe(name, 10,15, "Alarm message");
        }
        when: {
            receivedName = probe.getName();
        }
        then: {
            assertThat(receivedName).isEqualTo(name);
        }
    }

    @Test
    public void verify_withDifferentTime() throws Exception {
        TimeAlarmProbe probe;
        Status statusReport;
        given: {
            mockCalendarToReturnTime(11,16);
            probe = new TimeAlarmProbe(10,15, "Alarm message");
        }
        when: {
            statusReport = probe.verify();
        }
        then: {
            assertThat(statusReport).isNotNull();
            assertThat(statusReport.getState()).isEqualTo(State.SUCCESS);
            assertThat(statusReport.getMessages()).isNotEmpty();
            assertThat(statusReport.getMessages()[0]).isEqualTo("Alarm not triggered. Current time [11:16]");
        }
    }

    @Test
    public void verify_withSameTime() throws Exception {
        TimeAlarmProbe probe;
        Status statusReport;
        String alarmMessage = "Alarm message";
        given: {
            mockCalendarToReturnTime(10, 15);
            probe = new TimeAlarmProbe(10,15, alarmMessage);
        }
        when: {
            statusReport = probe.verify();
        }
        then: {
            assertThat(statusReport).isNotNull();
            assertThat(statusReport.getState()).isEqualTo(State.IN_PROGRESS);
            assertThat(statusReport.getMessages()).isNotEmpty();
            assertThat(statusReport.getMessages()[0]).isEqualTo(alarmMessage);
        }
    }

    private void mockCalendarToReturnTime(int hour, int minute) {
        mockStatic(Calendar.class);
        assertThat(Calendar.getInstance()).isNull();

        Calendar calendar = Mockito.mock(Calendar.class);
        when(calendar.get(eq(Calendar.HOUR_OF_DAY))).thenReturn(hour);
        when(calendar.get(eq(Calendar.MINUTE))).thenReturn(minute);

        when(Calendar.getInstance()).thenReturn(calendar);
    }
}
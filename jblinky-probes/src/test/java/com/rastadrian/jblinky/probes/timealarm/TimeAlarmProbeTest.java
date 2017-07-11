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
import static org.mockito.Matchers.eq;
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
        //Given
        Probe probe = TimeAlarmProbe.builder().withTime(10, 15)
                .withMessage("Alarm message")
                .build();

        //When
        String name = probe.getName();

        //Then
        assertThat(name).isEqualTo("Time Alarm Probe @ 10:15");
    }

    @Test
    public void getName_withCustomName() throws Exception {
        //Given
        String name = "Custom Name";
        Probe probe = TimeAlarmProbe.builder()
                .withName(name)
                .withTime(10, 15)
                .withMessage("Alarm message")
                .build();

        //When
        String receivedName = probe.getName();

        //Then
        assertThat(receivedName).isEqualTo(name);
    }

    @Test
    public void verify_withDifferentTime() throws Exception {
        //Given
        mockCalendarToReturnTime(11, 16);
        TimeAlarmProbe probe = TimeAlarmProbe.builder()
                .withTime(10, 15)
                .withMessage("Alarm message")
                .build();

        //When
        Status statusReport = probe.verify();

        //Then
        assertThat(statusReport).isNotNull();
        assertThat(statusReport.getState()).isEqualTo(State.SUCCESS);
        assertThat(statusReport.getMessages()).isNotEmpty();
        assertThat(statusReport.getMessages()[0]).isEqualTo("No alarm yet. ⏰");
    }

    @Test
    public void verify_withSameTime() throws Exception {
        //Given
        String alarmMessage = "Alarm message";
        mockCalendarToReturnTime(10, 15);
        TimeAlarmProbe probe = TimeAlarmProbe.builder()
                .withMessage(alarmMessage)
                .withTime(10, 15)
                .build();

        //When
        Status statusReport = probe.verify();

        //Then
        assertThat(statusReport).isNotNull();
        assertThat(statusReport.getState()).isEqualTo(State.IN_PROGRESS);
        assertThat(statusReport.getMessages()).isNotEmpty();
        assertThat(statusReport.getMessages()[0]).isEqualTo(alarmMessage);
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
package com.rastadrian.jblinky.core.light;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created on 7/8/17.
 *
 * @author Adrian Pena
 */
public class BaseLightTest {
    private BaseLight light;

    @Before
    public void setUp() throws Exception {
        light = spy(new BaseLight() {
            @Override
            public LightSource getSource() {
                return null;
            }

            @Override
            public String getId() {
                return null;
            }
        });
    }

    @Test
    public void success() throws Exception {
        //When
        light.success();

        //Then
        assertThat(light.getState()).isEqualTo(LightState.SUCCESS);
    }

    @Test
    public void failure() throws Exception {
        //When
        light.failure();

        //Then
        assertThat(light.getState()).isEqualTo(LightState.FAILURE);
    }

    @Test
    public void inProgress() throws Exception {
        //When
        light.inProgress();

        //Then
        assertThat(light.getState()).isEqualTo(LightState.IN_PROGRESS);
    }

    @Test
    public void warning() throws Exception {
        //When
        light.warning();

        //Then
        assertThat(light.getState()).isEqualTo(LightState.WARNING);
    }

    @Test
    public void off() throws Exception {
        //When
        light.off();

        //Then
        assertThat(light.getState()).isEqualTo(LightState.OFF);
    }

    @Test
    public void disconnect() throws Exception {
        //When
        light.disconnect();

        //Then
        verify(light, times(1)).off();
        assertThat(light.getState()).isEqualTo(LightState.DISCONNECTED);
    }

}
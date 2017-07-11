package com.rastadrian.jblinky.core;

import com.rastadrian.jblinky.core.light.Light;
import com.rastadrian.jblinky.core.light.LightSource;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 9/24/16.
 *
 * @author Adrian Pena
 */
public class JBlinkyTest {

    @Test
    public void jBlinkyWithLightSource() throws Exception {
        //given
        Light light = mock(Light.class);
        LightSource source = mock(LightSource.class);
        Collection<? extends Light> lights = Collections.singletonList(light);
        doReturn(lights).when(source).getLights();

        //when
        JBlinky jBlinky = JBlinky.builder()
                .withLightSources(source)
                .build();

        //then
        assertThat(jBlinky.getLight()).isNotNull().isEqualTo(light);
        assertThat(jBlinky.getLights()).contains(light);
        verify(source, times(1)).getLights();
    }

    @Test(expected = JBlinkyException.class)
    public void jBlinkyWithNoLightsFound() throws Exception {
        //given
        LightSource source = mock(LightSource.class);
        when(source.getLights()).thenReturn(Collections.emptyList());

        //when
        JBlinky.builder()
                .withLightSources(source)
                .build();
    }

    @Test(expected = JBlinkyException.class)
    public void jBlinkyWithNoLightSources() throws Exception {
        //when
        JBlinky.builder().build();
    }
}
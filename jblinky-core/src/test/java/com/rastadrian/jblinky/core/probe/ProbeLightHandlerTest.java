package com.rastadrian.jblinky.core.probe;

import com.rastadrian.jblinky.core.light.Light;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created on 7/8/17.
 *
 * @author Adrian Pena
 */
public class ProbeLightHandlerTest {

    private ProbeLightHandler probeLightHandler;

    @Before
    public void setUp() throws Exception {
        probeLightHandler = new ProbeLightHandler();
    }

    @Test
    public void acceptWithInProgressState() throws Exception {
        //Given
        Light light = mock(Light.class);
        State state = State.IN_PROGRESS;

        //When
        probeLightHandler.accept(state, light);

        //Then
        verify(light, times(1)).inProgress();
    }

    @Test
    public void acceptWithFailureState() throws Exception {
        //Given
        Light light = mock(Light.class);
        State state = State.FAILURE;

        //When
        probeLightHandler.accept(state, light);

        //Then
        verify(light, times(1)).failure();
    }

    @Test
    public void acceptWithWarningState() throws Exception {
        //Given
        Light light = mock(Light.class);
        State state = State.WARNING;

        //When
        probeLightHandler.accept(state, light);

        //Then
        verify(light, times(1)).warning();
    }

    @Test
    public void acceptWithSuccessState() throws Exception {
        //Given
        Light light = mock(Light.class);
        State state = State.SUCCESS;

        //When
        probeLightHandler.accept(state, light);

        //Then
        verify(light, times(1)).success();
    }

    @Test
    public void acceptWithNoState() throws Exception {
        //Given
        Light light = mock(Light.class);

        //When
        probeLightHandler.accept(null, light);

        //Then
        verify(light, times(0)).success();
        verify(light, times(0)).failure();
        verify(light, times(0)).warning();
        verify(light, times(0)).inProgress();
    }
}
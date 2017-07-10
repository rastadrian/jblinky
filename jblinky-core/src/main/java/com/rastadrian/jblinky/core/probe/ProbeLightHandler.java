package com.rastadrian.jblinky.core.probe;

import com.rastadrian.jblinky.core.light.Light;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BiConsumer;

/**
 * Created on 7/6/17.
 *
 * @author Adrian Pena
 */
@Slf4j
public class ProbeLightHandler implements BiConsumer<State, Light> {

    @Override
    public void accept(State state, Light light) {
        if (state == null || light == null) {
            LOGGER.error("Unable to handle State on Light.");
            return;
        }
        switch (state) {
            case SUCCESS:
                light.success();
                break;
            case FAILURE:
                light.failure();
                break;
            case WARNING:
                light.warning();
                break;
            case IN_PROGRESS:
                light.inProgress();
                break;
        }
    }
}

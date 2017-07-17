package com.rastadrian.jblinky.devices.phillipshue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rastadrian.jblinky.core.light.BaseLight;
import com.rastadrian.jblinky.core.light.LightSource;
import com.rastadrian.jblinky.core.light.LightState;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by adrian on 7/16/17.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HueLight extends BaseLight {

    @JsonIgnore
    private HueLightSource source;

    @JsonIgnore
    private HueCommunicationHandler communicationHandler;

    @JsonIgnore
    private String index;

    @JsonProperty("state")
    private HueLightState hueState;

    @JsonProperty("name")
    private String name;

    @JsonProperty("modelid")
    private String modelId;

    @JsonProperty("uniqueid")
    private String uniqueId;

    @Override
    public LightSource getSource() {
        return source;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public void success() {
        if (getState() == LightState.SUCCESS) {
            return;
        }
        super.success();
        setColor(0.41f, 0.51721f);
    }

    @Override
    public void failure() {
        super.failure();
        setColor(0.6679f, 0.3181f);
    }

    @Override
    public void inProgress() {
        super.inProgress();
        setColor(0.1691f, 0.0441f);
    }

    @Override
    public void warning() {
        super.warning();
        setColor(0.5425f, 0.4196f);
    }

    @Override
    public void off() {
        super.off();
        this.hueState.setOn(false);
        communicationHandler.updateLightState(this);
    }

    private void setColor(float x, float y) {
        this.hueState.setOn(true);
        this.hueState.setXy(new float[]{x, y});
        this.hueState.setColorTemperature(400);
        communicationHandler.updateLightState(this);
    }
}

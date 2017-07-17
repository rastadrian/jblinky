package com.rastadrian.jblinky.devices.phillipshue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by adrian on 7/16/17.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class HueLightState {

    @JsonProperty("on")
    private boolean on;

    @JsonProperty("bri")
    private int brightness;

    @JsonProperty("hue")
    private int hue;

    @JsonProperty("sat")
    private int saturation;

    @JsonProperty("xy")
    private float[] xy;

    @JsonProperty("ct")
    private int colorTemperature;
}

package com.rastadrian.jblinky.probes.cctray;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A Cruise Control Tray project entry.
 *
 * @author Adrian Pena
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class CCProject {

    private String name;

    private String lastBuildStatus;
}

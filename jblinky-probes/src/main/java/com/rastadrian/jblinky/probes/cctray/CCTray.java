package com.rastadrian.jblinky.probes.cctray;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * A CI Service Cruise Control Tray. This XML document provides the summary of a particular Project in a CI Server.
 *
 * @author Adrian Pena
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class CCTray {

    private List<CCProject> projects;

}

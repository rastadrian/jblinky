package com.rastadrian.jblinky.probes.cctray;

import java.util.List;

/**
 * A CI Service Cruise Control Tray. This XML document provides the summary of a particular Project in a CI Server.
 *
 * @author Adrian Pena
 */
class CCTray {
    private List<Project> projects;

    List<Project> getProjects() {
        return projects;
    }

    void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}

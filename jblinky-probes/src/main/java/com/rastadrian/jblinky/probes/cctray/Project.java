package com.rastadrian.jblinky.probes.cctray;

/**
 * A Cruise Control Tray project entry.
 *
 * @author Adrian Pena
 */
class Project {
    private String name;
    private String lastBuildStatus;

    public Project() {
    }

    Project(String name, String lastBuildStatus) {
        this.name = name;
        this.lastBuildStatus = lastBuildStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastBuildStatus() {
        return lastBuildStatus;
    }

    public void setLastBuildStatus(String lastBuildStatus) {
        this.lastBuildStatus = lastBuildStatus;
    }
}

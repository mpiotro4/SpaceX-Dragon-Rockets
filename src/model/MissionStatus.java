package model;

public enum MissionStatus {
    SCHEDULED("Scheduled"),
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    ENDED("Ended");

    private final String description;

    MissionStatus(String description) {
        this.description = description;
    }

    public String getStatusDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}

package model;

public enum RocketStatus {
    ON_GROUND("On ground"),
    IN_SPACE("In space"),
    IN_REPAIR("In repair");

    private final String description;

    RocketStatus(String description) {
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

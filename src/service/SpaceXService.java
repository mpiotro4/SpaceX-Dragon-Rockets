package service;

import java.util.List;
import model.MissionStatus;
import model.impl.MissionSummary;
import model.RocketStatus;

/**
 * Core service interface for SpaceX operations.
 */
public interface SpaceXService {
    /**
     * Creates a new Rocket and returns its ID.
     */
    int addRocket();

    /**
     * Creates a new Mission with the given name and returns its ID.
     */
    int addMission(String missionName);

    /**
     * Assigns a rocket to a mission by their IDs.
     */
    void assignRocketToMission(int rocketId, int missionId);

    /**
     * Assigns multiple rockets to a mission by their IDs.
     */
    void assignRocketsToMission(List<Integer> rocketIds, int missionId);

    /**
     * Changes the status of an existing rocket.
     */
    void changeRocketStatus(int rocketId, RocketStatus newStatus);

    /**
     * Changes the status of an existing mission.
     */
    void changeMissionStatus(int missionId, MissionStatus newStatus);

    /**
     * Returns a summary of all missions.
     */
    List<MissionSummary> getSummary();
}

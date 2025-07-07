package service;

import java.util.List;
import model.Mission;
import model.MissionStatus;
import model.MissionSummary;
import model.Rocket;
import model.RocketStatus;

public interface SpaceXService {
    int addRocket();
    void assignRocketToMission(int rocketId, int missionId);
    void assignRocketsToMission(List<Integer> rocketsIds, int missionId);
    void changeRocketStatus(int rocket, RocketStatus newStatus);
    void changeMissionStatus(int missionId, MissionStatus newStatus);
    int addMission(String missionName);
    List<MissionSummary> getSummary();
}

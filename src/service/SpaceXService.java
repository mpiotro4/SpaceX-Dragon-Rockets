package service;

import java.util.List;
import model.Mission;
import model.MissionStatus;
import model.MissionSummary;
import model.Rocket;
import model.RocketStatus;

public interface SpaceXService {
    void addRocket(Rocket rocket);
    void assignRocketToMission(Rocket rocket, Mission mission);
    void assignRocketsToMission(List<Rocket> rockets, Mission mission);
    void changeRocketStatus(Rocket rocket, RocketStatus newStatus);
    void changeMissionStatus(Mission mission, MissionStatus newStatus);
    void addMission(Mission mission);
    List<MissionSummary> getSummary();
}

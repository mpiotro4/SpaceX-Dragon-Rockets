package service;

import java.util.List;
import java.util.Map;

public interface StatusService {
    void updateMissionStatus(int missionId, Map<Integer, List<Integer>> assignments);
    void updateRocketStatus(int rocketId, Map<Integer, java.util.List<Integer>> assignments);
    void updateMissionStatusForRocket(int rocketId, Map<Integer, java.util.List<Integer>> assignments);
    void updateRocketsStatusForMission(int missionId, Map<Integer, java.util.List<Integer>> assignments);
}
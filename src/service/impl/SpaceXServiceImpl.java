package service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import model.Rocket;
import model.Mission;
import model.MissionSummary;
import model.RocketStatus;
import model.MissionStatus;
import service.SpaceXService;

import java.util.List;

public class SpaceXServiceImpl implements SpaceXService {

    private final Map<Integer, Rocket> rockets = new HashMap<>();
    private final Map<Integer, Mission> missions = new HashMap<>();
    private final Map<Integer, List<Integer>> missionAssignments = new HashMap<>();
    private int nextRocketId = 1;
    private int nextMissionId = 1;

    @Override
    public int addRocket() {
        int id = nextRocketId++;
        Rocket rocket = new Rocket();
        rocket.setId(id);
        rocket.setStatus(RocketStatus.ON_GROUND);
        rockets.put(id, rocket);
        return id;
    }

    @Override
    public void assignRocketToMission(int rocketId, int missionId) {
        missionAssignments.computeIfAbsent(missionId, mId -> new ArrayList<>());
        missionAssignments.get(missionId).add(rocketId);
    }

    @Override
    public void assignRocketsToMission(List<Integer> rocketIds, int missionId) {
        rocketIds.forEach(rId -> assignRocketToMission(rId, missionId));
    }

    @Override
    public void changeRocketStatus(int rocketId, RocketStatus newStatus) {
        Rocket stored = rockets.get(rocketId);
        if (stored != null) {
            stored.setStatus(newStatus);
        }
    }

    @Override
    public void changeMissionStatus(int missionId, MissionStatus newStatus) {
        Mission stored = missions.get(missionId);
        if (stored != null) {
            stored.setStatus(newStatus);
        }
    }

    @Override
    public int addMission(String missionName) {
        int id = nextMissionId++;
        Mission mission = new Mission();
        mission.setId(id);
        mission.setStatus(MissionStatus.SCHEDULED);
        mission.setName(missionName);
        missions.put(id, mission);
        missionAssignments.putIfAbsent(id, new ArrayList<>());
        return id;
    }

    @Override
    public List<MissionSummary> getSummary() {
        List<MissionSummary> summaries = new ArrayList<>();
        for (Mission mission : missions.values()) {
            int assigned = missionAssignments.getOrDefault(mission.getId(), Collections.emptyList()).size();
            MissionSummary summary = new MissionSummary();
            summary.setMissionName(String.valueOf(mission.getName()));
            summary.setStatus(mission.getStatus());
            summary.setAssignedRockets(assigned);
            summaries.add(summary);
        }
        return summaries;
    }
}
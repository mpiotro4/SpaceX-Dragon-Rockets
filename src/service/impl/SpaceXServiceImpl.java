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

    @Override
    public void addRocket(Rocket rocket) {
        rockets.put(rocket.getId(), rocket);
    }

    @Override
    public void assignRocketToMission(Rocket rocket, Mission mission) {
        missionAssignments.computeIfAbsent(mission.getId(), id -> new ArrayList<>());
        missionAssignments.get(mission.getId()).add(rocket.getId());
    }

    @Override
    public void assignRocketsToMission(List<Rocket> rockets, Mission mission) {
        for (Rocket r : rockets) {
            assignRocketToMission(r, mission);
        }
    }

    @Override
    public void changeRocketStatus(Rocket rocket, RocketStatus newStatus) {
        Rocket stored = rockets.get(rocket.getId());
        if (stored != null) {
            stored.setStatus(newStatus);
        }
    }

    @Override
    public void changeMissionStatus(Mission mission, MissionStatus newStatus) {
        Mission stored = missions.get(mission.getId());
        if (stored != null) {
            stored.setStatus(newStatus);
        }
    }

    @Override
    public void addMission(Mission mission) {
        missions.put(mission.getId(), mission);
        missionAssignments.putIfAbsent(mission.getId(), new ArrayList<>());
    }

    @Override
    public List<MissionSummary> getSummary() {
        List<MissionSummary> summaries = new ArrayList<>();
        for (Mission mission : missions.values()) {
            int assigned = missionAssignments.getOrDefault(mission.getId(), Collections.emptyList()).size();
            MissionSummary summary = new MissionSummary();
            summary.setMissionName(String.valueOf(mission.getId()));
            summary.setStatus(mission.getStatus());
            summary.setAssignedRockets(assigned);
            summaries.add(summary);
        }
        return summaries;
    }
}
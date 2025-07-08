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
import service.RocketFactory;
import service.SpaceXService;

import java.util.List;
import service.SummaryService;

public class SpaceXServiceImpl implements SpaceXService {
    private final Map<Integer, Rocket> rockets = new HashMap<>();

    private final Map<Integer, Mission> missions = new HashMap<>();

    private final Map<Integer, List<Integer>> missionAssignments = new HashMap<>();

    private final SummaryService summaryService;

    private final RocketFactory rocketFactory;

    private int nextRocketId = 1;

    private int nextMissionId = 1;

    public SpaceXServiceImpl(SummaryService summaryService, RocketFactory rocketFactory) {
        this.summaryService = summaryService;
        this.rocketFactory = rocketFactory;
    }

    public SpaceXServiceImpl() {
        this(new InMemorySummaryService(), new DragonRocketFactory());
    }

    @Override
    public int addRocket() {
        int id = nextRocketId++;
        Rocket rocket = rocketFactory.createRocket(id);
        rocket.setId(id);
        rocket.setStatus(RocketStatus.ON_GROUND);
        rockets.put(id, rocket);
        return id;
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
    public void assignRocketToMission(int rocketId, int missionId) {
        missionAssignments.computeIfAbsent(missionId, mId -> new ArrayList<>()).add(rocketId);
        deriveAndSetMission(missionId);
        deriveAndSetRocket(rocketId);
    }

    @Override
    public void assignRocketsToMission(List<Integer> rocketIds, int missionId) {
        for (int rId : rocketIds) {
            assignRocketToMission(rId, missionId);
        }
    }

    @Override
    public void changeRocketStatus(int rocketId, RocketStatus newStatus) {
        Rocket r = rockets.get(rocketId);
        if (r != null) {
            r.setStatus(newStatus);
            missionAssignments.forEach((mid, list) -> {
                if (list.contains(rocketId)) {
                    deriveAndSetMission(mid);
                }
            });
        }
    }

    @Override
    public void changeMissionStatus(int missionId, MissionStatus newStatus) {
        Mission m = missions.get(missionId);
        if (m != null) {
            m.setStatus(newStatus);
            missionAssignments.getOrDefault(missionId, Collections.emptyList())
                              .forEach(this::deriveAndSetRocket);
        }
    }

    @Override
    public List<MissionSummary> getSummary() {
        return summaryService.generate(missions, missionAssignments);
    }

    private void deriveAndSetMission(int missionId) {
        Mission m = missions.get(missionId);
        if (m != null) {
            List<RocketStatus> statuses = missionAssignments.getOrDefault(missionId, Collections.emptyList()).stream()
                                                            .map(rockets::get)
                                                            .map(Rocket::getStatus)
                                                            .toList();
            MissionStatus newSt = MissionStatus.derive(statuses, m.getStatus());
            m.setStatus(newSt);
        }
    }

    private void deriveAndSetRocket(int rocketId) {
        Rocket r = rockets.get(rocketId);
        if (r != null) {
            List<MissionStatus> missionsStatuses = missionAssignments.entrySet().stream()
                                                                     .filter(e -> e.getValue().contains(rocketId))
                                                                     .map(e -> missions.get(e.getKey()).getStatus())
                                                                     .toList();
            RocketStatus newSt = RocketStatus.derive(missionsStatuses, r.getStatus());
            r.setStatus(newSt);
        }
    }
}

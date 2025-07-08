package service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import model.Rocket;
import model.Mission;
import model.MissionSummary;
import model.RocketStatus;
import model.MissionStatus;
import repository.MissionRepository;
import repository.RocketRepository;
import repository.impl.InMemoryMissionRepository;
import repository.impl.InMemoryRocketRepository;
import service.RocketFactory;
import service.SpaceXService;

import java.util.List;
import service.SummaryService;

public class SpaceXServiceImpl implements SpaceXService {

    private final Map<Integer, List<Integer>> missionAssignments = new HashMap<>();

    private final SummaryService summaryService;

    private final RocketFactory rocketFactory;

    private final RocketRepository rocketRepo;

    private final MissionRepository missionRepo;

    private int nextRocketId = 1;

    private int nextMissionId = 1;

    public SpaceXServiceImpl(SummaryService summaryService, RocketFactory rocketFactory, RocketRepository rocketRepo, MissionRepository missionRepo) {
        this.summaryService = summaryService;
        this.rocketFactory = rocketFactory;
        this.rocketRepo = rocketRepo;
        this.missionRepo = missionRepo;
    }

    public SpaceXServiceImpl() {
        this(new InMemorySummaryService(), new DragonRocketFactory(), new InMemoryRocketRepository(), new InMemoryMissionRepository());
    }

   @Override
    public int addRocket() {
        int id = nextRocketId++;
        Rocket rocket = rocketFactory.createRocket(id);
        rocket.setStatus(RocketStatus.ON_GROUND);
        rocketRepo.save(rocket);
        return id;
    }

    @Override
    public int addMission(String missionName) {
        int id = nextMissionId++;
        Mission mission = new Mission();
        mission.setId(id);
        mission.setName(missionName);
        mission.setStatus(MissionStatus.SCHEDULED);
        missionRepo.save(mission);
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
        rocketIds.forEach(rId -> assignRocketToMission(rId, missionId));
    }

    @Override
    public void changeRocketStatus(int rocketId, RocketStatus newStatus) {
        rocketRepo.findById(rocketId).ifPresent(r -> {
            r.setStatus(newStatus);
            rocketRepo.save(r);
            missionAssignments.forEach((mid, list) -> {
                if (list.contains(rocketId)) {
                    deriveAndSetMission(mid);
                }
            });
        });
    }

    @Override
    public void changeMissionStatus(int missionId, MissionStatus newStatus) {
        missionRepo.findById(missionId).ifPresent(m -> {
            m.setStatus(newStatus);
            missionRepo.save(m);
            missionAssignments.getOrDefault(missionId, Collections.emptyList())
                .forEach(this::deriveAndSetRocket);
        });
    }

    @Override
    public List<MissionSummary> getSummary() {
        Map<Integer, Mission> missions = new HashMap<>();
        missionRepo.findAll().forEach(m -> missions.put(m.getId(), m));
        return summaryService.generate(missions, missionAssignments);
    }

    private void deriveAndSetMission(int missionId) {
        missionRepo.findById(missionId).ifPresent(m -> {
            List<RocketStatus> statuses = missionAssignments.getOrDefault(missionId, Collections.emptyList()).stream()
                .map(rocketRepo::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Rocket::getStatus)
                .toList();
            MissionStatus newSt = MissionStatus.derive(statuses, m.getStatus());
            m.setStatus(newSt);
            missionRepo.save(m);
        });
    }

    private void deriveAndSetRocket(int rocketId) {
        rocketRepo.findById(rocketId).ifPresent(r -> {
            List<MissionStatus> missionStatuses = missionAssignments.entrySet().stream()
                .filter(e -> e.getValue().contains(rocketId))
                .map(e -> missionRepo.findById(e.getKey()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Mission::getStatus)
                .toList();
            RocketStatus newSt = RocketStatus.derive(missionStatuses, r.getStatus());
            r.setStatus(newSt);
            rocketRepo.save(r);
        });
    }
}

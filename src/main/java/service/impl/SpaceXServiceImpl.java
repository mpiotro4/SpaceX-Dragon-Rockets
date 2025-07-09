package service.impl;

import factory.MissionFactory;
import factory.impl.DragonMissionFactory;
import factory.impl.DragonRocketFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.Rocket;
import model.Mission;
import model.impl.MissionSummary;
import model.RocketStatus;
import model.MissionStatus;
import repository.CrudRepository;
import repository.impl.InMemoryMissionRepository;
import repository.impl.InMemoryRocketRepository;
import factory.RocketFactory;
import service.SpaceXService;

import java.util.List;
import service.StatusService;
import service.SummaryService;

public class SpaceXServiceImpl implements SpaceXService {

    private final Map<Integer, List<Integer>> missionAssignments = new HashMap<>();

    private final SummaryService summaryService;

    private final Map<Class<? extends Rocket>, RocketFactory> rocketFactories;

    private final Map<Class<? extends Mission>, MissionFactory> missionFactories;

    private final CrudRepository<Rocket, Integer> rocketRepo;

    private final CrudRepository<Mission, Integer> missionRepo;

    private final StatusService statusService;

    public SpaceXServiceImpl(
            SummaryService summaryService,
            Map<Class<? extends Rocket>, RocketFactory> rocketFactories,
            Map<Class<? extends Mission>, MissionFactory> missionFactories,
            CrudRepository<Rocket, Integer> rocketRepo,
            CrudRepository<Mission, Integer> missionRepo,
            StatusService statusService
    ) {
        this.summaryService = summaryService;
        this.rocketFactories = rocketFactories;
        this.missionFactories = missionFactories;
        this.rocketRepo = rocketRepo;
        this.missionRepo = missionRepo;
        this.statusService = statusService;
    }

    public SpaceXServiceImpl() {
        CrudRepository<Rocket, Integer> rRepo = new InMemoryRocketRepository();
        CrudRepository<Mission, Integer> mRepo = new InMemoryMissionRepository();
        this.summaryService = new service.impl.InMemorySummaryService();
        this.rocketFactories = new HashMap<>();
        this.rocketFactories.put(model.impl.DragonRocket.class, new DragonRocketFactory());
        this.missionFactories = new HashMap<>();
        this.missionFactories.put(model.impl.DragonMission.class, new DragonMissionFactory());
        this.rocketRepo = rRepo;
        this.missionRepo = mRepo;
        this.statusService = new StatusServiceImpl(rRepo, mRepo);
    }

    @Override
    public int addRocket(Class<? extends Rocket> rocketType) {
        RocketFactory factory = rocketFactories.get(rocketType);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for " + rocketType);
        }
        Rocket rocket = factory.createRocket();
        rocket.setStatus(RocketStatus.ON_GROUND);
        return rocketRepo.save(rocket).getId();
    }

    @Override
    public int addMission(String missionName, Class<? extends Mission> missionType) {
        MissionFactory factory = missionFactories.get(missionType);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for " + missionType);
        }
        Mission mission = factory.createMission(missionName);
        Mission saved = missionRepo.save(mission);
        missionAssignments.putIfAbsent(saved.getId(), new ArrayList<>());
        return saved.getId();
    }

    @Override
    public void assignRocketToMission(int rocketId, int missionId) {
        missionAssignments.computeIfAbsent(missionId, id -> new ArrayList<>()).add(rocketId);
        statusService.updateMissionStatus(missionId, missionAssignments);
        statusService.updateRocketStatus(rocketId, missionAssignments);
    }

    @Override
    public void assignRocketsToMission(List<Integer> rocketIds, int missionId) {
        for (int rId : rocketIds) {
            assignRocketToMission(rId, missionId);
        }
    }

    @Override
    public void changeRocketStatus(int rocketId, RocketStatus newStatus) {
        rocketRepo.findById(rocketId).ifPresent(r -> {
            r.setStatus(newStatus);
            rocketRepo.save(r);
            statusService.updateMissionStatusForRocket(rocketId, missionAssignments);
        });
    }

    @Override
    public void changeMissionStatus(int missionId, MissionStatus newStatus) {
        missionRepo.findById(missionId).ifPresent(m -> {
            m.setStatus(newStatus);
            missionRepo.save(m);
            statusService.updateRocketsStatusForMission(missionId, missionAssignments);
        });
    }

    @Override
    public List<MissionSummary> getSummary() {
        Map<Integer, Mission> all = new HashMap<>();
        missionRepo.findAll().forEach(m -> all.put(m.getId(), m));
        return summaryService.generate(all, missionAssignments);
    }
}

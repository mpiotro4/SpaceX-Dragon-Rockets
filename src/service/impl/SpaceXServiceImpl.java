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

    private final RocketFactory rocketFactory;

    private final MissionFactory missionFactory;

    private final CrudRepository<Rocket, Integer> rocketRepo;

    private final CrudRepository<Mission, Integer> missionRepo;

    private final StatusService statusService;

    public SpaceXServiceImpl(
            SummaryService summaryService,
            RocketFactory rocketFactory,
            MissionFactory missionFactory,
            CrudRepository<Rocket, Integer> rocketRepo,
            CrudRepository<Mission, Integer> missionRepo,
            StatusService statusService
    ) {
        this.summaryService = summaryService;
        this.rocketFactory = rocketFactory;
        this.missionFactory = missionFactory;
        this.rocketRepo = rocketRepo;
        this.missionRepo = missionRepo;
        this.statusService = statusService;
    }

    public SpaceXServiceImpl() {
        CrudRepository<Rocket, Integer> rRepo = new InMemoryRocketRepository();
        CrudRepository<Mission, Integer> mRepo = new InMemoryMissionRepository();
        this.summaryService = new service.impl.InMemorySummaryService();
        this.rocketFactory = new DragonRocketFactory();
        this.missionFactory = new DragonMissionFactory();
        this.rocketRepo = rRepo;
        this.missionRepo = mRepo;
        this.statusService = new StatusServiceImpl(rRepo, mRepo);
    }

    @Override
    public int addRocket() {
        Rocket rocket = rocketFactory.createRocket();
        rocket.setStatus(RocketStatus.ON_GROUND);
        return rocketRepo.save(rocket).getId();
    }

    @Override
    public int addMission(String missionName) {
        Mission mission = missionFactory.createMission(missionName);
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

package test.repository.impl;

import static org.junit.Assert.*;

import model.DragonMission;
import org.junit.Before;
import org.junit.Test;
import model.DragonRocket;
import model.Rocket;
import model.Mission;
import model.RocketStatus;
import model.MissionStatus;
import repository.CrudRepository;
import repository.impl.InMemoryRocketRepository;
import repository.impl.InMemoryMissionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import service.StatusService;
import service.impl.StatusServiceImpl;

public class StatusServiceTest {
    private CrudRepository<Rocket, Integer> rocketRepo;
    private CrudRepository<Mission, Integer> missionRepo;
    private StatusService statusService;

    @Before
    public void setUp() {
        rocketRepo = new InMemoryRocketRepository();
        missionRepo = new InMemoryMissionRepository();
        statusService = new StatusServiceImpl(rocketRepo, missionRepo);
    }

    @Test
    public void testUpdateMissionStatusToPendingWhenRocketInRepair() {
        Mission mission = new DragonMission();
        mission.setName("TestMission");
        mission.setStatus(MissionStatus.SCHEDULED);
        mission = missionRepo.save(mission);

        Rocket rocket = new DragonRocket();
        rocket.setStatus(RocketStatus.IN_REPAIR);
        rocket = rocketRepo.save(rocket);

        Map<Integer, List<Integer>> assignments = new HashMap<>();
        assignments.put(mission.getId(), List.of(rocket.getId()));

        statusService.updateMissionStatus(mission.getId(), assignments);
        Mission updated = missionRepo.findById(mission.getId()).get();
        assertEquals(MissionStatus.PENDING, updated.getStatus());
    }

    @Test
    public void testUpdateMissionStatusToInProgressWhenRocketInSpace() {
        Mission mission = new DragonMission();
        mission.setName("TestMission");
        mission.setStatus(MissionStatus.SCHEDULED);
        mission = missionRepo.save(mission);

        Rocket rocket = new DragonRocket();
        rocket.setStatus(RocketStatus.IN_SPACE);
        rocket = rocketRepo.save(rocket);

        Map<Integer, List<Integer>> assignments = new HashMap<>();
        assignments.put(mission.getId(), List.of(rocket.getId()));

        statusService.updateMissionStatus(mission.getId(), assignments);
        Mission updated = missionRepo.findById(mission.getId()).get();
        assertEquals(MissionStatus.IN_PROGRESS, updated.getStatus());
    }

    @Test
    public void testUpdateRocketStatusToInRepairWhenMissionPending() {
        Mission mission = new DragonMission();
        mission.setName("TestMission");
        mission.setStatus(MissionStatus.SCHEDULED);
        mission = missionRepo.save(mission);

        Rocket rocket = new DragonRocket();
        rocket.setStatus(RocketStatus.ON_GROUND);
        rocket = rocketRepo.save(rocket);

        // Manually set mission to PENDING
        mission.setStatus(MissionStatus.PENDING);
        missionRepo.save(mission);

        Map<Integer, List<Integer>> assignments = new HashMap<>();
        assignments.put(mission.getId(), List.of(rocket.getId()));

        statusService.updateRocketStatus(rocket.getId(), assignments);
        Rocket updated = rocketRepo.findById(rocket.getId()).get();
        assertEquals(RocketStatus.IN_REPAIR, updated.getStatus());
    }

    @Test
    public void testUpdateRocketStatusToInSpaceWhenMissionInProgress() {
        Mission mission = new DragonMission();
        mission.setName("TestMission");
        mission.setStatus(MissionStatus.IN_PROGRESS);
        mission = missionRepo.save(mission);

        Rocket rocket = new DragonRocket();
        rocket.setStatus(RocketStatus.ON_GROUND);
        rocket = rocketRepo.save(rocket);

        Map<Integer, List<Integer>> assignments = new HashMap<>();
        assignments.put(mission.getId(), List.of(rocket.getId()));

        statusService.updateRocketStatus(rocket.getId(), assignments);
        Rocket updated = rocketRepo.findById(rocket.getId()).get();
        assertEquals(RocketStatus.IN_SPACE, updated.getStatus());
    }
}

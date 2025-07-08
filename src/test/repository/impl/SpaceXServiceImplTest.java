package test.repository.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import model.RocketStatus;
import org.junit.Test;
import service.SpaceXService;
import model.MissionStatus;
import model.impl.MissionSummary;
import java.util.List;
import service.impl.SpaceXServiceImpl;

public class SpaceXServiceImplTest {

    private final SpaceXService service = new SpaceXServiceImpl();

    @Test
    public void testGetSummaryWhenEmpty() {
        List<MissionSummary> summaries = service.getSummary();
        assertNotNull(summaries);
        assertTrue(summaries.isEmpty());
    }

    @Test
    public void testAddMissionThenGetSummary() {
        String name = "Mars";
        int missionId = service.addMission(name);
        List<MissionSummary> summaries = service.getSummary();
        assertEquals(1, summaries.size());
        MissionSummary summary = summaries.get(0);
        assertEquals(name, summary.getMissionName());
        assertEquals(MissionStatus.SCHEDULED, summary.getStatus());
        assertEquals(0, summary.getAssignedRockets());
    }

    @Test
    public void testAssignMultipleRocketsToMissionUpdatesSummary() {
        String name = "Moon";
        int missionId = service.addMission(name);
        int r1 = service.addRocket();
        int r2 = service.addRocket();
        service.assignRocketsToMission(Arrays.asList(r1, r2), missionId);

        List<MissionSummary> summaries = service.getSummary();
        assertEquals(2, summaries.get(0).getAssignedRockets());
    }

    @Test
    public void testChangeRocketStatusToInRepairSetsMissionPending() {
        String name = "Mars";
        int missionId = service.addMission(name);
        int rocketId = service.addRocket();
        service.assignRocketToMission(rocketId, missionId);
        service.changeRocketStatus(rocketId, RocketStatus.IN_REPAIR);

        List<MissionSummary> summaries = service.getSummary();
        assertEquals(MissionStatus.PENDING, summaries.get(0).getStatus());
    }

    @Test
    public void testMissionEndsWhenAllRocketsReturnOnGround() {
        String name = "ISS";
        int missionId = service.addMission(name);
        int rocketId = service.addRocket();
        service.assignRocketToMission(rocketId, missionId);
        service.changeRocketStatus(rocketId, RocketStatus.IN_SPACE);
        service.changeRocketStatus(rocketId, RocketStatus.ON_GROUND);

        List<MissionSummary> summaries = service.getSummary();
        assertEquals(MissionStatus.ENDED, summaries.get(0).getStatus());
    }

    @Test
    public void testChangeMissionStatusReflectsInSummary() {
        String name = "ISS";
        int missionId = service.addMission(name);
        service.changeMissionStatus(missionId, MissionStatus.IN_PROGRESS);

        List<MissionSummary> summaries = service.getSummary();
        assertEquals(MissionStatus.IN_PROGRESS, summaries.get(0).getStatus());
    }
}


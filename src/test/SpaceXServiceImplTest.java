package test;

import static org.junit.Assert.*;

import org.junit.Test;
import service.SpaceXService;
import service.impl.SpaceXServiceImpl;
import model.Rocket;
import model.Mission;
import model.RocketStatus;
import model.MissionStatus;
import model.MissionSummary;
import java.util.List;

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
        Mission mission = new Mission();
        mission.setId(1);
        mission.setStatus(MissionStatus.SCHEDULED);
        service.addMission(mission);
        List<MissionSummary> summaries = service.getSummary();
        assertEquals(1, summaries.size());
        MissionSummary summary = summaries.get(0);
        assertEquals(MissionStatus.SCHEDULED, summary.getStatus());
        assertEquals(0, summary.getAssignedRockets());
    }

    @Test
    public void testAssignRocketToMissionUpdatesSummary() {
        Mission mission = new Mission();
        mission.setId(1);
        mission.setStatus(MissionStatus.SCHEDULED);
        service.addMission(mission);
        Rocket rocket = new Rocket();
        rocket.setId(10);
        rocket.setStatus(RocketStatus.ON_GROUND);
        service.addRocket(rocket);
        service.assignRocketToMission(rocket, mission);

        List<MissionSummary> summaries = service.getSummary();
        assertEquals(1, summaries.get(0).getAssignedRockets());
    }

    @Test
    public void testChangeMissionStatusReflectsInSummary() {
        Mission mission = new Mission();
        mission.setId(2);
        mission.setStatus(MissionStatus.SCHEDULED);
        service.addMission(mission);
        service.changeMissionStatus(mission, MissionStatus.IN_PROGRESS);

        List<MissionSummary> summaries = service.getSummary();
        assertEquals(MissionStatus.IN_PROGRESS, summaries.get(0).getStatus());
    }
}

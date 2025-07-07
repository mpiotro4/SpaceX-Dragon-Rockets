package test;

import static org.junit.Assert.*;

import org.junit.Test;
import service.SpaceXService;
import model.MissionStatus;
import model.MissionSummary;
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
    public void testAssignRocketToMissionUpdatesSummary() {
        String name = "Moon";
        int missionId = service.addMission(name);
        int rocketId = service.addRocket();
        service.assignRocketToMission(rocketId, missionId);

        List<MissionSummary> summaries = service.getSummary();
        assertEquals(1, summaries.get(0).getAssignedRockets());
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


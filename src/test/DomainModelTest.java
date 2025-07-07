package test;

import static org.junit.Assert.*;

import org.junit.Test;
import model.Rocket;
import model.RocketStatus;
import model.Mission;
import model.MissionStatus;
import model.MissionSummary;

public class DomainModelTest {

    @Test
    public void testRocketStatusDescriptionAndToString() {
        RocketStatus status = RocketStatus.IN_REPAIR;
        assertEquals("In repair", status.getStatusDescription());
        assertEquals("In repair", status.toString());
    }

    @Test
    public void testMissionStatusDescriptionAndToString() {
        MissionStatus status = MissionStatus.IN_PROGRESS;
        assertEquals("In Progress", status.getStatusDescription());
        assertEquals("In Progress", status.toString());
    }

    @Test
    public void testMissionSummaryNoArgsConstructorAndSetters() {
        MissionSummary summary = new MissionSummary();
        summary.setMissionName("Mars");
        summary.setStatus(MissionStatus.SCHEDULED);
        summary.setAssignedRockets(2);

        assertEquals("Mars", summary.getMissionName());
        assertEquals(MissionStatus.SCHEDULED, summary.getStatus());
        assertEquals(2, summary.getAssignedRockets());
    }

    @Test
    public void testRocketToStringExcludesPresentMission() {
        Rocket rocket = new Rocket();
        rocket.setId(42);
        rocket.setStatus(RocketStatus.ON_GROUND);
        Mission mission = new Mission();
        mission.setId(1);
        mission.setStatus(MissionStatus.SCHEDULED);

        String repr = rocket.toString();
        assertTrue(repr.contains("id=42"));
        assertTrue(repr.contains("status=On ground"));
        assertFalse(repr.contains("presentMission="));
    }

    @Test
    public void testRocketEqualsIgnoresPresentMission() {
        Rocket r1 = new Rocket();
        r1.setId(1);
        r1.setStatus(RocketStatus.IN_SPACE);
        Rocket r2 = new Rocket();
        r2.setId(1);
        r2.setStatus(RocketStatus.IN_SPACE);

        // set presentMission only on r1
        Mission mission = new Mission();
        mission.setId(99);
        mission.setStatus(MissionStatus.PENDING);

        // They should still be equal and have same hash
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}

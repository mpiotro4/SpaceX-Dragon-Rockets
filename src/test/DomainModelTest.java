package test;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;
import model.RocketStatus;
import model.MissionStatus;
import model.MissionSummary;

public class DomainModelTest {

    @Test
    public void testMissionStatusDerive() {
        assertEquals(MissionStatus.PENDING,
            MissionStatus.derive(List.of(RocketStatus.IN_REPAIR), MissionStatus.SCHEDULED));
        assertEquals(MissionStatus.IN_PROGRESS,
            MissionStatus.derive(List.of(RocketStatus.IN_SPACE), MissionStatus.SCHEDULED));
        assertEquals(MissionStatus.SCHEDULED,
            MissionStatus.derive(List.of(RocketStatus.ON_GROUND), MissionStatus.SCHEDULED));
        assertEquals(MissionStatus.ENDED,
            MissionStatus.derive(List.of(RocketStatus.ON_GROUND), MissionStatus.IN_PROGRESS));
    }

    @Test
    public void testRocketStatusDerive() {
        assertEquals(RocketStatus.IN_REPAIR,
            RocketStatus.derive(List.of(MissionStatus.PENDING), RocketStatus.ON_GROUND));
        assertEquals(RocketStatus.IN_SPACE,
            RocketStatus.derive(List.of(MissionStatus.IN_PROGRESS), RocketStatus.ON_GROUND));
        assertEquals(RocketStatus.ON_GROUND,
            RocketStatus.derive(List.of(MissionStatus.SCHEDULED), RocketStatus.ON_GROUND));
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
}

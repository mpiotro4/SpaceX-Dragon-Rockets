package test.repository;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import repository.MissionRepository;
import repository.impl.InMemoryMissionRepository;
import model.Mission;
import model.MissionStatus;

import java.util.List;
import java.util.Optional;

public class MissionRepositoryTest {
    private MissionRepository repo;

    @Before
    public void setUp() {
        repo = new InMemoryMissionRepository();
    }

    @Test
    public void testSaveAndFindById() {
        Mission mission = new Mission();
        mission.setId(1);
        mission.setName("Mars");
        mission.setStatus(MissionStatus.SCHEDULED);
        repo.save(mission);

        Optional<Mission> found = repo.findById(1);
        assertTrue(found.isPresent());
        assertEquals(mission, found.get());
    }

    @Test
    public void testFindAll() {
        Mission m1 = new Mission(); m1.setId(1); m1.setName("A"); m1.setStatus(MissionStatus.SCHEDULED);
        Mission m2 = new Mission(); m2.setId(2); m2.setName("B"); m2.setStatus(MissionStatus.IN_PROGRESS);
        repo.save(m1);
        repo.save(m2);

        List<Mission> all = repo.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(m1));
        assertTrue(all.contains(m2));
    }
}

package test.repository;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import repository.RocketRepository;
import repository.impl.InMemoryRocketRepository;
import model.DragonRocket;
import model.Rocket;
import model.RocketStatus;

import java.util.List;
import java.util.Optional;

public class RocketRepositoryTest {
    private RocketRepository repo;

    @Before
    public void setUp() {
        repo = new InMemoryRocketRepository();
    }

    @Test
    public void testSaveAndFindById() {
        Rocket rocket = new DragonRocket();
        rocket.setId(1);
        rocket.setStatus(RocketStatus.ON_GROUND);
        repo.save(rocket);

        Optional<Rocket> found = repo.findById(1);
        assertTrue(found.isPresent());
        assertEquals(rocket, found.get());
    }

    @Test
    public void testFindAll() {
        DragonRocket r1 = new DragonRocket(); r1.setId(1); r1.setStatus(RocketStatus.ON_GROUND);
        DragonRocket r2 = new DragonRocket(); r2.setId(2); r2.setStatus(RocketStatus.IN_SPACE);
        repo.save(r1);
        repo.save(r2);

        List<Rocket> all = repo.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(r1));
        assertTrue(all.contains(r2));
    }
}
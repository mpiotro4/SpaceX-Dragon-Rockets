package repository.impl;

import repository.CrudRepository;
import model.Mission;

import java.util.*;

public class InMemoryMissionRepository implements CrudRepository<Mission, Integer> {
    private final Map<Integer, Mission> store = new HashMap<>();
    private int nextId = 1;

    @Override
    public Mission save(Mission mission) {
        if (mission.getId() <= 0) {
            mission.setId(nextId++);
        }
        store.put(mission.getId(), mission);
        return mission;
    }

    @Override
    public Optional<Mission> findById(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Mission> findAll() {
        return new ArrayList<>(store.values());
    }
}
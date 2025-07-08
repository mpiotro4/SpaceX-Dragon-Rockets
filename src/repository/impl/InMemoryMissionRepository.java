package repository.impl;

import repository.MissionRepository;
import model.Mission;

import java.util.*;

public class InMemoryMissionRepository implements MissionRepository {
    private final Map<Integer, Mission> store = new HashMap<>();

    @Override
    public Mission save(Mission mission) {
        store.put(mission.getId(), mission);
        return mission;
    }

    @Override
    public Optional<Mission> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Mission> findAll() {
        return new ArrayList<>(store.values());
    }
}

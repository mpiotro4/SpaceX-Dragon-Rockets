package repository.impl;

import repository.RocketRepository;
import model.Rocket;

import java.util.*;

public class InMemoryRocketRepository implements RocketRepository {
    private final Map<Integer, Rocket> store = new HashMap<>();

    @Override
    public Rocket save(Rocket rocket) {
        store.put(rocket.getId(), rocket);
        return rocket;
    }

    @Override
    public Optional<Rocket> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Rocket> findAll() {
        return new ArrayList<>(store.values());
    }
}
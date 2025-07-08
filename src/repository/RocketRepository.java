package repository;

import model.Rocket;
import java.util.List;
import java.util.Optional;

public interface RocketRepository {
    Rocket save(Rocket rocket);
    Optional<Rocket> findById(int id);
    List<Rocket> findAll();
}
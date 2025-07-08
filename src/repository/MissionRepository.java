package repository;

import model.Mission;
import java.util.List;
import java.util.Optional;

public interface MissionRepository {
    Mission save(Mission mission);
    Optional<Mission> findById(int id);
    List<Mission> findAll();
}

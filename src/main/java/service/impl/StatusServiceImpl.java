package service.impl;

import model.Rocket;
import model.Mission;
import model.RocketStatus;
import model.MissionStatus;
import repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import service.StatusService;

public class StatusServiceImpl implements StatusService {
    private final CrudRepository<Rocket, Integer> rocketRepo;
    private final CrudRepository<Mission, Integer> missionRepo;

    public StatusServiceImpl(CrudRepository<Rocket, Integer> rocketRepo,
                         CrudRepository<Mission, Integer> missionRepo) {
        this.rocketRepo = rocketRepo;
        this.missionRepo = missionRepo;
    }

    @Override
    public void updateMissionStatus(int missionId, Map<Integer, List<Integer>> assignments) {
        missionRepo.findById(missionId).ifPresent(m -> {
            List<RocketStatus> statuses = assignments
                .getOrDefault(missionId, List.of())
                .stream()
                .map(rocketRepo::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Rocket::getStatus)
                .collect(Collectors.toList());
            MissionStatus newSt = MissionStatus.derive(statuses, m.getStatus());
            m.setStatus(newSt);
            missionRepo.save(m);
        });
    }

    @Override
    public void updateRocketStatus(int rocketId, Map<Integer, List<Integer>> assignments) {
        rocketRepo.findById(rocketId).ifPresent(r -> {
            List<MissionStatus> mStatuses = assignments.entrySet()
                .stream()
                .filter(e -> e.getValue().contains(rocketId))
                .map(e -> missionRepo.findById(e.getKey()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Mission::getStatus)
                .collect(Collectors.toList());
            RocketStatus newSt = RocketStatus.derive(mStatuses, r.getStatus());
            r.setStatus(newSt);
            rocketRepo.save(r);
        });
    }

    @Override
    public void updateMissionStatusForRocket(int rocketId, Map<Integer, List<Integer>> assignments) {
        assignments.keySet().forEach(mid -> {
            if (assignments.get(mid).contains(rocketId)) {
                updateMissionStatus(mid, assignments);
            }
        });
    }

    @Override
    public void updateRocketsStatusForMission(int missionId, Map<Integer, List<Integer>> assignments) {
        assignments
            .getOrDefault(missionId, List.of())
            .forEach(rid -> updateRocketStatus(rid, assignments));
    }
}

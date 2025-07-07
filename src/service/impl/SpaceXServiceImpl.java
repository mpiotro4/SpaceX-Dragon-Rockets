package service.impl;

import model.Rocket;
import model.Mission;
import model.MissionSummary;
import model.RocketStatus;
import model.MissionStatus;
import service.SpaceXService;

import java.util.List;

public class SpaceXServiceImpl implements SpaceXService {

    @Override
    public void addRocket(Rocket rocket) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void assignRocketToMission(Rocket rocket, Mission mission) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void assignRocketsToMission(List<Rocket> rockets, Mission mission) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void changeRocketStatus(Rocket rocket, RocketStatus newStatus) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void changeMissionStatus(Mission mission, MissionStatus newStatus) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void addMission(Mission mission) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<MissionSummary> getSummary() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
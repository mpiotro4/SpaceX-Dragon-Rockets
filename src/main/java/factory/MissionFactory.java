package factory;

import model.Mission;

public interface MissionFactory {
    Mission createMission(String name);
}

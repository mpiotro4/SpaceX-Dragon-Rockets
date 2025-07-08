package factory.impl;

import factory.MissionFactory;
import model.DragonMission;
import model.MissionStatus;

public class DragonMissionFactory implements MissionFactory {
    @Override
    public DragonMission createMission(String name) {
        DragonMission m = new DragonMission();
        m.setName(name);
        m.setStatus(MissionStatus.SCHEDULED);
        return m;
    }
}

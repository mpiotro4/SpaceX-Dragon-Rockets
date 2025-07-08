package model.impl;

import lombok.Data;
import model.Mission;
import model.MissionStatus;

@Data
public class DragonMission implements Mission {
    private int id;
    private String name;
    private MissionStatus status;
}
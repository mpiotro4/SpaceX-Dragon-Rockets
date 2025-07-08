package model;

import lombok.Data;

@Data
public class DragonMission implements Mission {
    private int id;
    private String name;
    private MissionStatus status;
}
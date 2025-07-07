package model;

import lombok.Data;

@Data
public class Mission {
    private int id;
    private String name;
    private MissionStatus status;
}

package model;

import lombok.Data;

@Data
public class DragonRocket implements Rocket {
    private int id;
    private RocketStatus status;
}

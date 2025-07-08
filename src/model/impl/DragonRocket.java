package model.impl;

import lombok.Data;
import model.Rocket;
import model.RocketStatus;

@Data
public class DragonRocket implements Rocket {
    private int id;
    private RocketStatus status;
}

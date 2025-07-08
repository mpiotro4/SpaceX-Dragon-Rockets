package service.impl;

import model.DragonRocket;
import model.Rocket;
import service.RocketFactory;

/**
 * Default RocketFactory producing DragonRocket instances.
 */
public class DragonRocketFactory implements RocketFactory {
    @Override
    public Rocket createRocket(int id) {
        DragonRocket rocket = new DragonRocket();
        rocket.setId(id);
        return rocket;
    }
}
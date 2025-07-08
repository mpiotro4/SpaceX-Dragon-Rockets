package factory.impl;

import model.impl.DragonRocket;
import model.Rocket;
import factory.RocketFactory;

/**
 * Default RocketFactory producing DragonRocket instances.
 */
public class DragonRocketFactory implements RocketFactory {
    @Override
    public Rocket createRocket() {
        DragonRocket rocket = new DragonRocket();
        rocket.setId(0);
        return rocket;
    }
}
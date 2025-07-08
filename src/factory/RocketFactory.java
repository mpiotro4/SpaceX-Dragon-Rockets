package factory;

import model.Rocket;

/**
 * Factory for creating Rocket instances.
 */
public interface RocketFactory {
    Rocket createRocket(int id);
}

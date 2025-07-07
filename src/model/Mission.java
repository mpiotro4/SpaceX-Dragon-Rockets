package model;

import java.util.List;
import lombok.Data;

@Data
public class Mission {
    private int id;
    private MissionStatus status;
    private List<Rocket> rockets;
}

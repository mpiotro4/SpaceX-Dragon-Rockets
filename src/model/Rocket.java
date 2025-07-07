package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class Rocket {
    private int id;
    private RocketStatus status;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Mission presentMission;
}

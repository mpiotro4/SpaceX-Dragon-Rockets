package model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MissionSummary {
    private String missionName;
    private MissionStatus status;
    private int assignedRockets;
}

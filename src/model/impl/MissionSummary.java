package model.impl;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.MissionStatus;

@Data
@NoArgsConstructor
public class MissionSummary {
    private String missionName;
    private MissionStatus status;
    private int assignedRockets;
}

package model;

import java.util.List;
import java.util.Objects;

public enum RocketStatus {
    IN_REPAIR {
        @Override
        boolean matches(List<MissionStatus> missionStatuses, RocketStatus prev) {
            return missionStatuses.contains(MissionStatus.PENDING);
        }
    },
    IN_SPACE {
        @Override
        boolean matches(List<MissionStatus> missionStatuses, RocketStatus prev) {
            return missionStatuses.contains(MissionStatus.IN_PROGRESS)
                   && !missionStatuses.contains(MissionStatus.PENDING);
        }
    },
    ON_GROUND {
        @Override
        boolean matches(List<MissionStatus> missionStatuses, RocketStatus prev) {
            return missionStatuses.stream()
                    .allMatch(ms -> ms == MissionStatus.SCHEDULED || ms == MissionStatus.ENDED);
        }
    };

    abstract boolean matches(List<MissionStatus> missionStatuses, RocketStatus prev);

    public static RocketStatus derive(List<MissionStatus> missionStatuses, RocketStatus prev) {
        for (RocketStatus st : values()) {
            if (st.matches(Objects.requireNonNull(missionStatuses), prev)) {
                return st;
            }
        }
        return prev;
    }
}

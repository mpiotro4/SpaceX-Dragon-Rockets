package model;

import java.util.List;

public enum MissionStatus {
    PENDING {
        @Override
        boolean matches(List<RocketStatus> rockets, MissionStatus prev) {
            return rockets.contains(RocketStatus.IN_REPAIR);
        }
    },
    IN_PROGRESS {
        @Override
        boolean matches(List<RocketStatus> rockets, MissionStatus prev) {
            return !rockets.contains(RocketStatus.IN_REPAIR)
                   && rockets.contains(RocketStatus.IN_SPACE);
        }
    },
    ENDED {
        @Override
        boolean matches(List<RocketStatus> rockets, MissionStatus prev) {
            return prev == IN_PROGRESS
                   && rockets.stream().allMatch(s -> s == RocketStatus.ON_GROUND);
        }
    },
    SCHEDULED {
        @Override
        boolean matches(List<RocketStatus> rockets, MissionStatus prev) {
            return rockets.stream().allMatch(s -> s == RocketStatus.ON_GROUND);
        }
    };

    abstract boolean matches(List<RocketStatus> rockets, MissionStatus prev);

    public static MissionStatus derive(List<RocketStatus> rockets, MissionStatus prev) {
        for (MissionStatus st : values()) {
            if (st.matches(rockets, prev)) {
                return st;
            }
        }
        return prev;
    }
}


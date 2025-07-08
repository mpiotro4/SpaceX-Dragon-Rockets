package service.impl;

import model.Mission;
import model.impl.MissionSummary;
import service.SummaryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InMemorySummaryService implements SummaryService {
    @Override
    public List<MissionSummary> generate(Map<Integer, Mission> missions,
                                         Map<Integer, List<Integer>> missionAssignments) {
        List<MissionSummary> summaries = new ArrayList<>();
        for (Mission mission : missions.values()) {
            int assigned = missionAssignments.getOrDefault(mission.getId(), Collections.emptyList()).size();
            MissionSummary summary = new MissionSummary();
            summary.setMissionName(mission.getName());
            summary.setStatus(mission.getStatus());
            summary.setAssignedRockets(assigned);
            summaries.add(summary);
        }
        return summaries;
    }
}
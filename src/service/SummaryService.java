package service;

import model.Mission;
import model.impl.MissionSummary;

import java.util.List;
import java.util.Map;

public interface SummaryService {
    List<MissionSummary> generate(Map<Integer, Mission> missions,
                                  Map<Integer, List<Integer>> missionAssignments);
}
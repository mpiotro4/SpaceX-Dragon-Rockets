package service;

import model.Mission;
import model.MissionSummary;

import java.util.List;
import java.util.Map;

public interface SummaryService {
    List<MissionSummary> generate(Map<Integer, Mission> missions,
                                  Map<Integer, List<Integer>> missionAssignments);
}
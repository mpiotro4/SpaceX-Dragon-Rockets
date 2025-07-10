package model;

public interface Mission {
    int getId();
    void setId(int id);
    String getName();
    void setName(String name);
    MissionStatus getStatus();
    void setStatus(MissionStatus status);
}
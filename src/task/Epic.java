package task;
import manager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static manager.InMemoryTaskManager.subtasks;

public class Epic extends Task {

    public Epic(String name, String description, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
    }

    public Epic(String name, int id) {
        super(name, id);
    }

    protected Type type = Type.EPIC;
    public ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void setEndTime(LocalDateTime endTime) { //НОВЫЙ
     this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {//НОВЫЙ
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {//НОВЫЙ
       this.duration = duration;
    }

    @Override
    public LocalDateTime getEndTime() {//НОВЫЙ
        return this.endTime;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public Type getType() {
        return type;
    }

    public void addSubtaskId (int subtaskId){
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public String toString(){
        return  "Epic { " + "Название: " + name + "; " +
                "Описание: " + description + "; " +
                "ID: " + id + "; " +
                "Статус: " + status + "}";
    }
}

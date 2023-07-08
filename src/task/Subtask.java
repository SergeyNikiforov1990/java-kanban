package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    public int epicId;
    protected Type type = Type.SUBTASK;

    public Subtask (String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask (String name, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }
    public Subtask (String name, String description, int epicId, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(name, description, duration, startTime, endTime);
        this.epicId = epicId;
    }
    public Subtask (String name,  int epicId, Duration duration, LocalDateTime startTime) {
        super(name, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, int epicId) {
        super(name);
        this.epicId = epicId;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId (int epicId){
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subTask = (Subtask) o;
        return id == subTask.id && Objects.equals(name, subTask.name) && Objects.equals(description, subTask.description) &&
                status == subTask.status && type == subTask.type && epicId == subTask.epicId;
    }


    @Override
    public String toString() {
        return "SubTask { " + "Название: " + name + "; " +
                "Описание: " + description + "; " +
                "ID: " + id + "; " +
                "Статус: " + status + "; " + " Epic ID " + epicId + "}";
    }
}

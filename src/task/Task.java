package task;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task  {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected Type type = Type.TASK;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name) {
        this.name = name;
    }

    public Task(String name, int id) {
        this.name = name;
        this.id = id;
    }


    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime(){ //НОВЫЙ
        return this.startTime;
    }

    public LocalDateTime getEndTime() { //НОВЫЙ
        return startTime.plus(duration);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration(){ //НОВЫЙ
        return this.duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status && type == task.type
                && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime)
                && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, type, duration, startTime, endTime);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;

    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    /* @Override
    public int compareTo(Task o) {
        if (this.startTime == null || this.duration.isZero()) {
            return -1;
        }
        if (this.startTime.isBefore(o.startTime)){
            return -1;
        } else if (this.startTime.equals(o.startTime)) {
            return 0;
        } else return 1;
    }*/
}

package task;
import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtaskId (int subtaskId){
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
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

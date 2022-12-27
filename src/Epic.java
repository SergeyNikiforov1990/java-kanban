import java.util.ArrayList;

public class Epic extends Task{
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    protected final void addSubtaskId (int subtaskId){
        subtaskIds.add(subtaskId);
    }

    protected final void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    protected final void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    protected  final ArrayList<Integer> getSubtaskIds() {
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

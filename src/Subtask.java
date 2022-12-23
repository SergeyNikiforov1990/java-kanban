public class Subtask extends AbstractTask {
    protected int epicId;

    public Subtask(String name, String description, int epicId, Status status) {
        super(name, description);
        this.epicId = epicId;
        this.status = status;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString(){
        return  "SubTask { " + "Название: " + name + "; " +
                "Описание: " + description + "; " +
                "ID: " + id + "; " +
                "Статус: " + status + " Epic ID " + epicId + "}";
    }
}

public class Subtask extends Task {
    protected int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    protected final int getEpicId() {
        return epicId;
    }

    protected final void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask { " + "Название: " + name + "; " +
                "Описание: " + description + "; " +
                "ID: " + id + "; " +
                "Статус: " + status + "; " + " Epic ID " + epicId + "}";
    }
}


public class Task extends AbstractTask {

    public Task(String name, String description, Status status) {
        super(name, description);
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString(){
        return  "Task { " + "Название: " + name + "; "  +
                "Описание: " + description + "; " +
                "ID: " + id + "; " +
                "Статус: " + status + "}";
    }


}

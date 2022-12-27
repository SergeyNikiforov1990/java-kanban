
public class Task  {
    protected  String name;
    protected String description;
    protected int id;
    protected Status status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected final void setId(int id) {
        this.id = id;
    }

    protected final String getName() {
        return name;
    }

    protected final void setName(String name) {
        this.name = name;
    }

    protected final String getDescription() {
        return description;
    }

    protected final void setDescription(String description) {
        this.description = description;
    }

    protected final Integer getId() {
        return id;
    }

    protected final Status getStatus() {
        return status;
    }

    protected final void setStatus(Status status) {
        this.status = status;

    }

    @Override
    public String toString(){
        return  "Task { " + "Название: " + name + "; "  +
                "Описание: " + description + "; " +
                "ID: " + id + "; " +
                "Статус: " + status + "}";
    }
}

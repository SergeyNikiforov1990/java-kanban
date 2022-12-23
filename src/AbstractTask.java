public class AbstractTask { // Абстрактный класс, от него все наследуются
    protected String name;
    protected String description;
    protected int id;//2
    protected Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public AbstractTask(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

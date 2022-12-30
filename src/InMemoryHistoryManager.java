import java.util.ArrayList;
import java.util.List;
public class InMemoryHistoryManager implements HistoryManager {

    List<Task> history = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        if (history.size()>10){
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public void getHistory() {
        for (int i = 0; i < history.size(); i++){
            System.out.println(history.get(i));
        }
    }

    public InMemoryHistoryManager getDefaultHistory (){
        return new InMemoryHistoryManager();
    }
}

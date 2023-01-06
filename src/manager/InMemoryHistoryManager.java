package manager;
import task.*;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    LinkedList<Task> history = new LinkedList<>(); // с прицелом на будущее решил использовать LinkedList
    private static final int HISTORY_SIZE = 10;

    @Override
    public void add(Task task) {
        history.addLast(task);
        if (history.size() > HISTORY_SIZE) { //Если размер спика больше 10, удаляем первый элемент
            history.removeFirst(); //Удаляем первый элемент списка (unlinkFirst(node))
        }
    }

    @Override
    public List<Task> getHistory() {
         return history;
    }
}

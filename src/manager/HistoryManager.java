package manager;
import task.*;
import java.util.List;

public interface HistoryManager {
    void add(Task task); // помечает задачи как просмотренные
    List<Task> getHistory(); // возвращает их список
}

public interface HistoryManager {
    void add(Task task); // помечает задачи как просмотренные
    void getHistory(); // возвращает их список
}

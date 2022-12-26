import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int nextId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public int addTask(Task task) {
        task.setId(nextId);
        task.setStatus(Status.NEW);
        nextId++;
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public int addEpic(Epic epic) {
        epic.setId(nextId);
        epic.setStatus(Status.NEW);
        nextId++;
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public int addSubtask(Subtask subtask) { //
        int subtaskId = nextId;
        subtask.setStatus(Status.NEW);
        subtask.setId(subtaskId);  // добавляет ID субтаску
        subtasks.put(subtaskId, subtask); // кладем в хешмап subtask
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.addSubtaskId(subtaskId); // кладем в ArrayList subtaskIds Id субтаска
        updateEpicStatus(subtask);
        nextId++;
        return subtask.getId();
    }

    public ArrayList<Task> getAllTasks() { // вывод всех задач
        ArrayList<Task> allTasks = new ArrayList<>();
        for (int id : tasks.keySet()){
            allTasks.add(tasks.get(id));
        }
        for (int id : subtasks.keySet()){
            allTasks.add(subtasks.get(id));
        }
        for (int id : epics.keySet()){
            allTasks.add(epics.get(id));
        } return allTasks;
    }

    /*public ArrayList<Task> getAllTasks(){   //Либо разделяем на 3 отдельных метода, но такое решение мне кажется нелогичным
        return new ArrayList(tasks.values());
    }

    public ArrayList<Task> getAllEpics(){
        return new ArrayList(epics.values());
    }

    public ArrayList<Task> getAllSubtasks(){
        return new ArrayList(subtasks.values());
    }*/

    public void deleteAllTasks() { // удаления всех задач
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(int id){ //вывод задачи по ID
            if (tasks.containsKey(id)) {
                return tasks.get(id);
            }
            else if (epics.containsKey(id)) {
                return epics.get(id);
            }
            else if (subtasks.containsKey(id)) {
                return subtasks.get(id);
            }
            else {
                System.out.println("Задач с таким ID не существует");
                return null;
            }
        }

    public void deleteById(int id) { //  удаление по ID. Работает ли? (доработать удаление из ArrayList subtaskIds)
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (int subtaskId : epic.getSubtaskIds()) {
                if (subtasks.containsKey(subtaskId)) {
                    subtasks.remove(subtaskId);
                }
            }
            epics.remove(id);
        }
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            deleteSubtask(subtask);
        }
    }

    public ArrayList<Subtask> getAllEpicSubtasks(int id) { // получение Subtask каждого Epic
        Epic epic = epics.get(id);
        ArrayList <Subtask> epicSubtasks = new ArrayList<>();
        //System.out.println(epic.getSubtaskIds());
        for (int subtaskId : epic.getSubtaskIds()) {
            if (subtasks.containsKey(subtaskId)) {
                epicSubtasks.add(subtasks.get(subtaskId));
            }
        } return epicSubtasks;
    }

    public void updateTask(Task task) { // Обновление. Новая версия объекта с верным ID передается в виде парамента
        if (tasks.containsKey(task.getId())) {
            tasks.remove(task.getId());
            addTask(task);
        }
    }

    public void updateEpic(Epic epic) { // при обновлении эпика перезаписываем ему ArrayList c subtaskIds
        ArrayList <Integer> subtaskIds = new ArrayList<>();
        if (epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            epic.subtaskIds.addAll(oldEpic.getSubtaskIds());
            epics.remove(epic.getId());
            addEpic(epic);
        }
    }

    public void updateSubtask(Subtask subtask) { // при обновлении сабтаска обновляем статус эпика через метод deleteSubtask
        if (subtasks.containsKey(subtask.getId())) {
            deleteSubtask(subtasks.get(subtask.getId()));
            subtasks.remove(subtask.getId());
            addSubtask(subtask);
        }
    }

    private void updateEpicStatus (Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        Status epicNewStatus = null;
        for (Integer epicSubtaskID : epic.getSubtaskIds()) {  // проходим циклом for по ArrayList subtaskIds в классе Epic
            if (subtasks.containsKey(epicSubtaskID)) {  //сравниваем ключи ArrayList epicIds и HashMap subtasks
                Status status = subtasks.get(epicSubtaskID).getStatus(); // получили статус subtask'a
                if (status != Status.NEW && status != Status.DONE) {
                    epicNewStatus = Status.InProgress;
                } else if (status == Status.DONE) {
                    if (epicNewStatus == null)
                        epicNewStatus = Status.DONE;
                    else if (epicNewStatus != Status.DONE) {
                        epicNewStatus = Status.InProgress;
                    }
                } else if (status == Status.NEW) {
                    if (epicNewStatus == null)
                        epicNewStatus = Status.NEW;
                    else if (epicNewStatus != Status.NEW)
                        epicNewStatus = Status.InProgress;
                }
            }
            if (epicNewStatus == null)
                epic.setStatus(Status.NEW);
            else
                epic.setStatus(epicNewStatus);
        }

    }

    private void deleteSubtask(Subtask subtask) { // при удалении сабтаска обновляем статус эпика
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtaskId(subtask.getId());
        updateEpicStatus(subtask);
    }
}

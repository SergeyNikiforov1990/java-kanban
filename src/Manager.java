import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int nextId = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    protected final int addTask(Task task) {
        task.setId(nextId);
        task.setStatus(Status.NEW);
        nextId++;
        tasks.put(task.getId(), task);
        return task.getId();
    }

    protected final int addEpic(Epic epic) {
        epic.setId(nextId);
        epic.setStatus(Status.NEW);
        nextId++;
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    protected final int addSubtask(Subtask subtask) { //
        //int subtaskId = nextId;
        if (subtask.getStatus() == null || subtask.getStatus() == Status.NEW){
        subtask.setStatus(Status.NEW);
        }
        if (subtask.getId() == 0 ) {
            subtask.setId(nextId);  // добавляет ID субтаску
        }
        subtasks.put(subtask.getId(), subtask); // кладем в хешмап subtask
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.addSubtaskId(subtask.getId()); // кладем в ArrayList subtaskIds Id субтаска
        updateEpicStatus(subtask);
        nextId++;
        return subtask.getId();
    }

    protected final ArrayList<Task> getAllTasks() {   //Либо разделяем на 3 отдельных метода, но такое решение мне кажется нелогичным
        return new ArrayList(tasks.values());
    }

    protected final ArrayList<Task> getAllEpics() {
        return new ArrayList(epics.values());
    }

    protected final ArrayList<Task> getAllSubtasks() {
        return new ArrayList(subtasks.values());
    }

    protected final void deleteAllTasks() { // удаления всех Tasks
        tasks.clear();
    }

    protected final void deleteAllSubtasks() { // удаления всех Subtasks
        for (int id : epics.keySet()){
            Epic epic = epics.get(id);
            epic.setStatus(Status.NEW);
            epic.subtaskIds.clear();
        }
        subtasks.clear();
    }

    protected final void deleteAllEpics() { // удаления всех Epics
        epics.clear();
        subtasks.clear();
    }

    protected final Task getTaskById(int id) { //вывод задачи по ID
        return tasks.get(id);
    }

    protected final Task getSubtaskById(int id) { //вывод задачи по ID
        return subtasks.get(id);
    }

    protected final Task getEpicById(int id) { //вывод задачи по ID
        return epics.get(id);
    }

    protected final void deleteTaskById(int id) { //  удаление по ID.
        tasks.remove(id);
    }

    protected final void deleteSubtaskById(int id) { //  удаление по ID. Удалить из эпика
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.subtaskIds.remove(subtask.getId());
        deleteSubtask(subtask);
    }

    protected final void deleteEpicById(int id) { //  удаление по ID.
        Epic epic = epics.get(id);
        for (int subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    protected final ArrayList<Subtask> getAllEpicSubtasks(int id) { // получение Subtask каждого Epic
        Epic epic = epics.get(id);
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        //System.out.println(epic.getSubtaskIds());
        for (int subtaskId : epic.getSubtaskIds()) {
            if (subtasks.containsKey(subtaskId)) {
                epicSubtasks.add(subtasks.get(subtaskId));
            }
        }
        return epicSubtasks;
    }

    protected final void updateTask(Task task) { // Обновление. Новая версия объекта с верным ID передается в виде парамента
        if (tasks.containsKey(task.getId())) {
            //tasks.remove(task.getId()); // заменить на put или replace
            //addTask(task);
            tasks.put(task.getId(), task);
        }
    }

    protected final void updateEpic(Epic epic) { // при обновлении эпика перезаписываем ему ArrayList c subtaskIds
        if (epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            epic.subtaskIds.addAll(oldEpic.getSubtaskIds());
            //epics.remove(epic.getId());  заменить на put или replace
            //addEpic(epic);
            epics.put(epic.getId(), epic);
        }
    }

    protected final void updateSubtask(Subtask subtask) { //
        if (subtasks.containsKey(subtask.getId())) { //
            deleteSubtask(subtasks.get(subtask.getId())); //
            subtasks.put(subtask.getId(),subtask); // заменить на put или replace
            Epic epic = epics.get(subtask.getEpicId());
            epic.subtaskIds.add(subtask.getId());
            updateEpicStatus(subtask);
        }
    }

    protected final void deleteSubtask(Subtask subtask) { // при удалении сабтаска обновляем статус эпика
        Integer id = subtask.getId();
        subtasks.remove(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtaskId(id);
        updateEpicStatus(subtask);
    }

    protected final void updateEpicStatus(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        Status epicNewStatus = null;
        for (Integer epicSubtaskID : epic.getSubtaskIds()) {
            if (subtasks.containsKey(epicSubtaskID)) {
                Status status = subtasks.get(epicSubtaskID).getStatus();
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
        }
            if (epicNewStatus == null)
                epic.setStatus(Status.NEW);
            else
                epic.setStatus(epicNewStatus);
        }
}

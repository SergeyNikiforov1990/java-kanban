package manager;

import task.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    @Override
    public int addTask(Task task) {
        task.setId(nextId);
        task.setStatus(Status.NEW);
        nextId++;
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(nextId);
        epic.setStatus(Status.NEW);
        nextId++;
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) { //
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

    @Override
    public List<Task> getAllTasks() {   //Либо разделяем на 3 отдельных метода, но такое решение мне кажется нелогичным
        return new ArrayList(tasks.values());
    }

    @Override
    public List<Task> getAllEpics() {
        return new ArrayList(epics.values());
    }

    @Override
    public List<Task> getAllSubtasks() {
        return new ArrayList(subtasks.values());
    }

    @Override
    public void deleteAllTasks() { // удаления всех Tasks
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() { // удаления всех Subtasks
        for (int id : epics.keySet()){
            Epic epic = epics.get(id);
            epic.setStatus(Status.NEW);
            epic.subtaskIds.clear();
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() { // удаления всех Epics
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int id) { //вывод задачи по ID
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Task getSubtaskById(int id) { //вывод задачи по ID
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Task getEpicById(int id) { //вывод задачи по ID
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void deleteTaskById(int id) { //  удаление по ID.
        tasks.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) { //  удаление по ID. Удалить из эпика
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.subtaskIds.remove(subtask.getId());
        deleteSubtask(subtask);
    }

    @Override
    public void deleteEpicById(int id) { //  удаление по ID.
        Epic epic = epics.get(id);
        for (int subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    @Override
    public ArrayList<Subtask> getAllEpicSubtasks(int id) { // получение Subtasks каждого Epic
        Epic epic = epics.get(id);
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (int subtaskId : epic.getSubtaskIds()) {
            if (subtasks.containsKey(subtaskId)) {
                epicSubtasks.add(subtasks.get(subtaskId));
            }
        }
        return epicSubtasks;
    }

    @Override
    public void updateTask(Task task) { // Обновление. Новая версия объекта с верным ID передается в виде парамента
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) { // при обновлении эпика перезаписываем ему ArrayList c subtaskIds
        if (epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            epic.subtaskIds.addAll(oldEpic.getSubtaskIds());
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) { //
        if (subtasks.containsKey(subtask.getId())) {
            deleteSubtask(subtasks.get(subtask.getId()));
            subtasks.put(subtask.getId(),subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.subtaskIds.add(subtask.getId());
            updateEpicStatus(subtask);
        }
    }

    @Override
    public void deleteSubtask(Subtask subtask) { // при удалении сабтаска обновляем статус эпика
        Integer id = subtask.getId();
        subtasks.remove(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtaskId(id);
        updateEpicStatus(subtask);
    }

    @Override
    public void updateEpicStatus(Subtask subtask) {
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

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }
}

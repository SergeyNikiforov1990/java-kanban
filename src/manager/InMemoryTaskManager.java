package manager;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    public static HashMap<Integer, Task> tasks = new HashMap<>();
    public static HashMap<Integer, Epic> epics = new HashMap<>();
    public static HashMap<Integer, Subtask> subtasks = new HashMap<>();
    //protected List<Task> sortedTasks;

    private final Set <Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime() == null)
            return 1;
        if (o2.getStartTime() == null)
            return -1;
        if (o1.getStartTime() == null && o2.getStartTime() == null)
            return Integer.compare(o1.getId(), o2.getId());
        return o1.getStartTime().compareTo(o2.getStartTime());
    });

    private int nextId = 1;

    public static HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public static HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public static HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public List<Task> getPrioritizedTasks() { // кладет ли в sortedTasks в том же порядке?
        ArrayList<Task> sortedTasks = new ArrayList<>(prioritizedTasks);
        return sortedTasks;
    }

    private void isNotIntersection (Task task) {
        boolean isIntersection = false;
        for (Task priorityTask : getPrioritizedTasks()) {
            if (
                    !task.equals(priorityTask)
                            && task.getStartTime() != null
                            && priorityTask.getEndTime() != null
                            && priorityTask.getDuration() != null
                            && (task.getStartTime().equals(priorityTask.getEndTime())
                            || task.getStartTime().isBefore(priorityTask.getEndTime()))
            ) {
                isIntersection = true;
                break;
            }
        }
        if (isIntersection) {
            System.out.println("Пересечение задачи под id = " + task.getId());
            prioritizedTasks.remove(task);
        }
    }



    @Override
    public int addTask(Task task) {
        task.setId(nextId);
        task.setStatus(Status.NEW);
        task.setEndTime(task.getStartTime(), task.getDuration());
        if (!tasks.isEmpty()){
            isNotIntersection(task);
        }
        prioritizedTasks.add(task);
        nextId++;
        tasks.put(task.getId(), task);

        return task.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(nextId);
        epic.setStatus(Status.NEW);
        epic.setType(Type.EPIC);
        nextId++;
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) { //
        subtask.setType(Type.SUBTASK);
        if (subtask.getStatus() == null || subtask.getStatus() == Status.NEW) {
            subtask.setStatus(Status.NEW);
        }
        if (subtask.getId() == 0) {
            subtask.setId(nextId);  // добавляет ID субтаску
        }
        subtasks.put(subtask.getId(), subtask); // кладем в хешмап subtask
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.addSubtaskId(subtask.getId()); // кладем в ArrayList subtaskIds Id субтаска
        updateEpicStatus(subtask);
        if (subtask.getStartTime() == null) {
            System.out.println("no START TIME at Subtask with id: " + subtask.getId());
        } else {
            subtask.setStartTime(subtask.getStartTime()); // 01.05
            subtask.setDuration(subtask.getDuration()); // 01.05
            subtask.setEndTime(subtask.getStartTime(), subtask.getDuration());
            updateEpicStartTime(subtask); //пустое время!
            updateEpicEndTime(subtask); //НОВЫЙ
            updateEpicDuration(subtask); //НОВЫЙ
            isNotIntersection(subtask);
            prioritizedTasks.add(subtask);
        }
        nextId++;
        return subtask.getId();
    }

    public void clear(){
        tasks.clear();
        epics.clear();
        subtasks.clear();
        prioritizedTasks.clear();
    }


    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
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
        historyManager.remove(id);
        Task task = tasks.get(id); //new
        prioritizedTasks.remove(task); //new
        tasks.remove(id);

    }

    @Override
    public void deleteSubtaskById(int id) { //  удаление по ID. Удалить из эпика
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStartTime(subtask);
        updateEpicEndTime(subtask);
        updateEpicDuration(subtask);
        prioritizedTasks.remove(subtask); //new
        epic.subtaskIds.remove(subtask.getId());
        deleteSubtask(subtask);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) { //  удаление по ID.
        Epic epic = epics.get(id);
        for (int subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteAllTasks() { // удаления всех Tasks
        for (int taskIds : tasks.keySet()) {
            historyManager.remove(taskIds);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() { // удаления всех Subtasks
        for (int id : epics.keySet()) {
            Epic epic = epics.get(id);
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setStatus(Status.NEW);
            epic.subtaskIds.clear();
        }
        for (int subtaskIds : subtasks.keySet()) {
            historyManager.remove(subtaskIds);
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() { // долго думал и пришел к выводу что после удаления эпика из истории, его сабтаски из истории не должны удаляться. Не знаю прав ли.
        for (int epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public List<Subtask> getAllEpicSubtasks(int id) { // получение Subtasks каждого Epic
        Epic epic = epics.get(id);
        List<Subtask> epicSubtasks = new ArrayList<>();
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
            isNotIntersection(task);
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
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.subtaskIds.add(subtask.getId());
            isNotIntersection(subtask);
            updateEpicStatus(subtask);
            updateEpicStartTime(subtask);
            updateEpicEndTime(subtask);
            updateEpicDuration (subtask);
        }
    }

    public void updateEpicStartTime(Subtask subtask) { //НОВЫЙ
        Epic epic = epics.get(subtask.getEpicId());
        LocalDateTime minTime = subtask.getStartTime();
        epic.setStartTime(minTime);
        if (!epic.subtaskIds.isEmpty()) {
            for (Integer subId : epic.subtaskIds) {
                Subtask sub = subtasks.get(subId);
                LocalDateTime startTime = sub.getStartTime();
                if (startTime.isBefore(minTime)) {
                    minTime = startTime;
                    epic.setStartTime(minTime);
                }
            }
        }
    }

    public void updateEpicEndTime(Subtask subtask) { //НОВЫЙ
        Epic epic = epics.get(subtask.getEpicId());
        LocalDateTime maxTime = subtask.getEndTime();
        epic.setEndTime(maxTime);
        if (!epic.subtaskIds.isEmpty()) {
            for (Integer subId : epic.subtaskIds) {
                Subtask sub = subtasks.get(subId);
                LocalDateTime endTime = sub.getEndTime();
                if (endTime.isAfter(maxTime)) {
                    maxTime = endTime;
                    epic.setEndTime(maxTime);
                } else epic.setEndTime(endTime);
            }
        }
    }

    public void updateEpicDuration(Subtask subtask) { //НОВЫЙ
        Epic epic = epics.get(subtask.getEpicId());
        Duration duration = Duration.between(epic.getStartTime(), epic.getEndTime());
        epic.setDuration(duration);
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
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void save() {
    }

    @Override
    public void load() {
    }


}

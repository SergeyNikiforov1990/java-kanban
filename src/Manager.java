import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    protected int nextId = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public int addTask(Task task) {
        task.setId(nextId);
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
        subtask.setId(subtaskId);  // добавляет ID субтаску
        subtasks.put(subtaskId, subtask); // кладем в хешмап subtask
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.addSubtaskId(subtaskId); // кладем в ArrayList subtaskIds Id субтаска
        Status newEpicStatus = sincStatus(subtask.getStatus(), epic.getStatus()); //
        epic.setStatus(newEpicStatus);
        nextId++;
        return subtask.getId();
    }

    private Status sincStatus(Status subtaskStatus, Status epicStatus) { // синхронизация статуса Epic после добавления каждого Subtask
        switch (epicStatus) {
            case NEW:
                return subtaskStatus;
            case InProgress:
                return Status.InProgress;
            case DONE:
                return subtaskStatus;
            default:
                return null;
        }
    }

    public String getAllTasks() { // вывод всех задач
        return tasks.toString() + System.lineSeparator() + subtasks.toString() + System.lineSeparator() + epics.toString();
    }

    public void deleteAllTasks() { // удаления всех задач
        tasks.clear();
        epics.clear();
        subtasks.clear();
        for (int id : epics.keySet()) {
            Epic epic = epics.get(id);
            epic.subtaskIds.clear();
        }
    }

    public Object getTaskById(int id) { //вывод задачи по ID
        Object object = new Object();
        if (tasks.containsKey(id)) {
            object = tasks.get(id);
        }
        if (epics.containsKey(id)) {
            object = epics.get(id);
        }
        if (subtasks.containsKey(id)) {
            object = subtasks.get(id);
        }
        return object;
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

    private void deleteSubtask(Subtask subtask) { // при удалении сабтаска обновляем статус эпика
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.removeSubtaskId(subtask.getId());
        Status epicNewStatus = null;

        for (Integer epicSubtaskID : epic.getSubtaskIds()) { // проходим циклом for по ArrayList epicIds в классе Epic
            if (subtasks.containsKey(epicSubtaskID)) { //сравниваем ключи ArrayList epicIds и HashMap subtasks
                Status status = subtasks.get(epicSubtaskID).getStatus();// получили статус subtask'a
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
}

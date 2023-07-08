package manager;

import HTTPServer.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import server.KVTaskClient;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(String url) {
        super(new File("src/historyTasks.csv"));
        int PORT = 8078;
        String URL = url + PORT;
        gson = Managers.getGson();
        client = new KVTaskClient(URL);
    }

    public void load() {
        List<Task> tasks = gson.fromJson(client.load("tasks"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        System.out.println(tasks);
        if (tasks != null) {
            for (Task task : tasks) {
                addTask(task);
            }
        }
        List<Epic> epics = gson.fromJson(client.load("epics"),
                new TypeToken<ArrayList<Epic>>() {
                }.getType());
        if (epics != null) {
            for (Epic epic : epics) {
                addEpic(epic);
            }
        }

        List<Subtask> subtasks = gson.fromJson(client.load("subtasks"),
                new TypeToken<ArrayList<Subtask>>() {
                }.getType());
        if (subtasks != null) {
            for (Subtask subtask : subtasks) {
                addSubtask(subtask);
            }
        }
        List<Task> history = gson.fromJson(client.load("history"),
                new TypeToken<List<Task>>() {
                }.getType());
        if (history != null) {
            for (Task task : history) {
                getHistoryManager().add(task);
            }
        }
    }

    @Override
    public void save() {
        System.out.println("HttpTaskManager save");
        client.put("tasks", gson.toJson(getAllTasks()));
        client.put("epics", gson.toJson(getAllEpics()));
        client.put("subtasks", gson.toJson(getAllSubtasks()));
        client.put("history", gson.toJson(getHistory()));
    }
}

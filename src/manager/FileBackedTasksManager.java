package manager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.io.*;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    public FileBackedTasksManager(FileBackedTasksManager loadFromFile) {
        super();
    }

    public FileBackedTasksManager() {
        super();
    }

    static TaskManager manager = Managers.getDefaultBacked();
    static TaskManager taskManager = Managers.getDefault();
    Formatter formatter = new Formatter();
    static Path path = Path.of("data.csv");

    public static void main(String[] args) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        manager.addTask(new Task("taskName1", "taskDesc1", Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2023, 2, 26, 20,7)));
        manager.addTask(new Task("taskName2", "taskDesc2", Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2023, 2, 26, 20,7)));
        manager.addEpic(new Epic("epicName1", "epicDesc3"));
        manager.addEpic(new Epic("epicName2", "epicDesc4"));
        manager.addSubtask(new Subtask("subName1", "subDesc5", 3, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2023, 2, 26, 20,7)));
        manager.addSubtask(new Subtask("subName2", "subDesc6", 3, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2024, 2, 26, 20,7)));
        manager.addSubtask(new Subtask("subName3", "subDesc7", 4, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2025, 2, 26, 20,7)));
        manager.addSubtask(new Subtask("subName4", "subDesc8", 4, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2026, 2, 26, 20,7)));
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getEpicById(4);
        manager.getSubtaskById(5);
        manager.getSubtaskById(6);
        manager.getSubtaskById(7);
        manager.getSubtaskById(8);
        manager.getSubtaskById(8);

        FileBackedTasksManager loadManager = new FileBackedTasksManager(loadFromFile(path.toFile())); // Загрузка из файла

        if (manager.getAllTasks().size() == loadManager.getAllTasks().size()) {
            for (int i = 0; i < manager.getAllTasks().size(); i++) {
                System.out.println("Task is equals: " + manager.getAllTasks().get(i).equals(loadManager.getAllTasks().get(i)));
            }
            for (int i = 0; i < manager.getAllEpics().size(); i++) {
                System.out.println("Epic is equals: " + manager.getAllEpics().get(i).equals(loadManager.getAllEpics().get(i)));
            }
            for (int i = 0; i < manager.getAllSubtasks().size(); i++) {
                System.out.println("Subtask is equals: " + manager.getAllSubtasks().get(i).equals(loadManager.getAllSubtasks().get(i)));
            }
        }
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), false))) {
            writer.write("id, type, name, status, description, epic\n");
            for (Task task : tasks.values()) {
                writer.append(formatter.toString(task)).write("\n");
            }
            for (Epic task : epics.values()) {
                writer.append(formatter.toString(task)).write("\n");
            }
            for (Subtask task : subtasks.values()) {
                writer.append(formatter.toString(task)).write("\n");
            }
            writer.append("\n");
            writer.write(formatter.historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("No File");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) { // Загрузка из файла (поменять Split)
        FileBackedTasksManager loadedManager = new FileBackedTasksManager();
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                while (!line.isBlank()) {
                    if (line.equals("id, type, name, status, description, epic")) {
                        line = br.readLine();
                        if (line == null) {
                            System.out.println("File is empty");
                            return loadedManager;
                        }
                    }
                    String[] split = line.split(",");
                    if (split[1].equalsIgnoreCase("Subtask")) {
                        taskManager.addSubtask(new Subtask(split[2], split[4], Integer.parseInt(split[5]), Duration.parse(split[6]), LocalDateTime.parse(split[7])));
                    } else if (split[1].equalsIgnoreCase("Epic")) {
                        taskManager.addEpic(new Epic(split[2], split[4]));
                    } else if (split[1].equalsIgnoreCase("Task")) {
                        taskManager.addTask(new Task(split[2], split[4], Duration.parse(split[5]), LocalDateTime.parse(split[6])));
                    }
                    line = br.readLine();
                }
                System.out.println("All tasks are successfully loaded: ");
                line = br.readLine();
                if (line == null) {
                    System.out.println("History is empty");
                    return loadedManager;
                }
                for (Integer taskId : Formatter.historyFromString(line)) {
                    if (subtasks.containsKey(taskId)) {
                        loadedManager.historyManager.add(subtasks.get(taskId)); // проблема
                    } else if (epics.containsKey(taskId)) {
                        loadedManager.historyManager.add(epics.get(taskId));
                    } else {
                        loadedManager.historyManager.add(tasks.get(taskId));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedManager;
    }

    @Override
    public int addTask(Task task) {
        save();
        return super.addTask(task);
    }

    @Override
    public int addEpic(Epic epic) {
        save();
        return super.addEpic(epic);
    }

    @Override
    public int addSubtask(Subtask subtask) {
        save();
        return super.addSubtask(subtask);
    }

    @Override
    public Task getTaskById(int id) {
        save();
        return super.getTaskById(id);
    }

    @Override
    public Task getSubtaskById(int id) {
        save();
        return super.getSubtaskById(id);
    }

    @Override
    public Task getEpicById(int id) {
        save();
        return super.getEpicById(id);
    }

    @Override
    public List<Task> getAllTasks() {
        save();
        return super.getAllTasks();
    }

    @Override
    public List<Task> getAllEpics() {
        save();
        return super.getAllEpics();
    }

    @Override
    public List<Task> getAllSubtasks() {
        save();
        return super.getAllSubtasks();
    }

    @Override
    public void deleteAllTasks() {
        save();
        super.deleteAllTasks();
    }

    @Override
    public void deleteAllSubtasks() {
        save();
        super.deleteAllSubtasks();
    }

    @Override
    public void deleteAllEpics() {
        save();
        super.deleteAllEpics();
    }

    @Override
    public List<Subtask> getAllEpicSubtasks(int id) {
        save();
        return super.getAllEpicSubtasks(id);
    }

    @Override
    public void updateTask(Task task) {
        save();
        super.updateTask(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        save();
        super.updateEpic(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        save();
        super.updateSubtask(subtask);
    }

    @Override
    public void deleteSubtask(Subtask subtask) {
        save();
        super.deleteSubtask(subtask);
    }

    @Override
    public void updateEpicStatus(Subtask subtask) {
        save();
        super.updateEpicStatus(subtask);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}

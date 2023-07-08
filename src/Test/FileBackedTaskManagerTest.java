package Test;

import manager.InputException;
import manager.TaskManager;
import manager.Managers;
import manager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Task;
import task.Subtask;
import java.io.File;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// тесты выдают пересечения потому что в процессе загрузки из файла
// сравнивает загружаемые задачи с добавленными ранее. Я не понимаю как это обойти :-(

public class FileBackedTaskManagerTest {
    private final Path path = Path.of("data.csv"); // поменять на Path
    FileBackedTasksManager managers = Managers.getDefaultBacked();
    Task testTask1 = new Task("taskName1", "taskDesc1", Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2023, 1, 1, 0, 0));
    Task testTask2 = new Task("taskName2", "taskDesc2", Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2023, 2, 1, 0, 0));
    Task testTask3 = new Task("taskName3", "taskDesc3", Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2023, 3, 1, 0, 0));
    Epic epicTest = new Epic("epicName1", "epicDesc3");
    Subtask subtaskTest1 = new Subtask("subName1", "subDesc5", 1, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2025, 4, 1, 0, 0));
    Subtask subtaskTest2 = new Subtask("subName2", "subDesc6", 1, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2025, 5, 1, 0, 0));

    @BeforeEach
    public void clear() {
        managers.clear();
        managers.deleteAllTasks();
        managers.getAllSubtasks();
        managers.deleteAllEpics();
    }

    @AfterEach
    public void deleteFile() {
        try {
            Files.delete(Path.of(path.toUri()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void shouldCorrectlySaveAndLoadFromFile() {
        FileBackedTasksManager managers = new FileBackedTasksManager();
        managers.clear();
        managers.addTask(testTask1);
        managers.addEpic(epicTest);
        FileBackedTasksManager loadManager = Managers.getDefaultBacked();
        loadManager.loadFromFile(new File(path.toUri()));
        System.out.println(loadManager.getAllTasks().toString());
        assertEquals(List.of(testTask1), loadManager.getAllTasks());
        assertEquals(List.of(epicTest), loadManager.getAllEpics());
    }

    @Test
    void shouldDownloadFromFileWithEmptyTaskList() { //Пустой список задач
        FileBackedTasksManager managers = new FileBackedTasksManager();
        managers.addTask(testTask2);
        managers.deleteTaskById(testTask2.getId());
        FileBackedTasksManager loadManager = Managers.getDefaultBacked();
        loadManager.loadFromFile(new File(path.toUri()));
        assertTrue(loadManager.getAllTasks().isEmpty());
    }

    @Test
    void shouldLoadFromFileWithTaskList() {
        FileBackedTasksManager managers = Managers.getDefaultBacked();
        managers.addTask(testTask2);
        FileBackedTasksManager loadManager = Managers.getDefaultBacked();
        loadManager.loadFromFile(new File(path.toUri()));
        assertEquals(
                testTask2.getName(),
                loadManager.getTaskById(testTask2.getId()).getName()
        );
        assertEquals(
                testTask2.getDescription(),
                loadManager.getTaskById(testTask2.getId()).getDescription()
        );
        assertEquals(
                testTask2.getStatus(),
                loadManager.getTaskById(testTask2.getId()).getStatus()
        );
        assertEquals(
                testTask2.getDuration(),
                loadManager.getTaskById(testTask2.getId()).getDuration()
        );
    }

    @Test
    void shouldDownloadFromFileWithEpicWithoutSubtasks() {
        FileBackedTasksManager managers = Managers.getDefaultBacked();
        managers.addEpic(epicTest); // не записывается, так как эпик не существует без сабтаска
        FileBackedTasksManager loadManager = Managers.getDefaultBacked();
        loadManager.loadFromFile(new File(path.toUri()));
        assertTrue(loadManager.getAllEpicSubtasks(epicTest.getId()).isEmpty());
    }

    @Test
    void shouldLoadFromFileEpicWithSubtasks() {
        FileBackedTasksManager managers = Managers.getDefaultBacked();
        managers.addEpic(epicTest);
        managers.addSubtask(subtaskTest1);
        managers.addSubtask(subtaskTest2);
        managers.addSubtask(subtaskTest2); // за засчитается, так как дублирует предыдущий
        FileBackedTasksManager loadManager = Managers.getDefaultBacked();
        loadManager.loadFromFile(new File(path.toUri()));
        assertEquals(2, loadManager.getAllEpicSubtasks(epicTest.getId()).size());
    }

    @Test
    void shouldDownloadFromFileWithEmptyHistoryList() {
        TaskManager taskManager1 = Managers.getDefaultBacked();
        taskManager1.addTask(testTask1);
        FileBackedTasksManager loadManager = Managers.getDefaultBacked();
        loadManager.loadFromFile(new File(path.toUri()));
        assertEquals(0, loadManager.getHistory().size());
    }

    @Test
    void shouldDownloadFromFileWithHistoryList() {
        TaskManager taskManager1 = Managers.getDefaultBacked();
        taskManager1.addTask(testTask1);
        taskManager1.addTask(testTask2);
        taskManager1.addTask(testTask3);
        taskManager1.addEpic(epicTest);
        subtaskTest1.setEpicId(epicTest.getId());
        subtaskTest2.setEpicId(epicTest.getId());
        taskManager1.addSubtask(subtaskTest1);
        taskManager1.addSubtask(subtaskTest2);

        taskManager1.getTaskById(testTask1.getId());
        taskManager1.getTaskById(testTask2.getId());
        taskManager1.getTaskById(testTask3.getId());
        taskManager1.getEpicById(epicTest.getId());
        taskManager1.getSubtaskById(subtaskTest1.getId());
        taskManager1.getSubtaskById(subtaskTest2.getId());
        taskManager1.getSubtaskById(subtaskTest2.getId()); // не запишется в историю

        System.out.println("Список истории из первого  менеджера: " + taskManager1.getHistory());
        TaskManager loadManager = FileBackedTasksManager.loadFromFile(new File(path.toUri()));

        List<Task> hist = loadManager.getHistory();
        System.out.println("Список истории из второго менеджера: " + hist);
        assertEquals(6, hist.size());
    }
}


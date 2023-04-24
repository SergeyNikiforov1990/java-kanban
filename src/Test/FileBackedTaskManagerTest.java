package Test;
import manager.TaskManager;
import manager.Managers;
import manager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Task;
import task.Subtask;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final String path = "data.csv";

    //private final Managers managers = new Managers();
    Task testTask1;
    Task testTask2;
    Task testTask3;
    Epic epicTest;
    Subtask subtaskTest1;
    Subtask subtaskTest2;


    public FileBackedTaskManagerTest() {
        super(new FileBackedTasksManager());
    }

    @BeforeEach
    void initTasks() {
        testTask1 = new Task("taskName1", "taskDesc1", Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2023, 1, 1, 20, 0));
        testTask2 = new Task("taskName2", "taskDesc2", Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2023, 2, 1, 20, 0));
        testTask3 = new Task("taskName3", "taskDesc3", Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2023, 3, 1, 20, 0));
        epicTest = new Epic("epicName1", "epicDesc3");
        subtaskTest1 = new Subtask("subName1", "subDesc5", 3, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2025, 1, 1, 20, 0));
        subtaskTest2 = new Subtask("subName1", "subDesc5", 3, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2025, 1, 1, 20, 0));
    }

    @AfterEach
    public void deleteFile() {
        try {
            Files.delete(Path.of(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void shouldCorrectlySaveAndLoadFromFile() {
        TaskManager taskManager = Managers.getDefaultBacked();
        taskManager.addTask(testTask1);
        taskManager.addEpic(epicTest);
        ((FileBackedTasksManager) taskManager).loadFromFile(Path.of(path).toFile()); // это место вызывает вопросы
        assertEquals(List.of(testTask1), taskManager.getAllTasks());
        assertEquals(List.of(epicTest), taskManager.getAllEpics());
    }

    @Test
    void shouldDownloadFromFileWithEmptyTaskList() {            //Пустой список задач
        TaskManager taskManager1 = Managers.getDefaultBacked();
        taskManager1.addTask(testTask1);
        taskManager1.deleteTaskById(testTask1.getId());
        TaskManager taskManager2 = Managers.getDefaultBacked();
        ((FileBackedTasksManager) taskManager2).loadFromFile(Path.of(path).toFile());
        assertTrue(taskManager2.getAllTasks().isEmpty());
    }

    @Test
    void shouldLoadFromFileWithTaskList() {
        TaskManager firstManager = Managers.getDefaultBacked();
        firstManager.addTask(testTask1);
        TaskManager secondManager = Managers.getDefaultBacked();
        FileBackedTasksManager.loadFromFile(Path.of(path).toFile());
        assertEquals(
                testTask1.getName(),
                secondManager.getTaskById(testTask1.getId()).getName()
        );
        assertEquals(
                testTask1.getId().toString(),
                secondManager.getTaskById(testTask1.getId()).getDescription()
        );
        assertEquals(
                testTask1.getStatus(),
                secondManager.getTaskById(testTask1.getId()).getStatus()
        );
        assertEquals(
                testTask1.getDuration(),
                secondManager.getTaskById(testTask1.getId()).getDuration()
        );
    }

    @Test
    void shouldDownloadFromFileWithEpicWithoutSubtasks() {
        TaskManager taskManager1 = Managers.getDefaultBacked();
        taskManager1.addEpic(epicTest);
        FileBackedTasksManager taskManager2 = Managers.getDefaultBacked();
        ((FileBackedTasksManager) taskManager2).loadFromFile(Path.of(path).toFile());
        assertTrue(taskManager2.getAllEpicSubtasks(epicTest.getId()).isEmpty());
    }

    @Test
    void shouldLoadFromFileEpicWithSubtasks() {
        TaskManager taskManager1 = Managers.getDefaultBacked();
        taskManager1.addEpic(epicTest);
        taskManager1.addSubtask(subtaskTest1);
        taskManager1.addSubtask(subtaskTest2);
        taskManager1.addSubtask(subtaskTest2);
        TaskManager taskManager2 = Managers.getDefaultBacked();
        ((FileBackedTasksManager) taskManager2).loadFromFile(Path.of(path).toFile());
        assertEquals(2, taskManager2.getAllEpicSubtasks(epicTest.getId()).size());
    }

    @Test
    void shouldDownloadFromFileWithEmptyHistoryList() {
        TaskManager taskManager1 = Managers.getDefaultBacked();
        taskManager1.addTask(testTask1);
        TaskManager taskManager2 = Managers.getDefaultBacked();
        ((FileBackedTasksManager) taskManager2).loadFromFile(Path.of(path).toFile());
        assertNull((taskManager2).getHistory());
    }

    @Test
    void shouldDownloadFromFileWithHistoryList() {
        TaskManager taskManager1 = Managers.getDefaultBacked();
        taskManager1.addTask(testTask1);
        taskManager1.addTask(testTask2);
        taskManager1.addTask(testTask3);
        taskManager1.addEpic(epicTest);
        taskManager1.addSubtask(subtaskTest1);
        taskManager1.addSubtask(subtaskTest2);

        taskManager1.getTaskById(testTask1.getId());
        taskManager1.getTaskById(testTask2.getId());
        taskManager1.getTaskById(testTask3.getId());
        taskManager1.getEpicById(epicTest.getId());
        taskManager1.getSubtaskById(subtaskTest1.getId());
        taskManager1.getSubtaskById(subtaskTest2.getId());

        System.out.println(taskManager1.getHistory());

        TaskManager taskManager2 = Managers.getDefaultBacked();
        ((FileBackedTasksManager) taskManager2).loadFromFile(Path.of(path).toFile());
        List<Task> hist = taskManager2.getHistory();
        System.out.println("Список истории со второге менеджера: " + hist);
        assertEquals(6, hist.size());
    }
}


package Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import HTTPServer.KVServer;
import manager.HttpTaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    static KVServer kvServer;
    private String url;

    public HttpTaskManagerTest(HttpTaskManager object) {
        super(object);
    }


    @BeforeAll
    static void globalSetUp() {
        kvServer = new KVServer();
        kvServer.start();
    }

    @BeforeEach
    void setUp() {
        url = "http://localhost:";
        taskManager = new HttpTaskManager(url);
        taskManager.deleteAllTasks();
    }

    @Test
    public void shouldSaveAndLoad() {
        int taskId1 = taskManager.addTask(new Task("TaskName1", "TaskDesc1"));
        int taskId2 = taskManager.addTask(new Task("TaskName1", "TaskDesc1"));
        int epicId = taskManager.addEpic(new Epic("EpicName", "EpicDesc"));
        taskManager.addSubtask(new Subtask("SubName1", "Subdesc1",1));
        taskManager.getEpicById(epicId);
        taskManager.getTaskById(taskId1);
        taskManager.getTaskById(taskId2);
        HttpTaskManager newManager = new HttpTaskManager(url);
        newManager.load();
        assertEquals(taskManager.getHistory(), newManager.getHistory());
        assertEquals(taskManager.getAllEpics(), newManager.getEpics());
        assertEquals(taskManager.getAllSubtasks(), newManager.getSubtasks());
        assertEquals(taskManager.getAllTasks(), newManager.getTasks());
        kvServer.stop();
    }
}
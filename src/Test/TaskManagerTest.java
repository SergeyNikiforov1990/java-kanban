package Test;

import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

abstract public class TaskManagerTest<T extends TaskManager> {
    protected final T object;
    TaskManager taskManager;
    Epic epic;
    Subtask subtask;
    Task task;

    public TaskManagerTest(T object) {
        this.object = object;
    }

    @BeforeEach
    void createTaskManager() {
        taskManager = object;
        epic = new Epic("Epic name", 1);
        subtask = new Subtask("subName1", "subDesc5", 1, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2025, 2, 26, 20, 7));
        task = new Task("taskName1", "taskDesc1", Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2023, 2, 26, 20, 7));
    }

    @Test
    void shouldAddTask() {
        object.addTask(task);
        Task[] expectedTaskList = new Task[]{task};
        Task[] realTaskList = taskManager.getAllTasks().toArray(Task[]::new);
        assertArrayEquals(expectedTaskList, realTaskList, "Tasks not equal");
    }

    @Test
    void shouldAddEpic() {
        object.addEpic(epic);
        Epic[] expectedEpicList = new Epic[]{epic};
        Epic[] realEpicList = object.getAllEpics().toArray(Epic[]::new);
        assertArrayEquals(expectedEpicList, realEpicList, "Epics not equal");
    }

    @Test
    void shouldAddSubtask() {
        object.addEpic(epic);
        object.addSubtask(subtask);
        Subtask[] expectedSubtaskList = new Subtask[]{subtask};
        Subtask[] realSubtaskList = object.getAllEpicSubtasks(epic.getId()).toArray(Subtask[]::new);
        assertArrayEquals(expectedSubtaskList, realSubtaskList, "Subtasks not equal");
    }

    @Test
    void shouldGetSubtaskById() {
        object.addEpic(epic);
        object.addSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtaskById(subtask.getId()), "Subtask not found");
    }

    @Test
    void shouldGetEpicById() {
        object.addEpic(epic);
        assertEquals(epic, taskManager.getEpicById(epic.getId()), "Epic not found");
    }

    @Test
    void shouldGetTaskById() {
        object.addTask(task);
        assertEquals(task, taskManager.getTaskById(task.getId()), "Task not found");
    }

    @Test
    void shouldGetAllTasks() {
        object.addTask(task);
        Task[] expectedTaskList = new Task[]{task};
        Task[] realTaskList = taskManager.getAllTasks().toArray(Task[]::new);
        assertArrayEquals(expectedTaskList, realTaskList, "Task arrays not equals");
    }

    @Test
    void shouldGetAllEpics() {
        object.addEpic(epic);
        Task[] expectedEpicList = new Epic[]{epic};
        Task[] realEpicList = taskManager.getAllEpics().toArray(Epic[]::new);
        assertArrayEquals(expectedEpicList, realEpicList, "Epic arrays not equals");
    }

    @Test
    void shouldGetAllSubtasks() {
        object.addEpic(epic);
        object.addSubtask(subtask);
        Subtask[] expectedSubtaskList = new Subtask[]{subtask};
        Subtask[] realSubtaskList = taskManager.getAllSubtasks().toArray(Subtask[]::new);
        assertArrayEquals(expectedSubtaskList, realSubtaskList, "Subtask arrays not equals 1");
    }

    @Test
    void shouldGetAllEpicSubtasks() {
        object.addEpic(epic);
        object.addSubtask(subtask);
        Subtask[] expectedSubtaskList = new Subtask[]{subtask};
        Subtask[] realSubtaskList = taskManager.getAllEpicSubtasks(epic.getId()).toArray(Subtask[]::new);
        assertArrayEquals(expectedSubtaskList, realSubtaskList, "Subtask arrays not equals 2");
    }

    @Test
    void shouldDeleteTaskById() {
        object.addTask(task);
        object.deleteTaskById(task.getId());
        assertTrue(taskManager.getAllTasks().isEmpty(), "The task was not deleted");
    }

    @Test
    void shouldDeleteEpicById() {
        object.addEpic(epic);
        object.addSubtask(subtask);
        object.deleteEpicById(epic.getId());
        assertTrue(taskManager.getAllEpics().isEmpty(), "The epic was not deleted");
    }

    @Test
    void shouldDeleteSubtask() {
        object.addEpic(epic);
        object.addSubtask(subtask);
        object.deleteSubtaskById(subtask.getId());
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Subtask was not deleted");
    }

    @Test
    void shouldRemoveAllSimpleTasks() {
        object.addTask(task);
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Tasks have not been deleted");
    }

    @Test
    void shouldRemoveAllSubtasks() {
        object.addEpic(epic);
        object.addSubtask(subtask);
        object.deleteAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Subtasks have not been deleted");
    }

    @Test
    void shouldRemoveAllEpics() {
        object.addEpic(epic);
        object.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Epics have not been deleted");
    }
}
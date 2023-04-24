package Test;

import manager.InMemoryTaskManager;
import manager.InputException;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private TaskManager taskManager1 = new InMemoryTaskManager();
    private final LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 0, 0);
    Epic epic1;
    Subtask subtask1;
    Subtask subtask2;
    Task task;

    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    void createEpicAndSubtasks() {
        epic1 = new Epic("TestEpic",1);
        subtask1 = new Subtask("TestSubtask1",1);
        subtask2 = new Subtask("TestSubtask2",1);
    }
    void createTask() {
        task = new Task("TestTask");
        task.setStartTime(startTime);
        task.setDuration(Duration.of(1, ChronoUnit.HOURS));
    }

    @Test
    void shouldCheckLocalDataTimeOfTask() {
        createTask();
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 0), task.getStartTime());
        assertEquals(Duration.of(1, ChronoUnit.HOURS), task.getDuration());
        assertEquals(LocalDateTime.of(2023, 1, 1, 1, 0), task.getEndTime());
    }

    @Test
    void shouldCheckLocalDataTimeOfSubtask() {
        createEpicAndSubtasks();
        subtask1.setStartTime(startTime);
        subtask1.setDuration(Duration.of(1, ChronoUnit.HOURS));
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 0), subtask1.getStartTime());
        assertEquals(Duration.of(1, ChronoUnit.HOURS), subtask1.getDuration());
        assertEquals(LocalDateTime.of(2023, 1, 1, 1, 0), subtask1.getEndTime());
    }

    //Проверка рассчета времени у эпика
    @Test
    void shouldCheckLocalDataTimeOfEpic() {
        TaskManager taskManager = new InMemoryTaskManager();
        createEpicAndSubtasks();
        subtask1.setStartTime(startTime);
        subtask1.setDuration(Duration.of(1, ChronoUnit.HOURS));
        subtask2.setStartTime(LocalDateTime.of(2023, 1, 1, 2, 0));
        subtask2.setDuration(Duration.of(1, ChronoUnit.HOURS));
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 0), epic1.getStartTime());
        assertEquals(Duration.of(3, ChronoUnit.HOURS), epic1.getDuration());
        assertEquals(LocalDateTime.of(2023, 1, 1, 3, 0), epic1.getEndTime());
    }

    //Когда во второй подзадаче не определены дата старта и продолжительность
    @Test
    void shouldReturnLocalDateTimeWhenOnSecondSubtaskStartTimeAndDurationNotDefined() {
        TaskManager taskManager = new InMemoryTaskManager();
        createEpicAndSubtasks();
        subtask1.setStartTime(startTime);
        subtask1.setDuration(Duration.of(1, ChronoUnit.HOURS));
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 0), epic1.getStartTime());
        assertEquals((Duration.of(1, ChronoUnit.HOURS)), epic1.getDuration());
        assertEquals(LocalDateTime.of(2023, 1, 1, 1, 0), epic1.getEndTime());
    }

    //Проверка времени старта и окончания эпика, когда подзадачи начинаются в разное время
    @Test
    void shouldCheckLocalDataTimeOfEpicWithSubtasksStartAtDifferentTimes() {
        TaskManager taskManager = new InMemoryTaskManager();
        createEpicAndSubtasks();
        subtask1.setStartTime(startTime);
        subtask1.setDuration(Duration.of(24, ChronoUnit.HOURS));
        subtask2.setStartTime(LocalDateTime.of(2023, 1, 2, 1, 0));
        subtask2.setDuration(Duration.of(1, ChronoUnit.HOURS));
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 0), epic1.getStartTime());
        assertEquals(Duration.of(26, ChronoUnit.HOURS), epic1.getDuration());
        assertEquals(LocalDateTime.of(2023, 1, 2, 3, 0), epic1.getEndTime());
    }

    //Проверка на корректность сортировки задач по времени
    @Test
    void shouldReturnSortedListByTime() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("task1");
        task1.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0));
        Task task2 = new Task("task2");
        task2.setStartTime(LocalDateTime.of(2023, 1, 1, 1, 0));
        Task task3 = new Task("task3");
        //task3.setStartTime(LocalDateTime.of(2023, 1, 1, 21, 0));
        Task task4 = new Task("task4");
        task4.setStartTime(LocalDateTime.of(2023, 1, 1, 2, 0));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        Task[] expectedSortedTaskList = new Task[]{task1,task2,task4,task3};
        Task[] realSortedTaskList = ((InMemoryTaskManager)taskManager).getPrioritizedTasks().toArray(Task[]::new);
        assertArrayEquals(expectedSortedTaskList,realSortedTaskList);
    }
    //Проверка пересечений, когда время старта задачи совпадает с предыдущей
    @Test
    void shouldCheckIntersection_IfTheStartDatesOfTheTasksAreTheSame(){
//        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("task1");
        task1.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0));
        task1.setDuration(Duration.of(1, ChronoUnit.HOURS));
        Task task2 = new Task("task2");
        task2.setStartTime(LocalDateTime.of(2023, 1, 1, 2, 0));
        task2.setDuration(Duration.of(1, ChronoUnit.HOURS));
        //Пересечение времени старта с task2
        Task task3 = new Task("task3");
        task3.setStartTime(LocalDateTime.of(2023, 1, 1, 2, 30));
        task3.setDuration(Duration.of(1, ChronoUnit.HOURS));
        Task task4 = new Task("task4");
        task4.setStartTime(LocalDateTime.of(2023, 1, 1, 22, 0));
        task4.setDuration(Duration.of(1, ChronoUnit.HOURS));
        taskManager1.addTask(task1);
        taskManager1.addTask(task2);
        taskManager1.addTask(task3);
        taskManager1.addTask(task4);

        Task[] expectedSortedTaskList = new Task[]{task1,task2,task4,task3};
        // Task[] realSortedTaskList = ((InMemoryTaskManager)taskManager).getPrioritizedTasks().toArray(Task[]::new);
        List<Task> realSortedTaskList =  ((InMemoryTaskManager)taskManager1).getPrioritizedTasks();
        assertArrayEquals(expectedSortedTaskList,realSortedTaskList.toArray(Task[]::new));
        assertNull(realSortedTaskList.get(realSortedTaskList.size()-1).getStartTime());
        assertNull(realSortedTaskList.get(realSortedTaskList.size()-1).getDuration());
    }

    // Проверка пересечений, когда задача начинается во время выполнения предыдущей
    @Test
    void shouldCheckIntersrection_WhenNewTaskStartsAtTimeWhenThePreviousHasNotYetFinished(){
        Task task1 = new Task("task1");
        task1.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0));
        task1.setDuration(Duration.of(1, ChronoUnit.HOURS));
        Task task2 = new Task("task2");
        task2.setStartTime(LocalDateTime.of(2023, 1, 1, 2, 0));
        task2.setDuration(Duration.of(1, ChronoUnit.HOURS));
        //Пересечение времени старта с task2
        Task task3 = new Task("task3");
        task3.setStartTime(LocalDateTime.of(2023, 1, 1, 2, 30));
        task3.setDuration(Duration.of(1, ChronoUnit.HOURS));
        Task task4 = new Task("task4");
        task4.setStartTime(LocalDateTime.of(2023, 1, 1, 5, 0));
        task4.setDuration(Duration.of(1, ChronoUnit.HOURS));
        taskManager1.addTask(task1);
        taskManager1.addTask(task2);
        taskManager1.addTask(task3);
        taskManager1.addTask(task4);
        Task[] expectedSortedTaskList = new Task[]{task1,task2,task4,task3};
        List<Task> realSortedTaskList =  ((InMemoryTaskManager)taskManager1).getPrioritizedTasks();
        assertArrayEquals(expectedSortedTaskList,realSortedTaskList.toArray(Task[]::new));
        assertNull(realSortedTaskList.get(realSortedTaskList.size()-1).getStartTime());
        assertNull(realSortedTaskList.get(realSortedTaskList.size()-1).getDuration());
    }
}




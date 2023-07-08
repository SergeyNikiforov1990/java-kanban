/*import task.*;
import manager.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Main { // объект для тестов

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefaultBacked();
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        manager.addTask(new Task("taskName1", "taskDesc1", Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2023, 2, 26, 20,7)));
        manager.addTask(new Task("taskName2", "taskDesc2"));
        manager.addEpic(new Epic("epicName1", "epicDesc3"));
        manager.addEpic(new Epic("epicName2", "epicDesc4"));
        manager.addSubtask(new Subtask("subName1", "subDesc5", 3, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2025, 2, 26, 20,7)));
        manager.addSubtask(new Subtask("subName2", "subDesc6", 3, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2026, 2, 26, 20,7)));
        manager.addSubtask(new Subtask("subName3", "subDesc7", 4, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2027, 2, 26, 20,7)));
        manager.addSubtask(new Subtask("subName4", "subDesc8", 4, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(2028, 2, 26, 20,7)));
        //TaskManager taskManager = Managers.getDefault();

        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getEpicById(4);
        manager.getSubtaskById(5);
        manager.getSubtaskById(6);
        manager.getSubtaskById(7);
        manager.getSubtaskById(8);
        manager.getSubtaskById(8);

        System.out.println(taskManager.getPrioritizedTasks());



       //System.out.print(taskManager.getHistory());

    }
}*/

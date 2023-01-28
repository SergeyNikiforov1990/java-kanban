import task.*;
import manager.*;

public class Main { // объект для тестов

    public static void main(String[] args) {  // хочу сказать, что ЭТО тз показалось мне самым сложным!

        Task task1 = new Task("taskName1", "taskDesc1");
        Task task2 = new Task("taskName2", "taskDesc2");
        Epic epic1 = new Epic("epicName1", "epicDesc3");
        Epic epic2 = new Epic("epicName2", "epicDesc4");
        Subtask subtask1 = new Subtask("subName1", "subDesc5", 3);
        Subtask subtask2 = new Subtask("subName2", "subDesc6", 3);
        Subtask subtask3 = new Subtask("subName3", "subDesc7", 4);
        Subtask subtask4 = new Subtask("subName4", "subDesc8", 4);

        TaskManager taskManager = Managers.getDefault();

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getSubtaskById(7);
        taskManager.getSubtaskById(8);
        taskManager.getSubtaskById(8);

        System.out.print(taskManager.getHistory());









    }
}

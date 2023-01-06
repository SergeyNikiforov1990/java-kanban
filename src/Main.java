import task.*;
import manager.*;

public class Main { // объект для тестов

    public static void main(String[] args) {
        Task task = new Task("Уборка", "Выполнить полную уборку");//1
        Epic epic = new Epic("Приготовить ужин", "Ужин из 3 блюд");//2
        Epic epic1 = new Epic("Приготовить завтрак", "Завтрак из 3 блюд");//3
        Subtask subtask1 = new Subtask("Приготовить ужин", "Нарезать картошку мелко", 2);//4
        Subtask subtask2 = new Subtask("Приготовить ужин", "Вскипятить воду в кастрюле", 2);//5
        Subtask subtask3 = new Subtask("Приготовить завтрак", "Пожарить яичницу", 3);//6
        Subtask subtask4 = new Subtask("Приготовить завтрак", "Сварить кофе", 3);//7
        TaskManager taskManager = Managers.getDefault();

        taskManager.addTask(task);//1
        taskManager.addEpic(epic);//2
        taskManager.addEpic(epic1);//3
        taskManager.addSubtask(subtask1);//4
        taskManager.addSubtask(subtask2);//5
        taskManager.addSubtask(subtask3);//6
        taskManager.addSubtask(subtask4);//7

        //int sub1 = taskManager.addSubtask(subtask1);// добавил еще субтаск1 и сразу вернул Id
        //Subtask subtask = (Subtask) taskManager.getSubtaskById(sub1);
        //System.out.println(epic);
        //subtask1.setStatus(Status.DONE);
        //taskManager.updateSubtask(subtask1);

        //System.out.println(epic);
        //subtask1.setStatus(Status.DONE);
        //taskManager.updateSubtask(subtask1);
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());

        System.out.print(taskManager.getHistory());

        //System.out.println(epic);
        //System.out.println(taskManager.getAllEpicSubtasks(subtask1.getEpicId()));
        //taskManager.deleteSubtaskById(subtask1.getId());
        //System.out.println(epic);
        //System.out.println(taskManager.getAllEpics());
        //System.out.println(taskManager.getAllSubtasks());







    }
}

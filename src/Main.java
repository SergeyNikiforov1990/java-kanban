/*import java.sql.SQLOutput;
import java.util.Scanner;
public class Main { // объект для тестов

    public static void main(String[] args) {
        Task task = new Task("Уборка", "Выполнить полную уборку", Status.DONE);
        Epic epic = new Epic("Приготовить ужин", "Ужин из 3 блюд");
        Epic epic1 = new Epic("Приготовить завтрак", "Завтрак из 3 блюд");
        Subtask subtask1 = new Subtask("Нарезать картошку", "Нарезать картошку мелко", 2, Status.NEW);
        Subtask subtask2 = new Subtask("Вскипятить воду", "Вскипятить воду в кастрюле", 2, Status.NEW);
        Subtask subtask3 = new Subtask("Яичница", "Пожарить яичницу", 3, Status.NEW);
        Subtask subtask4 = new Subtask("Кофе", "Сварить кофе", 3, Status.NEW);

        Manager manager = new Manager();
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);

        System.out.println(manager.getAllTasks());
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        System.out.println(manager.getAllEpicSubtasks(id));
        //System.out.println(epic.subtaskIds);
        //System.out.println(epic.toString());
        //System.out.println(subtask1.toString());
       // System.out.println(subtask2.toString());
        //сделать меню для проверки удаления по ID
    }
}*/

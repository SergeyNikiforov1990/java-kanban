public class Main { // объект для тестов

    public static void main(String[] args) {
        Task task = new Task("Уборка", "Выполнить полную уборку");//1
        Epic epic = new Epic("Приготовить ужин", "Ужин из 3 блюд");//2
        Epic epic1 = new Epic("Приготовить завтрак", "Завтрак из 3 блюд");//3
        Subtask subtask1 = new Subtask("Приготовить ужин", "Нарезать картошку мелко", 2);//4
        Subtask subtask2 = new Subtask("Приготовить ужин", "Вскипятить воду в кастрюле", 2);//5
        Subtask subtask3 = new Subtask("Приготовить завтрак", "Пожарить яичницу", 3);//6
        Subtask subtask4 = new Subtask("Приготовить завтрак", "Сварить кофе", 3);//7
        TaskManager manager = new InMemoryTaskManager();

        manager.addTask(task);//1
        manager.addEpic(epic);//2
        manager.addEpic(epic1);//3
        manager.addSubtask(subtask1);//4
        manager.addSubtask(subtask2);//5
        manager.addSubtask(subtask3);//6
        manager.addSubtask(subtask4);//7

        //int sub1 = manager.addSubtask(subtask1);// добавил еще субтаск1 и сразу вернул Id
        //Subtask subtask = (Subtask) manager.getSubtaskById(sub1);
        //System.out.println(epic);
        //subtask1.setStatus(Status.DONE);
        //manager.updateSubtask(subtask1);

        //System.out.println(epic);
        //subtask1.setStatus(Status.DONE);
       // manager.updateSubtask(subtask1);
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic1.getId());
        manager.printHistory();

        //System.out.println(epic);
        //System.out.println(manager.getAllEpicSubtasks(subtask1.getEpicId()));
        //manager.deleteSubtaskById(subtask1.getId());
        //System.out.println(epic);
        //System.out.println(manager.getAllEpics());
        //System.out.println(manager.getAllSubtasks());







    }
}

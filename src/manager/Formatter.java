package manager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.Type;

import java.util.ArrayList;
import java.util.List;

public class Formatter {
    String toString(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return subtask.getId() + "," + Type.SUBTASK + "," + subtask.getName() + "," + subtask.getStatus() + ","
                    + subtask.getDescription() + "," + subtask.getEpicId() + "," + subtask.getDuration() + "," + subtask.getStartTime() + "," + subtask.getEndTime();
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            return epic.getId() + "," + Type.EPIC + "," + epic.getName() + "," + epic.getStatus() + ","
                    + epic.getDescription() + "," + epic.getDuration() + "," + epic.getStartTime()+ "," + epic.getEndTime();
        } else if (task != null) {
            return task.getId() + "," + Type.TASK + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + task.getDuration() + "," + task.getStartTime()+ "," + task.getEndTime();
        }
        throw new IllegalStateException("Unexpected value!!!");
    }

     String historyToString(HistoryManager manager) {
        StringBuilder historyOfViews = new StringBuilder();
        if (!manager.getHistory().isEmpty()) {
            List<Task> list = manager.getHistory();
            for (Task task : list) {
                if (historyOfViews.length() != 0) {
                    historyOfViews.append(",").append(task.getId());
                } else {
                    historyOfViews.append(task.getId());
                }
            }
            return historyOfViews.toString();
        } else return " ";
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> list = new ArrayList<>();
        for (String s : value.split(",")) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }
}

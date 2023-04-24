package Test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @BeforeEach
    private void beforeEach() {
        epic = new Epic("epicName1",1);
        subtask1 = new Subtask("subName1",1);
        subtask2 = new Subtask("subName2",1);

    }

    @Test
    public void listOfSubtasksIsEmpty() {
        assertTrue(epic.subtaskIds.isEmpty());

    }

    @Test
    public void allSubtasksMustHaveStatusNew() {
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void allSubtasksMustHaveStatusDone() {
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        manager.updateEpicStatus(subtask1);
        manager.updateEpicStatus(subtask2);
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void allSubtasksWithStatusDoneAndNew() {
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.NEW);
        manager.updateEpicStatus(subtask1);
        manager.updateEpicStatus(subtask2);
        Assertions.assertEquals(Status.InProgress, epic.getStatus());
    }

    @Test
    public void allSubtasksWithStatusInProgress() {
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        subtask1.setStatus(Status.InProgress);
        subtask2.setStatus(Status.InProgress);
        manager.updateEpicStatus(subtask1);
        manager.updateEpicStatus(subtask2);
        Assertions.assertEquals(Status.InProgress, epic.getStatus());
    }

}
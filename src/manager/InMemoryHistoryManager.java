package manager;
import task.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    protected CustomLinkedList historyOfViews = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (historyOfViews.mapOfHistoryViews.containsKey(task.getId())){
            historyOfViews.removeNode(historyOfViews.mapOfHistoryViews.get(task.getId()));
        }
        historyOfViews.linkLast(task);
        }


    @Override
    public List<Task> getHistory() {
         return historyOfViews.getTasks();
    }

    @Override
    public void remove(int id) {
        historyOfViews.removeNode(historyOfViews.mapOfHistoryViews.get(id));
    }

    public class CustomLinkedList {
        protected HashMap<Integer, Node <Task>> mapOfHistoryViews = new HashMap<>();
        public Node<Task> head;
        public Node <Task> tail;
        private int size = 0;

        private void linkLast (Task task){
            Node <Task> newNode = new Node<>(task);
            if (tail != null) {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            } else {
                head = tail = newNode;
            }
            mapOfHistoryViews.put(task.getId(), historyOfViews.tail);
            size ++;
        }

        private ArrayList<Task> getTasks() {
            ArrayList <Task> tmp = new ArrayList<>();
            Node <Task> node = head;
            while (node != null) {
                tmp.add(node.task);
                node = node.next;
            }
            return tmp;
        }

        private void removeNode (Node<Task> deletedNode){
            if(head == null)
                return;
            if (head.equals(deletedNode)){
                if (head != tail){
                    head = head.next;
                    head.prev = null;
                } else {
                    head = tail = null;
                }
                return;
            }
            if (tail.equals(deletedNode)) {
                Node oldTale = tail;
                tail = oldTale.prev;
                tail.next = null;
            } else {
                deletedNode.prev.next = deletedNode.next;
                deletedNode.next.prev = deletedNode.prev;
                deletedNode.next = deletedNode.prev = null;
            }
            mapOfHistoryViews.remove(deletedNode.task.getId());
        }

    }
}

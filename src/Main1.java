import HTTPServer.HttpTaskServer;
import HTTPServer.KVServer;

public class Main1 {
    public static void main(String[] args) {

        try {
            KVServer kvServer = new KVServer();
            HttpTaskServer server = new HttpTaskServer();
            kvServer.start();
            server.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

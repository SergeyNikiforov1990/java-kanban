package HTTPServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.google.gson.Gson;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.io.OutputStream;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private final int PORT = 8080;
    private final Gson gson;
    private final TaskManager taskManager;
    private final HttpServer server;

    // оставил для тестов эндпоинтов
    /*public static void main(String[] args) throws IOException, InterruptedException {
        HttpTaskServer server1 = new HttpTaskServer();
         //server1.start();
    }*/


    public HttpTaskServer() throws IOException, InterruptedException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress("localhost", PORT), 0);
        System.out.println(server);
        taskManager = Managers.getDefault();
        gson = Managers.getGson();
        server.createContext("/tasks/epic/", this::handleEpics);
        server.createContext("/tasks/subtask/", this::handleSubtasks);
        server.createContext("/tasks/task/", this::handleTasks);
        server.createContext("/tasks/subtask/epic/", this::handleEpicSubtasks);
        server.createContext("/tasks/history/", this::handleHistory);
        server.createContext("/tasks/", this::handlePrioritizedTasks);
        //server.start(); // оставил для тестов эндпоинтов
        //System.out.println("Сервер Запущен"); // оставил для тестов эндпоинтов
    }

    private void writeResponse(HttpExchange exchange, String response, int responseCode) throws IOException {
        if (response.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            exchange.sendResponseHeaders(responseCode, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
        exchange.close();
    }

    private int parseIntId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes()); //UTF-???
    }

    public void stop() {
        System.out.println("Сервер на порту " + PORT + " остановлен.");
        server.stop(0);
    }

    public void start() {
        //System.out.println("Запуск сервера, порт " + PORT);
        server.start();
    }

    private void handleTasks(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String method = exchange.getRequestMethod();
            String response = "молодецTask";
            switch (method) {
                case "GET":
                    System.out.println("работает");
                    //writeResponse(exchange, response, 200);
                    if (query == null) {
                        response = gson.toJson(taskManager.getAllTasks());
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getTaskById(id));
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                case "POST":
                    System.out.println("работает");
                    Task task = gson.fromJson(readText(exchange), Task.class);
                    if (taskManager.getAllTasks().contains(task)) {
                        taskManager.updateTask(task); // исправил
                        System.out.println("Задача обновлена.");
                        writeResponse(exchange, response, 200);
                    } else {
                        int id = taskManager.addTask(task); // Исправил
                        response = String.valueOf(id);
                        System.out.println("Задача добавлена.");
                        writeResponse(exchange, response, 200);
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.deleteAllTasks();
                        System.out.println("Задачи удалены.");
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            taskManager.deleteTaskById(id);
                            System.out.println("Задача с id = " + id + " удалена.");
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                default:
                    System.out.println("ложный метод.");
                    writeResponse(exchange, response, 405);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleEpics(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String method = exchange.getRequestMethod();
            String response = "молодецEpic";
            switch (method) {
                case "GET":
                    if (query == null) {
                        response = gson.toJson(taskManager.getAllEpics());
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getEpicById(id));
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    } else {
                        System.out.println("Получен некорректный запрос");
                        writeResponse(exchange, response, 405);
                    }
                    break;
                case "POST":
                    Epic epic = gson.fromJson(readText(exchange), Epic.class);
                    if (taskManager.getAllEpics().contains(epic)) {
                        System.out.println("Этот эпик уже создан, его статус зависит от его подзадач.");
                        response = String.valueOf(epic.getId());
                        writeResponse(exchange, response, 405);
                    } else {
                        int id = taskManager.addEpic(epic);
                        response = String.valueOf(id);
                        System.out.println("Эпик успешно добавлен.");
                        writeResponse(exchange, response, 200);
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.deleteAllTasks();
                        System.out.println("Задачи удалены.");
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            taskManager.deleteEpicById(id);
                            System.out.println("Эпик с id = " + id + " успешно удален.");
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                default:
                    System.out.println("Не подходящий метод.");
                    writeResponse(exchange, response, 405);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }
    private void handleSubtasks(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String method = exchange.getRequestMethod();
            String response = "";
            switch (method) {
                case "GET":
                    if (query == null) {
                        response = gson.toJson(taskManager.getAllSubtasks());
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        System.out.println(id);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getSubtaskById(id));
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                case "POST":
                    Subtask subtask = gson.fromJson(readText(exchange), Subtask.class);
                    if (taskManager.getAllSubtasks().contains(subtask)) {
                        taskManager.updateSubtask(subtask);
                        System.out.println("Подзадача успешно обновлена.");
                        writeResponse(exchange, response, 200);
                    } else {
                        int id = taskManager.addSubtask(subtask);
                        response = String.valueOf(id);
                        System.out.println("Подзадача успешно добавлена.");
                        writeResponse(exchange, response, 200);
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.deleteAllSubtasks();
                        System.out.println("Все задачи удалены.");
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            taskManager.deleteSubtaskById(id);
                            System.out.println("Подзадача с id = " + id + " успешно удалена.");
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                default:
                    System.out.println("Не подходящий метод.");
                    writeResponse(exchange, response, 405);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleEpicSubtasks(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String method = exchange.getRequestMethod();
            String response = "";
            switch (method) {
                case "GET":
                    if (query == null) {
                        System.out.println("Нужно указать id эпика, чтобы получить его подзадачи");
                        writeResponse(exchange, response, 405);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            if (taskManager.getEpicById(id) != null) {
                                response = gson.toJson(taskManager.getAllEpicSubtasks(id));
                                writeResponse(exchange, response, 200);
                            } else {
                                System.out.println("Эпика с id = " + pathId + " не существует.");
                                writeResponse(exchange, response, 405);
                            }
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                default:
                    System.out.println("Не тот метод. Можно только получить subtask эпика методом GET");
                    writeResponse(exchange, response, 405);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleHistory(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            String response = "";
            if ("GET".equals(method)) {
                response = gson.toJson(taskManager.getHistory());
                writeResponse(exchange, response, 200);
            } else {
                System.out.println("/task/history работает только для GET запросов, метод "
                        + method + " не запускается");
                writeResponse(exchange, response, 405);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handlePrioritizedTasks(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            String response = "";
            if ("GET".equals(method)) {
                response = gson.toJson(taskManager.getPrioritizedTasks());
                writeResponse(exchange, response, 200);
            } else {
                System.out.println("/tasks работает только для GET запросов, метод " + method + " не запускается");
                writeResponse(exchange, response, 405);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

}
package HTTPServer;

import exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private  String URL;
    private final HttpClient client;
    private final String token;

    public KVTaskClient(String url) {
        this.URL = url;
        client = HttpClient.newHttpClient();
        token = register(this.URL);
    }

    public String load(String key) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8078" + "/load/" + key + "?API_TOKEN=" + token))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось получить запрос от KVServer. Статус код: "
                        + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException exception) {
            throw new ManagerSaveException("Не получено от KVServer.");
        }
    }

    private String register(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось зарегистрироваться на KVServer. Статус код: "
                        + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException exception) {
            throw new ManagerSaveException("Не удалось зарегистрироваться на KVServer.");
        }
    }

    public void put(String key, String json) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/save/" + key + "?API_TOKEN=" + token))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            System.out.println(response);
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не сохранилось на KVServer. Статус код: " + response.statusCode());
            }
        } catch (IOException | InterruptedException exception) {
            throw new ManagerSaveException("Не сохранилось на KVServer.");
        }
    }
}

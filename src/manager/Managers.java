package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Managers {
    public static TaskManager getDefault() {
        //return new InMemoryTaskManager();
        return new HttpTaskManager("http://localhost:");
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static  FileBackedTasksManager getDefaultBacked(){
        return new FileBackedTasksManager();
    }

    public static Gson getGson (){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.setPrettyPrinting().create();
    }
}

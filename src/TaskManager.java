import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private static final String TASK_FILE = "tasks.txt";

    public static void saveTasks(Map<Integer, String> tasks) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TASK_FILE))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    public static Map<Integer, String> loadTasks() {
        File file = new File(TASK_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<Integer, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            return new HashMap<>();
        }
    }
}

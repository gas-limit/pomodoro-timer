import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        PomodoroTimer timer = new PomodoroTimer();
        Scanner scanner = new Scanner(System.in);
        Map<Integer, String> tasks = TaskManager.loadTasks();
        int taskId = tasks.isEmpty() ? 1 : tasks.keySet().stream().max(Integer::compare).get() + 1;
        String currentTask = "";
        boolean running = true;


        while (running) {
            System.out.println("Choose an option: (1) Add Task, (2) Select Task, (3) Start Work, (4) Start Break, (5) Stop Timer, (6) Print Total Times, (7) Delete Task, (8) Clear Log History, (9) Exit");
            int choice = scanner.nextInt();


            switch (choice) {
                case 1: // Add task
                    System.out.println("Enter task name:");
                    scanner.nextLine();  // Consume newline
                    String taskName = scanner.nextLine();
                    tasks.put(taskId++, taskName);
                    System.out.println("Task added with ID: " + (taskId - 1));
                    TaskManager.saveTasks(tasks); // Save tasks
                    break;
                case 2: // Select task
                    if (tasks.isEmpty()) {
                        System.out.println("No tasks available.");
                        break;
                    }
                    System.out.println("Available Tasks:");
                    tasks.forEach((id, name) -> System.out.println(id + ": " + name));
                    System.out.println("Enter Task ID:");
                    int selectedId = scanner.nextInt();
                    if (tasks.containsKey(selectedId)) {
                        currentTask = tasks.get(selectedId);
                        System.out.println("Selected Task: " + currentTask);
                    } else {
                        System.out.println("Invalid Task ID.");
                    }
                    break;
                case 3: // Start work
                    timer.startWork();
                    break;
                case 4: // Start break
                    if (timer.getCurrentState() == TimerState.WORK) {
                        // Log the current work session before starting break
                        timer.stopTimer();
                        long workDuration = timer.getSessionDuration();
                        SessionLogger.logSession("Work session ended", currentTask, workDuration);
                    }
                    timer.startBreak();
                    break;
                case 5: // Stop timer
                    if (timer.getCurrentState() == TimerState.WORK) {
                        // Log the current work session before starting break
                        timer.stopTimer();
                        long workDuration = timer.getSessionDuration();
                        SessionLogger.logSession("Work session ended", currentTask, workDuration);
                    }
                    if (timer.getCurrentState() == TimerState.SHORT_BREAK || timer.getCurrentState() == TimerState.LONG_BREAK) {
                        timer.stopTimer();
                    }
                    break;
                case 6: // Print total times for a task
                    // Print available tasks
                    if (tasks.isEmpty()) {
                        System.out.println("No tasks available.");
                        break;
                    }
                    System.out.println("Available Tasks:");
                    tasks.forEach((id, name) -> System.out.println(id + ": " + name));
                    // Print total times
                    System.out.println("Enter Task ID:");
                    int taskIdForTotal = scanner.nextInt();
                    if (tasks.containsKey(taskIdForTotal)) {
                        SessionLogger.printTotalTimes(tasks.get(taskIdForTotal));
                    } else {
                        System.out.println("Invalid Task ID.");
                    }
                    break;
                case 7: // Delete task
                    // Print available tasks
                    if (tasks.isEmpty()) {
                        System.out.println("No tasks available.");
                        break;
                    }
                    System.out.println("Available Tasks:");
                    tasks.forEach((id, name) -> System.out.println(id + ": " + name));
                    // Delete task
                    System.out.println("Enter Task ID to delete:");
                    int deleteId = scanner.nextInt();
                    if (tasks.containsKey(deleteId)) {
                        tasks.remove(deleteId);
                        System.out.println("Task " + deleteId + " deleted.");
                        tasks = reassignTaskIDs(tasks); // Reassign IDs to tasks
                        TaskManager.saveTasks(tasks); // Save updated tasks
                    } else {
                        System.out.println("Task ID not found.");
                    }
                    break;
                case 8: // Clear log history
                    SessionLogger.clearLog();
                    break;
                case 9: // Exit
                    running = false;
                    timer.stopTimer();
                    TaskManager.saveTasks(tasks);
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
        System.out.println("Pomodoro Timer exited.");
    }

    private static Map<Integer, String> reassignTaskIDs(Map<Integer, String> originalTasks) {
        int newId = 1;
        Map<Integer, String> reassignedTasks = new HashMap<>();
        for (String task : originalTasks.values()) {
            reassignedTasks.put(newId++, task);
        }
        return reassignedTasks;
    }
}

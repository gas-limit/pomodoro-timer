import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessionLogger {
    private static final String LOG_FILE = "pomodoro_log.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logSession(String sessionType, String taskName, long duration) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = timestamp + " - " + sessionType + " - Task: " + taskName + " - Duration: " + duration + " minutes\n";
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    public static void printTotalTimes(String taskName) {
        long totalDuration = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Task: " + taskName)) {
                    String[] parts = line.split(" - ");
                    if (parts.length > 3) {
                        // Parse the duration part of the log entry
                        String durationPart = parts[3];
                        long duration = Long.parseLong(durationPart.split(": ")[1].split(" ")[0]);
                        totalDuration += duration;
                    }
                }
            }

            System.out.println("Total Time for Task \"" + taskName + "\": " + totalDuration + " minutes");
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
    }

    public static void clearLog() {
        try (FileWriter writer = new FileWriter(LOG_FILE, false)) {
            writer.write(""); // Writing an empty string to clear the file
            System.out.println("Log history cleared.");
        } catch (IOException e) {
            System.err.println("Error clearing log file: " + e.getMessage());
        }
    }
    
}

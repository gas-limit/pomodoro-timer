import java.util.Timer;
import java.util.TimerTask;

public class PomodoroTimer {

    public static final long WORK_DURATION = 25 * 60 * 1000; // 25 minutes
    private static final long SHORT_BREAK_DURATION = 5 * 60 * 1000; // 5 minutes
    private static final long LONG_BREAK_DURATION = 15 * 60 * 1000; // 15 minutes

    private Timer timer;
    private TimerState currentState;
    private int sessionCount = 0;

    public long startTime;
    private long endTime;    

    public PomodoroTimer() {
        this.currentState = TimerState.IDLE;
        this.timer = new Timer();
    }

    public void startWork() {
        startTime = System.currentTimeMillis();
        startTimer(WORK_DURATION, TimerState.WORK);  // Start the timer for a work session
        sessionCount++;
    }

    public void startBreak() {
        startTime = System.currentTimeMillis();

        if (sessionCount % 4 == 0) {
            startTimer(LONG_BREAK_DURATION, TimerState.LONG_BREAK);
        } else {
            startTimer(SHORT_BREAK_DURATION, TimerState.SHORT_BREAK);
        }
    }

    private void startTimer(long duration, TimerState state) {
        // Cancel any existing timer before starting a new one
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        // Initialize a new Timer
        timer = new Timer();
        currentState = state;

        timer.schedule(new TimerTask() {
            long timeLeft = duration;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    timer.cancel();
                    currentState = TimerState.IDLE;
                    System.out.println("Timer finished: " + currentState);
                } else {
                    System.out.println("Time left: " + (timeLeft / 1000) + " seconds");
                    timeLeft -= 1000;
                }
            }
        }, 0, 1000);
    }


    public void stopTimer() {
        endTime = System.currentTimeMillis();
        if (timer != null) {
            timer.cancel(); // Cancel the timer
            timer.purge(); // Remove cancelled tasks from the timer's task queue
        }
        currentState = TimerState.IDLE;
        System.out.println("Timer stopped.");
    }

    public long getSessionDuration() {
        return (endTime - startTime) / 60000; // duration in minutes
    }
    

    public TimerState getCurrentState() {
        return currentState;
    }

    // In PomodoroTimer class

    public long getCurrentDuration() {
        switch (currentState) {
            case WORK:
                return WORK_DURATION;
            case SHORT_BREAK:
                return SHORT_BREAK_DURATION;
            case LONG_BREAK:
                return LONG_BREAK_DURATION;
            default:
                return 0;
        }
    }

}

enum TimerState {
    WORK,
    SHORT_BREAK,
    LONG_BREAK,
    IDLE
}

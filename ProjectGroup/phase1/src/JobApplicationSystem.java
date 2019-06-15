import java.util.Timer;
import java.util.TimerTask;

public class JobApplicationSystem {

    public static void main(String[] args) {
        cyclicalTask();
    }


    /*
    A method which triggers once a day from the time it is started.
     */
    private static void cyclicalTask(){
        TimerTask daily_tasks = new TimerTask() {
            public void run() {
                // Insert any daily methods here:
                // todo CleanUpfunction() here for example
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(daily_tasks, 0, 86400000);
    }
}

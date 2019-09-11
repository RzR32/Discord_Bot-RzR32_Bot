package other;

public class Pause {

    public void pause(Thread thread, int time) {
        LogBack LB = new LogBack();
        try {
            thread.sleep(time);
        } catch (InterruptedException error) {
            LB.log(Thread.currentThread().getName(), error.getMessage(), "error");
        }
    }
}

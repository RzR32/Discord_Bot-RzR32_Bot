package other;

public class Pause {

    public void pause(int time) {
        LogBack LB = new LogBack();
        try {
            Thread.sleep(time);
        } catch (InterruptedException error) {
            LB.log(Thread.currentThread().getName(), error.getMessage(), "error");
        }
    }
}

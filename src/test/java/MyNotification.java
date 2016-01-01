import codefacts.Notification;

/**
 * Created by sohan on 1/1/2016.
 */
public class MyNotification extends Notification {
    public String message = "Notification";

    @Override
    public String toString() {
        return "MyNotification{" +
                "message='" + message + '\'' +
                '}';
    }
}

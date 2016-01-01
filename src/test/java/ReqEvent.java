import codefacts.Event;
import codefacts.Message;

import java.util.Arrays;

/**
 * Created by sohan on 1/1/2016.
 */
public class ReqEvent extends Message {
    private final int id;

    public ReqEvent(int id) {
        this.id = id;
    }

    @Override
    public void onReply(Message event) {
//        System.out.println(event);
    }

    public void onProgress(int... progress) {
//        System.out.println(asList(progress));
    }

    public static String asList(int... arg) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arg.length; i++) {
            builder.append(arg[i]).append(", ");
        }
        return builder.delete(arg.length - 1, arg.length).toString();
    }

    @Override
    public void onFail(Throwable e) {
//        System.out.println("Failed: " + e);
    }

    @Override
    public void onComplete(Message event) {
//        System.out.println("Complete");
    }

    @Override
    public String toString() {
        return "ReqEvent{" +
                "id=" + id +
                '}';
    }
}

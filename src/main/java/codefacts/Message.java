package codefacts;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sohan on 1/1/2016.
 */
public abstract class Message extends Event {
    public static final int SUCCESS = 1;
    public static final int FAILED = 2;
    private int status = 0;

    public void fail(Throwable e) {
        if (status != 0) {
            Logger.getAnonymousLogger().log(Level.INFO, "Already fulfilled, can't fulfill again. Falling to nops" + this);
            return;
        }
        try {
            status = FAILED;
            onFail(e);
        } finally {
            onComplete(this);
        }
    }

    public void reply(Message event) {
        if (status != 0) {
            Logger.getAnonymousLogger().log(Level.INFO, "Already fulfilled, can't fulfill again. . Falling to nops" + this);
            return;
        }
        try {
            status = SUCCESS;
            onReply(event);
        } finally {
            onComplete(event);
        }
    }

    public void progress(int... progress) {
        onProgress(progress);
    }

    public abstract void onReply(Message event);

    public void onProgress(int... progress) {

    }

    public abstract void onFail(Throwable e);

    public void onComplete(Message event) {

    }

    public boolean isError() {
        return status == FAILED;
    }

    public boolean isSuccess() {
        return status == SUCCESS;
    }

    public boolean isComplete() {
        return status != 0;
    }

    @Override
    public String toString() {
        return "Message{" +
                "status=" + (status == SUCCESS ? "SUCCESS" : "FAILED") +
                '}';
    }
}

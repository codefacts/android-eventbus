package codefacts;

import codefacts.impl.EventBusImpl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sohan on 1/1/2016.
 */
public abstract class EventBus {
    private static EventBus defautlInstance;

    public abstract void subscribe(Subscriber subscriber);

    public abstract void send(Message message);

    public abstract void publish(Notification notification);

    public abstract void unsubscribe(Subscriber subscriber);

    public static EventBus getDefault() {
        return getDefault(new DefaultEventBusExceptionHandler());
    }

    public static EventBus getDefault(EventBusExceptionHandler exceptionHandler) {
        return defautlInstance == null ? defautlInstance = new EventBusImpl(exceptionHandler) : defautlInstance;
    }

    private static class DefaultEventBusExceptionHandler implements EventBusExceptionHandler {

        public void onException(Exception e) {
            Logger.getAnonymousLogger().log(Level.INFO, "EventBusExceptionHandler. Exception: " + e);
        }
    }
}

package codefacts.impl;

import codefacts.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sohan on 1/1/2016.
 */
public class EventBusImpl extends EventBus {
    private final Map<Class<?>, CopyOnWriteArraySet<Entry>> map = new ConcurrentHashMap<Class<?>, CopyOnWriteArraySet<Entry>>();
    private final EventBusExceptionHandler exceptionHandler;

    public EventBusImpl(EventBusExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void subscribe(Subscriber subscriber) {
        parse(subscriber);
    }

    public void send(final Message message) {
        dispatchSend(message);
    }

    public void publish(final Notification event) {
        dispatchPublish(event);
    }

    public void unsubscribe(final Subscriber subscriber) {
        Iterator<CopyOnWriteArraySet<Entry>> iterator = map.values().iterator();
        loop:
        {
            for (; iterator.hasNext(); ) {
                Set<Entry> entries = iterator.next();
                Iterator<Entry> entryIterator = entries.iterator();
                if (entryIterator.hasNext()) {
                    Entry entry = entryIterator.next();
                    if (entry.subscriber == subscriber) {
                        iterator.remove();
                        break loop;
                    }
                }
            }
        }
    }

    private void dispatchPublish(final Event event) {
        CopyOnWriteArraySet<Entry> set = map.get(event.getClass());
        if (set == null) {
            return;
        }
        for (Entry entry : set) {
            executePublish(entry, event);
        }
    }

    private void dispatchSend(final Message message) {
        CopyOnWriteArraySet<Entry> set = map.get(message.getClass());
        if (set == null) {
            return;
        }
        Iterator<Entry> iterator = set.iterator();
        if (iterator.hasNext()) {
            Entry entry = iterator.next();
            execute(entry, message);
        }
    }

    private void execute(Entry entry, Message message) {
        try {
            entry.method.invoke(entry.subscriber, message);
        } catch (InvocationTargetException e) {
            message.fail(e.getCause());
        } catch (Exception e) {
            try {
                exceptionHandler.onException(e);
            } catch (Exception e1) {
                Logger.getAnonymousLogger().log(Level.INFO, "Exception when executing EventBusExceptionHandler. Exception: " + e1);
            }
        }
    }

    private void executePublish(Entry entry, Event event) {
        try {
            entry.method.invoke(entry.subscriber, event);
        } catch (Exception e) {
            try {
                exceptionHandler.onException(e);
            } catch (Exception e1) {
                Logger.getAnonymousLogger().log(Level.INFO, "Exception when executing EventBusExceptionHandler. Exception: " + e1);
            }
        }
    }

    private void parse(Subscriber subscriber) {
        Method[] methods = subscriber.getClass().getDeclaredMethods();

        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            //Ensure Method is public
            if (!Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())) {
                //Ensure Method takes only and only one parameter
                // And it's a subtype of Event Interface
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    Class<?> parameterType = parameterTypes[0];
                    if (Event.class.isAssignableFrom(parameterType)) {
                        put(parameterType, new Entry(method, subscriber));
                    }
                }
            }
        }
    }

    private void put(Class<?> aClass, Entry entry) {
        CopyOnWriteArraySet<Entry> set = map.get(aClass);
        if (set == null) {
            set = new CopyOnWriteArraySet<Entry>();
            map.put(aClass, set);
        }
        set.add(entry);
    }

    private static class Entry {
        final Method method;
        final Subscriber subscriber;

        private Entry(Method method, Subscriber subscriber) {
            this.method = method;
            this.subscriber = subscriber;
        }
    }
}

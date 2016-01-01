package codefacts;

/**
 * Created by sohan on 1/1/2016.
 */
public class Event {
    public <R extends Message> R safeCast(Class<R> rClass) {
        if (this.getClass().isAssignableFrom(rClass))
            return rClass.cast(this);
        else return null;
    }

    public <R extends Message> R as(Class<R> rClass) {
        return rClass.cast(this);
    }
}

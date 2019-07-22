package NotificationSystem;

public interface Observer {

    /**
     * The observer receiving something to observe
     *
     * @param obj - the object to be observed
     */
    void update(Object obj);

}

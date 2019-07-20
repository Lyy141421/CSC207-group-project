package NotificationSystem;

import java.util.ArrayList;

public class NotificationManager {
    // === Instance Variables ===
    ArrayList<Notification> notification_list;

    // === Constructors ===
    public void NotificationManager(){
        this.notification_list = new ArrayList<Notification>();
    }

    public void NotificationManager(ArrayList<Notification> list){
        this.notification_list = list;
    }
}

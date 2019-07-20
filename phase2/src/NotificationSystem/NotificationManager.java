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

    // === Other Methods ===

    public void add(Notification notification){this.notification_list.add(notification);}

    public void remove(Notification notification){this.notification_list.remove(notification);}

    public String displayAll(){
        StringBuilder output = new StringBuilder();
        for (Notification notification : this.notification_list){
            output.append(notification.toString());
        }
        return output.toString();
    }
}

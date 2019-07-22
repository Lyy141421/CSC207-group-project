package NotificationSystem;

import java.io.Serializable;
import java.util.ArrayList;

public class NotificationManager implements Serializable {
    // === Instance Variables ===

    private ArrayList<Notification> notification_list;

    // === Constructors ===

    public NotificationManager(){
        this.notification_list = new ArrayList<>();
    }

    // === Getter ===

    public ArrayList<Notification> getNotifications(){
        return this.notification_list;
    }

    // === Other Methods ===

    public void add(Notification notification){this.notification_list.add(notification);}

    public void add(ArrayList<Notification> notification_list){
        for (Notification notification : notification_list){
            this.add(notification);
        }
    }

    public void remove(Notification notification){this.notification_list.remove(notification);}

    public String displayAll(){
        StringBuilder output = new StringBuilder();
        for (Notification notification : this.notification_list){
            output.append(notification.toString());
        }
        return output.toString();
    }
}

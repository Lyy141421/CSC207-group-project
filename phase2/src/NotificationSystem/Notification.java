package NotificationSystem;

import java.io.Serializable;
import java.time.LocalDate;

public class Notification implements Serializable {
    // === Instance variables ===
    // The title of this Notification
    String title;
    // The message of this Notification
    String message;
    // The date on which this Notification was created
    LocalDate date;

    // === Constructors ===

    public Notification(String title, String message) {
        this.date = LocalDate.now();
        this.title = title;
        this.message = message;
    }

    // === Getters ===

    public String getTitle(){
        return this.title;
    }
    public String getMessage(){
        return this.message;
    }
    public LocalDate getDate(){
        return this.date;
    }

    // === Other Methods ===

    public String toString(){
        StringBuilder output = new StringBuilder();
        output.append(this.getDate().toString());
        output.append("\n");
        output.append(this.getTitle());
        output.append("\n");
        output.append(this.getMessage());
        output.append("\n");
        return output.toString();
    }
}

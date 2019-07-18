package ActionListeners.UserActionListeners;

import Main.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchActionListener extends UserActionListener {

    private String searchType;

    public SearchActionListener(User user, String searchType) {
        super(user);
        this.searchType = searchType;
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("Yay!");
    }
}

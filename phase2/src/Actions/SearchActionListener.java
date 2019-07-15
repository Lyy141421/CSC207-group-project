package Actions;

import CompanyStuff.HRCoordinator;
import Main.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchActionListener implements ActionListener {

    private User user;
    private String searchType;

    public SearchActionListener(User user, String searchType) {
        this.user = user;
        this.searchType = searchType;
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("Yay!");
    }
}

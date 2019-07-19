package ActionListeners.UserActionListeners;

import Main.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class UserActionListener implements ActionListener {
    // TODO may not need this instance variable
    private User user;

    UserActionListener(User user) {
        this.user = user;
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);
}

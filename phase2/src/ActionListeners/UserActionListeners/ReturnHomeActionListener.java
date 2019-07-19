package ActionListeners.UserActionListeners;

import Main.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ReturnHomeActionListener extends UserActionListener {

    private final String HOME = "Home";

    public ReturnHomeActionListener(User user) {
        super(user);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel cards = (JPanel) e.getSource();
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, HOME);
    }
}

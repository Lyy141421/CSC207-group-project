package ActionListeners.UserActionListeners;

import Main.User;
import NewGUI.ReferencePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ReturnHomeActionListener extends UserActionListener {

    public ReturnHomeActionListener(User user) {
        super(user);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        JMenuBar sideBarMenu = (JMenuBar) menuItem.getParent();
        JPanel sideBarMenuPanel = (JPanel) sideBarMenu.getParent();
        JPanel fullPanel = (JPanel) sideBarMenuPanel.getParent();
        JPanel cards = (JPanel) fullPanel.getComponent(1);
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, ReferencePanel.HOME);
    }
}

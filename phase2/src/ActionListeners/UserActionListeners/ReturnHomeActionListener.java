package ActionListeners.UserActionListeners;

import ActionListeners.CardLayoutPanelGetter;
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
        JPanel cards = new CardLayoutPanelGetter().getLayoutForMenuItemDirectlyOnMenuBar(e);
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, ReferencePanel.HOME);
    }
}

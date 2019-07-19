package ActionListeners.UserActionListeners;

import ActionListeners.CardLayoutPanelGetter;
import Main.User;
import NewGUI.ReferencePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ProfileActionListener extends UserActionListener {

    public ProfileActionListener(User user) {
        super(user);
    }


    public void actionPerformed(ActionEvent e) {
        JPanel cards = new CardLayoutPanelGetter().getLayoutForMenuItemDirectlyOnMenuBar(e);
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, ReferencePanel.PROFILE);
    }

}

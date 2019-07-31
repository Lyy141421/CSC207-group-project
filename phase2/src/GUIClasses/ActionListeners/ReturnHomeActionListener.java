package GUIClasses.ActionListeners;

import GUIClasses.CommonUserGUI.UserPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReturnHomeActionListener implements ActionListener {

    public ReturnHomeActionListener() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel cards = new CardLayoutPanelGetter().fromMenuItemDirectlyOnMenuBar(e);
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, UserPanel.HOME);
    }
}

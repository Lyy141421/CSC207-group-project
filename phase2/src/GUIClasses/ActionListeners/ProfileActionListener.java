package GUIClasses.ActionListeners;

import GUIClasses.CommonUserGUI.UserPanel;
import GUIClasses.ReferenceInterface.ReferencePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileActionListener implements ActionListener {

    public ProfileActionListener() {
    }


    public void actionPerformed(ActionEvent e) {
        JPanel cards = new CardLayoutPanelGetter().fromMenuItemDirectlyOnMenuBar(e);
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, UserPanel.PROFILE);
    }

}
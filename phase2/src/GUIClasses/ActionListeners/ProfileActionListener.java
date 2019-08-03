package GUIClasses.ActionListeners;

import GUIClasses.CommonUserGUI.UserMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileActionListener implements ActionListener {

    public ProfileActionListener() {
    }


    public void actionPerformed(ActionEvent e) {
        JPanel cards = new PanelGetter().getCardLayoutFromMenuItemDirectlyOnMenuBar(e);
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, UserMain.PROFILE);
    }

}

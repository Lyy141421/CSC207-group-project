package GUIClasses.ActionListeners;

import GUIClasses.CommonUserGUI.UserMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileActionListener implements ActionListener {

    private JPanel cards;

    public ProfileActionListener(JPanel cards) {
        this.cards = cards;
    }


    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, UserMain.PROFILE);
    }

}

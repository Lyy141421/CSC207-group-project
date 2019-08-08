package GUIClasses.ActionListeners;

import GUIClasses.ApplicantInterface.ApplicantMain;
import GUIClasses.CommonUserGUI.UserMain;
import Main.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class ReturnHomeActionListener implements ActionListener {

    private JPanel cards;

    public ReturnHomeActionListener(JPanel cards) {
        this.cards = cards;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout) cards.getLayout();
        if (cards.getParent() instanceof UserMain) {
            ((UserMain) cards.getParent()).refresh();
        } else {
            ((ApplicantMain) cards.getParent()).refresh();
        }
        cl.show(cards, UserMain.HOME);
    }
}

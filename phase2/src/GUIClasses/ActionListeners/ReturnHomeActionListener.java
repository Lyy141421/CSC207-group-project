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
        Thread newThread = new Thread() {
            public void run() {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            if (cards.getParent() instanceof UserMain) {
                                ((UserMain) cards.getParent()).refresh();
                            } else {
                                ((ApplicantMain) cards.getParent()).refresh();
                            }
                        }
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }
        };
        newThread.start();
        cl.show(cards, UserMain.HOME);
    }
}

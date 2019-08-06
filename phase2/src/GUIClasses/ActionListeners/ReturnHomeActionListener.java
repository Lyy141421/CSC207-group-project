package GUIClasses.ActionListeners;

import GUIClasses.CommonUserGUI.UserMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class ReturnHomeActionListener implements ActionListener {

    public ReturnHomeActionListener() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel cards = new PanelGetter().getCardLayoutFromMenuItemDirectlyOnMenuBar(e);
        CardLayout cl = (CardLayout) cards.getLayout();
        Thread newThread = new Thread() {
            public void run() {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            ((UserMain) cards.getParent()).refresh();
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

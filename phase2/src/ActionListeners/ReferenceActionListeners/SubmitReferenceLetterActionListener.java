package ActionListeners.ReferenceActionListeners;

import ApplicantStuff.Reference;
import NewGUI.ReferencePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SubmitReferenceLetterActionListener extends ReferenceActionListener {

    public SubmitReferenceLetterActionListener(Reference reference) {
        super(reference);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        JMenuBar sideBarMenu = (JMenuBar) menuItem.getParent();
        JPanel sideBarMenuPanel = (JPanel) sideBarMenu.getParent();
        JPanel fullPanel = (JPanel) sideBarMenuPanel.getParent();
        JPanel cards = (JPanel) fullPanel.getComponent(1);
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, ReferencePanel.SUBMIT_REFERENCE_LETTER);
    }
}

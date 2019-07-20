package ActionListeners.ReferenceActionListeners;

import ActionListeners.CardLayoutPanelGetter;
import ApplicantStuff.Reference;
import NewGUI.ReferencePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ViewRefereeJobPostingsActionListener extends ReferenceActionListener {

    public ViewRefereeJobPostingsActionListener(Reference reference) {
        super(reference);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel cards = new CardLayoutPanelGetter().getLayoutForMenuItemDirectlyOnMenuBar(e);
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, ReferencePanel.VIEW_REFEREE_JOB_POSTINGS);
    }
}

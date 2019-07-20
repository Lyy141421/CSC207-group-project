package NewGUI;

import ActionListeners.CardLayoutPanelGetter;
import ActionListeners.UserActionListeners.*;
import ApplicantStuff.Reference;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class ReferenceSideBarMenuPanel extends JPanel {

    // === Class variables ===
    private static int CELL_WIDTH = 190;
    private static int CELL_HEIGHT = 20;
    private static int NUM_MAIN_MENU_OPTIONS = 4;

    // === Instance variable ===
    private Reference reference;


    // === Constructor ===
    ReferenceSideBarMenuPanel(Reference reference) {
        this.reference = reference;
        TreeMap<String, Object> fullMenu = this.createFullMenu();
        this.setLayout(new BorderLayout());
        this.add(new SideBarMenu(fullMenu, CELL_WIDTH, CELL_HEIGHT).createMenuBar());
        this.setSize(CELL_WIDTH, CELL_HEIGHT * NUM_MAIN_MENU_OPTIONS);
    }

    /**
     * Create the map for the full menu.
     *
     * @return the map for the full menu.
     */
    private TreeMap<String, Object> createFullMenu() {
        TreeMap<String, Object> fullMenu = new TreeMap<>();
        fullMenu.put("1. Home", new ReturnHomeActionListener(this.reference));
        fullMenu.put("2. Profile", new ProfileActionListener(this.reference));
        fullMenu.put("3. Submit Reference Letter", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel cards = new CardLayoutPanelGetter().getLayoutForMenuItemDirectlyOnMenuBar(e);
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, ReferencePanel.SUBMIT_REFERENCE_LETTER);
            }
        });
        fullMenu.put("4. View Referee Job Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel cards = new CardLayoutPanelGetter().getLayoutForMenuItemDirectlyOnMenuBar(e);
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, ReferencePanel.VIEW_REFEREE_JOB_POSTINGS);
            }
        });
        return fullMenu;
    }

}

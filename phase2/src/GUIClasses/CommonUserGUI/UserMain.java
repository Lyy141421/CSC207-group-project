package GUIClasses.CommonUserGUI;

import javax.swing.*;

abstract public class UserMain extends JPanel {
    /**
     * A user panel.
     */

    // === Class variables ===
    // Keys for common cards
    public static final String HOME = "HOME";
    public static final String PROFILE = "PROFILE";

    /**
     * Set the cards.
     */
    abstract public void setCards();

    /**
     * Refresh the page.
     */
    abstract public void refresh();

}
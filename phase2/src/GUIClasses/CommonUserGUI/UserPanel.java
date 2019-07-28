package GUIClasses.CommonUserGUI;

import javax.swing.*;

abstract public class UserPanel extends JPanel {
    /**
     * A user panel.
     */

    // === Class variables ===
    // Keys for common cards
    public static String HOME = "HOME";
    public static String PROFILE = "PROFILE";

    /**
     * Set the cards.
     */
    abstract public void setCards();

    /**
     * Refresh the page.
     */
    abstract public void refresh();

}
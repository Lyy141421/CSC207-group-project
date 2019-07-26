package GUIClasses.CommonUserGUI;

import javax.swing.*;

abstract public class UserPanel extends JPanel {

    public static String HOME = "HOME";
    public static String PROFILE = "PROFILE";

    abstract public void addCards();

    abstract public void resetCards();

}
package GUIClasses.CommonUserGUI;

import javax.swing.*;
import java.awt.*;

public class FrequentlyUsedMethods {
    //TODO rename

    /**
     * Create the title panel.
     *
     * @param text     The actual title.
     * @param fontSize The size of the title.
     * @return the JPanel with this title.
     */
    public JPanel createTitlePanel(String text, int fontSize) {
        JLabel title = new JLabel(text);
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(title, BorderLayout.PAGE_START);
        title.setFont(new Font("Century Gothic", Font.BOLD, fontSize));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.revalidate();
        return titlePanel;
    }
}

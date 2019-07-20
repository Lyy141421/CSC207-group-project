package NewGUI;

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
        JLabel pageTitle = new JLabel(text);
        JPanel pageTitlePanel = new JPanel(new BorderLayout());
        pageTitlePanel.add(pageTitle, BorderLayout.PAGE_START);
        pageTitle.setFont(new Font("Century Gothic", Font.BOLD, fontSize));
        pageTitle.setHorizontalAlignment(JLabel.CENTER);
        pageTitle.revalidate();
        return pageTitlePanel;
    }
}

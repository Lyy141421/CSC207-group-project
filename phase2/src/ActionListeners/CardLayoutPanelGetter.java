package ActionListeners;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CardLayoutPanelGetter {

    /**
     * Get the panel with the card layout.
     * @param e The click of the menu item on the menu bar.
     * @return the panel with the card layout.
     */
    public JPanel fromMenuItemDirectlyOnMenuBar(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        JMenuBar sideBarMenu = (JMenuBar) menuItem.getParent();
        JPanel sideBarMenuPanel = (JPanel) sideBarMenu.getParent();
        JPanel fullPanel = (JPanel) sideBarMenuPanel.getParent();
        return (JPanel) fullPanel.getComponent(1);
    }

    /**
     * Get the panel with the card layout from the submit file button.
     *
     * @param e The click of the submit file button.
     * @return the panel with the card layout.
     */
    JPanel fromSubmitFilesButton(ActionEvent e) {
        JButton submitButton = (JButton) e.getSource();
        JPanel submitButtonPanel = (JPanel) submitButton.getParent();
        JPanel fileChooserPanel = (JPanel) submitButtonPanel.getParent();
        JPanel submitFilePanel = (JPanel) fileChooserPanel.getParent();
        return (JPanel) submitFilePanel.getParent();
    }
}

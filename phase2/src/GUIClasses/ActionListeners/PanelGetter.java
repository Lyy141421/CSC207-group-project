package GUIClasses.ActionListeners;

import javax.swing.*;
import java.awt.event.ActionEvent;

class PanelGetter {

    /**
     * Get the panel with the card layout.
     * @param e The click of the menu item on the menu bar.
     * @return the panel with the card layout.
     */
    JPanel getCardLayoutFromMenuItemDirectlyOnMenuBar(ActionEvent e) {
        JPanel fullPanel = this.getUserPanelFromMenuItemDirectlyOnMenuBar(e);
        return (JPanel) fullPanel.getComponent(1);
    }

    /**
     * Get the panel with the card layout from the submit file button.
     *
     * @param e The click of the submit file button.
     * @return the panel with the card layout.
     */
    JPanel getCardLayoutFromSubmitFilesButton(ActionEvent e) {
        JButton submitButton = (JButton) e.getSource();
        JPanel submitButtonPanel = (JPanel) submitButton.getParent();
        JPanel fileChooserPanel = (JPanel) submitButtonPanel.getParent();
        JPanel submitFilePanel = (JPanel) fileChooserPanel.getParent();
        return (JPanel) submitFilePanel.getParent();
    }

    JPanel getUserPanelFromMenuItemDirectlyOnMenuBar(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        JMenuBar sideBarMenu = (JMenuBar) menuItem.getParent();
        JPanel sideBarMenuPanel = (JPanel) sideBarMenu.getParent();
        return (JPanel) sideBarMenuPanel.getParent();
    }
}

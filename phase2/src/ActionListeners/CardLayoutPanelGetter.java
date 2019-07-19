package ActionListeners;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CardLayoutPanelGetter {

    /**
     * Get the panel with the card layout.
     *
     * @return the panel with the card layout.
     */
    public JPanel getLayoutForMenuItemDirectlyOnMenuBar(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        JMenuBar sideBarMenu = (JMenuBar) menuItem.getParent();
        JPanel sideBarMenuPanel = (JPanel) sideBarMenu.getParent();
        JPanel fullPanel = (JPanel) sideBarMenuPanel.getParent();
        return (JPanel) fullPanel.getComponent(1);
    }
}

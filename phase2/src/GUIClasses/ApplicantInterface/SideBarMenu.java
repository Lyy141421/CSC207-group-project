package GUIClasses.ApplicantInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

class SideBarMenu extends MouseAdapter {

    // === Instance variables ===
    private TreeMap<String, Object> menuTitles;
    private MouseAdapter mouseAdapter = new MouseAdapter1();
    private int cellWidth;
    private int cellHeight;

    // === Constructor ===
    SideBarMenu(TreeMap<String, Object> menuTitles, int cellWidth, int cellHeight) {
        this.menuTitles = menuTitles;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    // === Package-private methods ===

    /**
     * Creates a menu bar with the structure dictated by the menuTitles TreeMap.
     *
     * @return a formatted menu bar.
     */
    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.PAGE_AXIS));
        this.createMenu(menuBar, menuTitles);
        for (Component component : menuBar.getComponents()) {
            this.setBackgroundAndBorder((JComponent) component);
        }
        this.setBackgroundAndBorder(menuBar);
        return menuBar;
    }

    /**
     * Create a menu item with this title and action listener
     *
     * @param title          The title of the menu item.
     * @param actionListener The action listener to be added to the menu item.
     * @return the JMenuItem created.
     */
    private JMenuItem createMenuItem(String title, ActionListener actionListener) {
        JMenuItem menu = new JMenuItem(title);
        menu.addActionListener(actionListener);
        menu.addMouseListener(mouseAdapter);
        return menu;
    }

    private void setBackgroundAndBorder(JComponent menu) {
        menu.setBackground(Color.LIGHT_GRAY);
        menu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.DARK_GRAY));
        menu.setPreferredSize(new Dimension(this.cellWidth, this.cellHeight));
        menu.revalidate();
    }

    /**
     * Create a menu
     *
     * @param menu    The JMenuBar / JMenu to which other menu items (JMenuItem or JMenu) are to be attached.
     * @param subMenu The submenu for the menu component.
     */
    private void createMenu(JComponent menu, Object subMenu) {
        TreeMap<String, Object> map = (TreeMap<String, Object>) subMenu;
        for (String menuTitle : map.keySet()) {
            if (map.get(menuTitle) instanceof ActionListener) {
                JMenuItem menuItem = this.createMenuItem(menuTitle, (ActionListener) map.get(menuTitle));
                menu.add(menuItem);
            } else {
                VerticalMenu popUpMenu = new VerticalMenu(menuTitle);
                this.setBackgroundAndBorder(popUpMenu);
                popUpMenu.addMouseListener(mouseAdapter);
                menu.add(popUpMenu);
                this.createMenu(popUpMenu, map.get(menuTitle));
            }
        }
    }

    // === Private classes ===
    class VerticalMenu extends JMenu {

        VerticalMenu(String label) {
            super(label);
            JPopupMenu pm = getPopupMenu();
            if (pm != null) {
                pm.setLayout(new BoxLayout(pm, BoxLayout.PAGE_AXIS));
            }
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public void setPopupMenuVisible(boolean b) {
            boolean isVisible = isPopupMenuVisible();
            if (b != isVisible) {
                if ((b == true) && isShowing()) {
                    getPopupMenu().show(this, getWidth(), 0);   // Pop-up menu to the right
                } else {
                    getPopupMenu().setVisible(false);
                }
            }
        }
    }


    /**
     * Code adapted from a comment at https://stackoverflow.com/questions/12125402/activate-jmenubar-on-hover
     */
    class MouseAdapter1 extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            ((JMenuItem) e.getSource()).setArmed(true);
        }

        public void mouseExited(MouseEvent e) {
            ((JMenuItem) e.getSource()).setArmed(false);
        }
    }
}

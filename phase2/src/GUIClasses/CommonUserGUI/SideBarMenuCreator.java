/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * Adapted from MenuLayoutDemo.java from https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html#eg
 * @author ges
 * @author kwalrath
 */

package GUIClasses.CommonUserGUI;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;
import javax.swing.*;


public class SideBarMenuCreator extends MouseAdapter {
    /**
     * A side bar menu creator.
     */

    // === Instance variables ===
    private TreeMap<String, Object> menuTitles; // A map of menu titles to action listeners
    private MouseAdapter mouseAdapter = new MouseAdapter1();    // The mouse adapter

    // === Constructor ===
    public SideBarMenuCreator(TreeMap<String, Object> menuTitles) {
        this.menuTitles = menuTitles;
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

    /**
     * Set the background colour and border
     *
     * @param menu The menu that is being set
     */
    private void setBackgroundAndBorder(JComponent menu) {
        menu.setBackground(Color.LIGHT_GRAY);
//        menu.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
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
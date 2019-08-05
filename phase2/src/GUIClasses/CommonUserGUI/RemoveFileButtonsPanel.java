package GUIClasses.CommonUserGUI;

import GUIClasses.ActionListeners.GenericRemoveFileButtonActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class RemoveFileButtonsPanel extends JPanel implements Scrollable {
    /**
     * A panel for removing files selected.
     */

    // === Class variables ===
    private static final Color LIGHT_RED = new Color(255, 210, 210);
    private JPanel buttonPanel;

    // === Constructor ===
    RemoveFileButtonsPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        this.add(buttonPanel);
    }

    /**
     * Adds a remove button to this panel and returns the one added.
     *
     * @param file The file that has been selected.
     * @return the remove button that was created.
     */
    JButton addButton(File file) {
        JButton removeFileButton = new JButton("Remove " + file.getName());
        removeFileButton.setName(file.getName());
        removeFileButton.setBackground(LIGHT_RED);
        buttonPanel.add(removeFileButton);
        removeFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.validate();
        buttonPanel.repaint();
        this.repaint();
        this.revalidate();
        return removeFileButton;
    }

    public void removeButton(File file) {
        for (Component c : buttonPanel.getComponents()) {
            if (c.getName().equals(file.getName())) {
                buttonPanel.remove(c);
                break;
            }
        }
        buttonPanel.repaint();
        buttonPanel.revalidate();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(250, 200);
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }
}

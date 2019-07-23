package GUIClasses.CommonUserGUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

class RemoveFileButtonsPanel extends JPanel {

    // === Class variables ===
    private static final Color LIGHT_RED = new Color(255, 210, 210);

    RemoveFileButtonsPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Adds a remove button to this panel and returns the one added.
     *
     * @param file The file that has been selected.
     * @return the remove button that was created.
     */
    JButton addButton(File file) {
        JButton removeFileButton = new JButton("Remove " + file.getName());
        removeFileButton.setBackground(LIGHT_RED);
        this.add(removeFileButton);
        removeFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.validate();
        this.repaint();
        return removeFileButton;
    }
}

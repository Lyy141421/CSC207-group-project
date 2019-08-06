package GUIClasses.CommonUserGUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

abstract class AbstractDocumentUser extends JPanel implements ActionListener {
    // === Instance variables ===
    private JLabel label = new JLabel();   // The label for this panel
    private JButton button = new JButton(); // The button for this panel
    private JList list; // The list of file names that the user can choose from.
    private String[] fileNames; // The file names of files that this user can view.
    private File[] files;   // The files that this user can view

    // === Representation Invariant ===
    // The elements in fileNames and files correspond to each other (ie, fileNames[0] is the file name of files[0])

    // === Constructor ===
    AbstractDocumentUser(File folder) {
        this.fileNames = folder.list();
        if (folder.listFiles().length == 0) {
            this.files = new File[0];
        } else {
            this.files = folder.listFiles();
        }

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(label);
        this.setListScrollPane();
        this.add(list);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(this.createButtonPanel());
    }

    /**
     * Create the button panel with an action listener.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        button.setEnabled(false);
        button.setSize(50, 20);
        buttonPanel.add(button);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(this);
        return buttonPanel;
    }

    // === Getters ===
    File[] getFiles() {
        return this.files;
    }

    JList getList() {
        return this.list;
    }

    public JButton getButton() {
        return this.button;
    }

    // === Setters ==
    void setPanelTitle(String text) {
        this.label.setText(text);
    }

    void setButton(String text) {
        this.button.setText(text);
    }

    // === Other methods ===
    @Override
    public abstract void actionPerformed(ActionEvent e);

    /**
     * Create the list panel.
     */
    private void setListScrollPane() {
        list = new JList(this.fileNames);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(-1);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (list.getSelectedIndex() != -1) {
                    button.setEnabled(true);
                }
            }
        });

        list.setPreferredSize(new Dimension(250, 200));
    }

}

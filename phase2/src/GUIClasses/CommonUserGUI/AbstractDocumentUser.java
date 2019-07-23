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
    public AbstractDocumentUser(File folder) {
        this.fileNames = folder.list();
        this.files = folder.listFiles();

        this.setLayout(new BorderLayout());
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        this.add(label, BorderLayout.NORTH);

        this.createListPanel();
        this.createButtonPanel();
    }

    /**
     * Create the button panel with an action listener.
     */
    void createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        button.setEnabled(false);
        button.setSize(50, 20);
        buttonPanel.add(button);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        button.addActionListener(this);
        this.add(buttonPanel, BorderLayout.PAGE_END);
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
    private void createListPanel() {
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

        JPanel listPanel = new JPanel();
        listPanel.add(new JScrollPane(list));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        this.add(listPanel, BorderLayout.CENTER);
    }

}

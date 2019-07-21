package NewGUI.ReferenceInterface;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import NewGUI.FileChooser;
import NewGUI.FrequentlyUsedMethods;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReferenceSubmitLetterPanel extends JPanel implements ActionListener, ListSelectionListener {

    // === Instance variables ===
    // The reference who logged in
    private Reference reference;
    // The button for selecting a job application
    private JButton selectJobAppButton = new JButton("Select");
    // The list that displays the job applications that need reference letters
    private JList jobAppList;
    // The file chooser for this reference
    private FileChooser fileChooser;

    // === Constructor ===
    ReferenceSubmitLetterPanel(Reference reference) {
        this.reference = reference;
        this.fileChooser = new FileChooser(reference, null);    // So that the file chooser is on the panel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.add(new FrequentlyUsedMethods().createTitlePanel("Submit Reference Letter", 20));
        this.add(new FrequentlyUsedMethods().createTitlePanel("Select a Job Application", 15));
        this.add(this.createJobApplicationListPanel());
        this.add(new FrequentlyUsedMethods().createTitlePanel("Choose a File", 15));
        this.add(fileChooser);
    }

    /**
     * Create a list panel with all the job applications that this reference needs to write a reference for.
     *
     * @return the panel with this list.
     */
    private JPanel createJobApplicationListPanel() {
        JPanel jobAppListPanel = new JPanel();
        jobAppListPanel.setLayout(new BorderLayout());
        DefaultListModel listModel = new DefaultListModel();
        for (JobApplication jobApp : reference.getJobAppsForReference()) {
            listModel.addElement(jobApp.getMiniDescriptionForReference());
        }
        jobAppList = new JList(listModel);
        jobAppList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobAppList.setSelectedIndex(-1);
        jobAppList.addListSelectionListener(this);
        jobAppList.setVisibleRowCount(5);
        jobAppList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        JScrollPane listScrollPane = new JScrollPane(jobAppList);
        jobAppListPanel.add(listScrollPane, BorderLayout.CENTER);
        jobAppListPanel.add(this.createButtonPane(), BorderLayout.PAGE_END);
        return jobAppListPanel;
    }

    /**
     * Create the button pane for the list.
     *
     * @return the button pane for the job application list.
     */
    private JPanel createButtonPane() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        selectJobAppButton.addActionListener(this);
        selectJobAppButton.setEnabled(false);
        buttonPane.add(selectJobAppButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return buttonPane;
    }

    // For the list selection listener
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (jobAppList.getSelectedIndex() == -1) {
                selectJobAppButton.setEnabled(false);   //No selection, disable select button.

            } else {
                selectJobAppButton.setEnabled(true);    //Selection, enable the select button.
            }
        }
    }

    // For the Select button action listener
    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedIndex = jobAppList.getSelectedIndex();
        JobApplication jobAppSelected = reference.getJobAppsForReference().get(selectedIndex);
        this.remove(fileChooser);
        fileChooser = new FileChooser(reference, jobAppSelected);
        fileChooser.getUploadButton().setEnabled(true);
        this.add(fileChooser);
        this.revalidate();
    }
}

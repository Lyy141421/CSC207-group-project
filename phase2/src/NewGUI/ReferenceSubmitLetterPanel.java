package NewGUI;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;

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
    // The job application selected from the list
    private JobApplication jobAppSelected;
    // The button for selecting a job application
    private JButton selectJobAppButton = new JButton("Select");
    //  The list that displays the job applications that need reference letters
    private JList jobAppList;
    // The file chooser for this reference
    private FileChooser fileChooser = new FileChooser(this.reference);

    // === Constructor ===
    ReferenceSubmitLetterPanel(Reference reference) {
        this.reference = reference;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel pageTitle = new FrequentlyUsedMethods().createTitlePanel("Submit Reference Letter", 20);
        JPanel selectApplicationTitle = new FrequentlyUsedMethods().createTitlePanel("Select a Job Application", 15);
        JPanel chooseAFileTitle = new FrequentlyUsedMethods().createTitlePanel("Choose a File", 15);
        this.add(pageTitle);
        this.add(selectApplicationTitle);
        this.add(this.createJobApplicationListPanel());
        this.add(chooseAFileTitle);
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
                //No selection, disable select button.
                selectJobAppButton.setEnabled(false);

            } else {
                //Selection, enable the select button.
                selectJobAppButton.setEnabled(true);
            }
        }
    }

    // For the Select button action listener
    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedIndex = jobAppList.getSelectedIndex();
        jobAppSelected = reference.getJobAppsForReference().get(selectedIndex);
        fileChooser.getUploadButton().setEnabled(true);
    }
}

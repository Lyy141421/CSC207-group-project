package GUIClasses.ReferenceInterface;

import ApplicantStuff.JobApplication;
import GUIClasses.CommonUserGUI.FileChooser;
import GUIClasses.CommonUserGUI.GUIElementsCreator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReferenceSubmitLetterPanel extends JPanel implements ActionListener, ListSelectionListener {
    /**
     * The panel for submitting a reference letter.
     */

    // === Instance variables ===
    // The card panel
    private JPanel cardPanel;
    // The reference back end
    private ReferenceBackEnd referenceBackEnd;
    // The button for selecting a job application
    private JButton selectJobAppButton = new JButton("Select");
    // The list that displays the job applications that need reference letters
    private JList jobAppList;
    // The file chooser for this reference
    private FileChooser fileChooser;

    // === Constructor ===
    ReferenceSubmitLetterPanel(JPanel cardPanel, ReferenceBackEnd referenceBackEnd) {
        this.cardPanel = cardPanel;
        this.referenceBackEnd = referenceBackEnd;
        this.fileChooser = new FileChooser(cardPanel, this.referenceBackEnd.getReference(), null);    // So that the file chooser is on the panel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.add(new GUIElementsCreator().createLabelPanel("Submit Reference Letter", 20, true));
        this.add(new GUIElementsCreator().createLabelPanel("Select a Job Application", 15, true));
        this.add(this.createJobApplicationListPanel());
        this.add(new GUIElementsCreator().createLabelPanel("Choose a File", 15, true));
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
        for (JobApplication jobApp : referenceBackEnd.getJobAppsForReference()) {
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
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        selectJobAppButton.addActionListener(this);
        selectJobAppButton.setEnabled(false);
        buttonPane.add(selectJobAppButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return buttonPane;
    }

    // For the list selection listener
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
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
        JobApplication jobAppSelected = referenceBackEnd.getJobAppsForReference().get(selectedIndex);
        if (referenceBackEnd.isTodayAfterApplicationCloseDate(jobAppSelected)) {
            this.remove(fileChooser);
            fileChooser = new FileChooser(cardPanel, referenceBackEnd.getReference(), jobAppSelected);
            fileChooser.enableUploadButton();
            this.add(fileChooser);
            this.revalidate();
        } else {
            JOptionPane.showMessageDialog(this, "Reference letter submission has not yet opened for this job posting");
        }
    }
}

package GUIClasses.InterviewerInterface;

import ApplicantStuff.JobApplication;
import GUIClasses.CommonUserGUI.GUIElementsCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;

class InterviewerCompleteInterviewFull extends InterviewerViewAndWriteNotes {
    /**
     * The panel where interviewers set the results of an interview.
     */

    // === Instance/Class variables ===
    private static String ADVANCE = "Advance to next round";     // Label for advance radio button
    private static String REJECT = "Reject";    // Label reject for radio button
    private JPanel resultsPanel = new JPanel(new BorderLayout());   // The panel for setting interview results.
    // The map of job applications to the button group of radio buttons associated with the pass/fail decision
    //private HashMap<JobApplication, ButtonGroup> jobAppsToButtonGroup = new HashMap<>();
    private HashMap<JobApplication, JRadioButton> jobAppsToAdvanceButton = new HashMap<>();

    // === Constructor ===
    InterviewerCompleteInterviewFull(InterviewerBackEnd interviewerBackEnd) {
        super(interviewerBackEnd);
        this.infoPane.addTab("Set results", this.resultsPanel);
    }

    /**
     * Loads the contents of the tabs for the tabbed pane.
     *
     */
    @Override
    void loadTabContents() {
        super.loadTabContents();
        setResultsPanelFull();
    }

    /**
     * Set the full results panel.
     */
    private void setResultsPanelFull() {
        resultsPanel.removeAll();
        if (!interviewerBackEnd.isCoordinator(interviewSelected)) {
            JPanel message = new GUIElementsCreator().createLabelPanel("You do not have permission to set the " +
                    "results of this interview", 18, false);
            message.setBorder(BorderFactory.createEmptyBorder(100, 20, 20, 20));
            resultsPanel.add(message, BorderLayout.CENTER);
        } else {
            setResultsPanelAndHashMap();
            resultsPanel.add(createSaveResultsButtonPanel(), BorderLayout.AFTER_LAST_LINE);
        }
        resultsPanel.revalidate();
    }

    /**
     * Sets the result panel and map of job applications to button group.
     */
    private void setResultsPanelAndHashMap() {
        resultsPanel.add(new GUIElementsCreator().createLabelPanel("Set the interview results", 20, true),
                BorderLayout.PAGE_START);
        JPanel selectResultsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        for (JobApplication jobApp : interviewSelected.getJobApplications()) {
            JLabel intervieweeName = new JLabel(jobApp.getApplicant().getLegalName() + "    ");
            selectResultsPanel.add(intervieweeName, c);
            c.gridx = 1;
            ButtonGroup buttonGroup = new ButtonGroup();
            JRadioButton advanceRadioButton = new JRadioButton(ADVANCE);
            selectResultsPanel.add(advanceRadioButton, c);
            c.gridx++;
            JRadioButton rejectRadioButton = new JRadioButton(REJECT);
            selectResultsPanel.add(rejectRadioButton, c);
            buttonGroup.add(advanceRadioButton);
            buttonGroup.add(rejectRadioButton);
            //jobAppsToButtonGroup.put(jobApp, buttonGroup);
            jobAppsToAdvanceButton.put(jobApp, advanceRadioButton);
            c.gridx = 0;
            c.gridy++;
        }
        this.resultsPanel.add(selectResultsPanel, BorderLayout.CENTER);
    }

    /**
     * Create a panel with the button to save results.
     * @return the button panel created.
     */
    private JPanel createSaveResultsButtonPanel() {
        JPanel saveButtonPanel = new JPanel();
        JButton saveButton = new JButton("Save results");
        saveButtonPanel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<JobApplication, Boolean> jobAppToResults = new HashMap<>();
                for (JobApplication jobApp : jobAppsToAdvanceButton.keySet()) {
                    //boolean isAdvanced = isAdvancedButtonSelected(jobAppsToButtonGroup.get(jobApp));
                    boolean isAdvanced = jobAppsToAdvanceButton.get(jobApp).isSelected();
                    jobAppToResults.put(jobApp, isAdvanced);
                    JOptionPane.showMessageDialog(resultsPanel, "You have successfully completed this interview.");
                }
                interviewerBackEnd.setInterviewResults(interviewSelected, jobAppToResults);
                refresh();
            }
        });

        return saveButtonPanel;
    }

    /**
     * Checks whether the radio button marked as "advanced" was selected.
     * @param buttonGroup   The button group for a specific job application.
     * @return true iff the radio button marked as "advanced" was selected.
     */
    private boolean isAdvancedButtonSelected(ButtonGroup buttonGroup) {
        String text = null;
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                text = button.getText();
            }
        }
        return text.equals(ADVANCE);
    }
}
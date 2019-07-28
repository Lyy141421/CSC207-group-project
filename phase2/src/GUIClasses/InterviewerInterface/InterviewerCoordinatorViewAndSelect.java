package GUIClasses.InterviewerInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.Interview;
import GUIClasses.CommonUserGUI.GUIElementsCreator;
import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;

class InterviewerCoordinatorViewAndSelect extends InterviewerViewAndWriteNotes {
    /**
     * The panel where interviewers set the results of an interview.
     */

    // === Instance/Class variables ===
    private static String ADVANCE = "Advance to next round";     // Label for advance radio button
    private static String REJECT = "Reject";    // Label reject for radio button
    private JPanel resultsPanel = new JPanel(new BorderLayout());   // The panel for setting interview results.
    // The map of job applications to the button group of radio buttons associated with the pass/fail decision
    private HashMap<JobApplication, ButtonGroup> jobAppsToButtonGroup = new HashMap<>();

    // === Constructor ===
    InterviewerCoordinatorViewAndSelect(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
        this.infoPane.addTab("Set results", this.resultsPanel);
    }

    /**
     * Gets the map of interview titles to the interviews for all incomplete interviews as coordinator that have already occurred.
     *
     * @return a map of interview titles to the interviews for this card.
     */
    @Override
    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getIncompleteInterviewsAlreadyOccurredAsCoordinator());
    }

    /**
     * Loads the contents of the tabs for the tabbed pane.
     *
     */
    @Override
    void loadTabContents() {
        super.loadTabContents();
        resultsPanel.removeAll();
        setResultsPanelAndHashMap(this.interviewSelected);
        resultsPanel.add(createSaveResultsButtonPanel(), BorderLayout.AFTER_LAST_LINE);
        resultsPanel.revalidate();
    }

    /**
     * Sets the result panel and map of job applications to button group.
     * @param interviewSelected The interview selected by the interviewer.
     */
    private void setResultsPanelAndHashMap(Interview interviewSelected) {
        resultsPanel.add(new GUIElementsCreator().createTitlePanel("Set the interview results", 20),
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
            jobAppsToButtonGroup.put(jobApp, buttonGroup);
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
                for (JobApplication jobApp : jobAppsToButtonGroup.keySet()) {
                    boolean isAdvanced = isAdvancedButtonSelected(jobAppsToButtonGroup.get(jobApp));
                    jobAppToResults.put(jobApp, isAdvanced);
                    JOptionPane.showMessageDialog(resultsPanel, "You have successfully completed this interview.");
                }
                interviewerInterface.setInterviewResults(interviewSelected, jobAppToResults);
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
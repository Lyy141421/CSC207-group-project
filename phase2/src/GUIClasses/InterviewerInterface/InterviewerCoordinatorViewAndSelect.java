package GUIClasses.InterviewerInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.Interview;
import GUIClasses.CommonUserGUI.TitleCreator;
import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;

class InterviewerCoordinatorViewAndSelect extends InterviewerViewAndWriteNotes {

    private static String ADVANCE = "Advance to next round";
    private static String REJECT = "Reject";
    private JPanel resultsPanel = new JPanel(new BorderLayout());
    private HashMap<JobApplication, ButtonGroup> jobAppsToButtonGroup = new HashMap<>();

    InterviewerCoordinatorViewAndSelect(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
        this.infoPane.addTab("Set results", this.resultsPanel);
    }

    @Override
    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getIncompleteInterviewsAlreadyOccurredAsCoordinator());
    }

    private void setResultsPanelAndHashMap(Interview interviewSelected) {
        resultsPanel.add(new TitleCreator().createTitlePanel("Set the interview results", 20),
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

    @Override
    void loadTabContents(Interview selectedInterview) {
        super.loadTabContents(selectedInterview);
        resultsPanel.removeAll();
        setResultsPanelAndHashMap(interviewSelected);
        resultsPanel.add(createSaveResultsButtonPanel(), BorderLayout.AFTER_LAST_LINE);
        resultsPanel.revalidate();
    }

}

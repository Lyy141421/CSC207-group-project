package GUIClasses.HRInterface;

import CompanyStuff.Branch;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupInterviewFrame extends JInternalFrame {

    MethodsTheGUICallsInHR HRInterface;
    BranchJobPosting branchJobPosting;

    HashMap<String, Interviewer> nameToInterviewerMap;
    HashMap<Interviewer, JCheckBox> interviewerToCheckBoxMap;

    JComboBox<String> coordinatorSelection;

    GroupInterviewFrame(MethodsTheGUICallsInHR HRInterface, BranchJobPosting branchJobPosting) {
        this.HRInterface = HRInterface;
        this.branchJobPosting = branchJobPosting;

        this.nameToInterviewerMap = this.getNameToInterviewerMap(HRInterface.getInterviewersInField(branchJobPosting));

        this.setLayout(new BorderLayout());

        this.add(new JScrollPane(this.createInterviewers()), BorderLayout.EAST);
        this.add(this.createCoordinator(), BorderLayout.WEST);
        this.setButtons();
    }

    JPanel createCoordinator() {
        JPanel coordinatorPanel = new JPanel();
        coordinatorPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Select coordinator"));
        this.coordinatorSelection = new JComboBox<>(this.nameToInterviewerMap.keySet().toArray(new String[this.nameToInterviewerMap.size()]));
        this.coordinatorSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //TODO: enable deselected, disable selection of coordinator
                String name = (String) e.getItem();
                Interviewer selectedInterviewer = nameToInterviewerMap.get(name);
                JCheckBox interviewerCheckBox = interviewerToCheckBoxMap.get(selectedInterviewer);
                interviewerCheckBox.setEnabled(e.getStateChange() == ItemEvent.DESELECTED);
            }
        });

        coordinatorPanel.add(this.coordinatorSelection);

        return coordinatorPanel;
    }

    private JPanel createInterviewers() {
        JPanel interviewerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = -1;
        c.gridy = 0;
        for (String name: this.nameToInterviewerMap.keySet()) {
            JCheckBox checkBox = new JCheckBox(name);
            this.interviewerToCheckBoxMap.put(this.nameToInterviewerMap.get(name), checkBox);
            c.gridy += (c.gridx+1)/3;
            c.gridx = (c.gridx+1)%3;
            interviewerPanel.add(checkBox, c);
        }

        return interviewerPanel;
    }

    private HashMap<String, Interviewer> getNameToInterviewerMap(ArrayList<Interviewer> interviewers) {
        HashMap<String, Interviewer> nameToInterviewerMap = new HashMap<>();
        for (Interviewer interviewer: interviewers) {
            nameToInterviewerMap.put(toInterviewerName(interviewer), interviewer);
        }

        return nameToInterviewerMap;
    }

    private String toInterviewerName(Interviewer interviewer) {
        return interviewer.getLegalName();
    }

    private void setButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }
}

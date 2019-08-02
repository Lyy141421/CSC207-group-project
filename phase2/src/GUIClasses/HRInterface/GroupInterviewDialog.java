package GUIClasses.HRInterface;

import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

class GroupInterviewDialog extends JDialog {

    static int MAX_DAYS = 20;

    JFrame parent;
    HRBackend hrBackend;
    BranchJobPosting branchJobPosting;
    HRViewPosting parentPanel;

    HashMap<String, Interviewer> nameToInterviewerMap;

    JSpinner daysSpinner;
    JComboBox<String> coordinatorSelection;
    HashMap<Interviewer, JCheckBox> interviewerToCheckBoxMap = new HashMap<>();

    GroupInterviewDialog(JFrame parent, HRBackend hrBackend, BranchJobPosting branchJobPosting, HRViewPosting postingPanel) {
        super(parent, "Select interviewers for group interview");
        this.parent = parent;
        this.hrBackend = hrBackend;
        this.branchJobPosting = branchJobPosting;
        this.parentPanel = postingPanel;

        this.nameToInterviewerMap = this.getNameToInterviewerMap(hrBackend.getInterviewersInField(branchJobPosting));

        this.setLayout(new BorderLayout());

        this.daysSpinner = new JSpinner(new SpinnerNumberModel(1, 1, MAX_DAYS, 1));
        this.add(this.daysSpinner, BorderLayout.NORTH);

        this.add(new JScrollPane(this.createInterviewers()), BorderLayout.EAST);
        this.add(this.createCoordinator(), BorderLayout.WEST);
        this.setButtons();

        this.setSize(300, 200);
        this.setVisible(true);
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
                interviewerCheckBox.setSelected(false);
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
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hrBackend.setUpGroupInterviews(branchJobPosting,
                        nameToInterviewerMap.get((String)coordinatorSelection.getSelectedItem()),
                        getSelectedInterviewers(), (int)daysSpinner.getValue());
                parentPanel.removeFromJPLists(parentPanel.toJPTitle(branchJobPosting));
                parentPanel.reload();
                dispose();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                parentPanel.reload();
            }
        });
        this.add(confirmButton, BorderLayout.SOUTH);
    }

    private ArrayList<Interviewer> getSelectedInterviewers() {
        ArrayList<Interviewer> selectedInterviewer = new ArrayList<>();
        for (Interviewer interviewer: this.interviewerToCheckBoxMap.keySet()) {
            if (this.interviewerToCheckBoxMap.get(interviewer).isSelected()) {
                selectedInterviewer.add(interviewer);
            }
        }

        return selectedInterviewer;
    }
}

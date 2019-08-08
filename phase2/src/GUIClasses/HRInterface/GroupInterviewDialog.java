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

    private static int MAX_DAYS = 20;

    private JFrame parent;
    private HRBackend hrBackend;
    private JDialog container = this;
    private BranchJobPosting branchJobPosting;
    private HRViewPosting parentPanel;

    private HashMap<String, Interviewer> nameToInterviewerMap;

    private JSpinner daysSpinner;
    private JComboBox<String> coordinatorSelection;
    private HashMap<Interviewer, JCheckBox> interviewerToCheckBoxMap = new HashMap<>();

    GroupInterviewDialog(JFrame parent, HRBackend hrBackend, BranchJobPosting branchJobPosting, HRViewPosting postingPanel) {
        super(parent, "Please select interviewers for group interview");
        this.parent = parent;
        this.hrBackend = hrBackend;
        this.branchJobPosting = branchJobPosting;
        this.parentPanel = postingPanel;
        this.nameToInterviewerMap = this.getNameToInterviewerMap(hrBackend.getInterviewersInField(branchJobPosting));
        this.setLayout(new BorderLayout());

        this.setHeader();
        this.setSplitPane();
        this.setButtons();

        this.setDialogProperties();
        this.setVisible(true);
    }

    private void setDialogProperties() {
        this.setSize(500, 350);
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    private void setHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel prompt = new JLabel("Please select interviewers for group interview");
        headerPanel.add(prompt, BorderLayout.PAGE_START);
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel daysLabel = new JLabel("Minimum days to notify interviewers: ");
        this.daysSpinner = new JSpinner(new SpinnerNumberModel(1, 1, MAX_DAYS, 1));
        spinnerPanel.add(daysLabel);
        spinnerPanel.add(this.daysSpinner);
        headerPanel.add(spinnerPanel, BorderLayout.CENTER);
        this.add(headerPanel, BorderLayout.PAGE_START);
    }

    private void setSplitPane() {
        JSplitPane splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(150);
        splitDisplay.setLeftComponent(this.createCoordinator());
        splitDisplay.setRightComponent(new JScrollPane(this.createInterviewers()));
        this.add(splitDisplay, BorderLayout.CENTER);
    }

    private JPanel createCoordinator() {
        JPanel coordinatorPanel = new JPanel();
        coordinatorPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Select coordinator"));
        this.coordinatorSelection = new JComboBox<>(this.nameToInterviewerMap.keySet().toArray(new String[this.nameToInterviewerMap.size()]));
        this.coordinatorSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
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
        interviewerPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Select interviewer(s)"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 4, 2, 4);
        c.gridx = -1;
        c.gridy = 0;
        for (String name : this.nameToInterviewerMap.keySet()) {
            JCheckBox checkBox = new JCheckBox(name);
            this.interviewerToCheckBoxMap.put(this.nameToInterviewerMap.get(name), checkBox);
            c.gridy += (c.gridx + 1) / 3;
            c.gridx = (c.gridx + 1) % 3;
            interviewerPanel.add(checkBox, c);
        }
        Interviewer interviewer = nameToInterviewerMap.get(this.coordinatorSelection.getSelectedItem());
        JCheckBox checkBox = interviewerToCheckBoxMap.get(interviewer);
        checkBox.setSelected(false);
        checkBox.setEnabled(false);
        return interviewerPanel;
    }

    private HashMap<String, Interviewer> getNameToInterviewerMap(ArrayList<Interviewer> interviewers) {
        HashMap<String, Interviewer> nameToInterviewerMap = new HashMap<>();
        for (Interviewer interviewer : interviewers) {
            nameToInterviewerMap.put(toInterviewerName(interviewer), interviewer);
        }

        return nameToInterviewerMap;
    }

    private String toInterviewerName(Interviewer interviewer) {
        return interviewer.getLegalName();
    }

    private void setButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createConfirmButton());
        buttonPanel.add(createCancelButton());

        this.add(buttonPanel, BorderLayout.PAGE_END);
    }

    private JButton createConfirmButton() {
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hrBackend.setUpGroupInterviews(branchJobPosting,
                        nameToInterviewerMap.get(coordinatorSelection.getSelectedItem()),
                        getSelectedInterviewers(), (int) daysSpinner.getValue());
                parentPanel.main.removeFromJPLists(branchJobPosting);
                setModalityType(ModalityType.MODELESS);
                JOptionPane.showMessageDialog(container, "Group interview is set.");
                dispose();
            }
        });
        return confirmButton;
    }

    private JButton createCancelButton() {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setModalityType(ModalityType.MODELESS);
                dispose();
            }
        });
        return cancelButton;
    }


    private ArrayList<Interviewer> getSelectedInterviewers() {
        ArrayList<Interviewer> selectedInterviewer = new ArrayList<>();
        for (Interviewer interviewer : this.interviewerToCheckBoxMap.keySet()) {
            if (this.interviewerToCheckBoxMap.get(interviewer).isSelected()) {
                selectedInterviewer.add(interviewer);
            }
        }

        return selectedInterviewer;
    }
}

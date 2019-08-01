package GUIClasses.HRInterface;

import CompanyStuff.JobPostings.BranchJobPosting;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

class HRViewJobPostingThatCanBeUpdated extends HRPanel {

    private HashMap<String, BranchJobPosting> updatableJPs;

    private JTextArea info;
    private JButton updateButton;
    private JList<String> jobPostingList = new JList<>();
    private JPanel parent;

    HRViewJobPostingThatCanBeUpdated(HRBackend hrBackend, JPanel parent) {
        super(hrBackend);
        this.parent = parent;
        this.setLayout(new BorderLayout());
        this.updatableJPs = this.getTitleToJPMap(hrBackend.getJPThatCanBeUpdated());

        JSplitPane splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(250);
        this.setJobPostingList(splitDisplay);
        this.setInfoBox(splitDisplay);

        JPanel buttons = new JPanel(new FlowLayout());
        this.setUpdateButton();
        buttons.add(updateButton);

        this.add(splitDisplay, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);

        this.setListSelectionListener();
    }

    void reload() {
        this.jobPostingList.removeAll();
        this.jobPostingList.setListData(updatableJPs.keySet().toArray(new String[updatableJPs.size()]));
    }

    private void setListSelectionListener() {
        this.jobPostingList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedTitle = jobPostingList.getSelectedValue();
                BranchJobPosting selectedJP = updatableJPs.get(selectedTitle);
                info.setText(selectedJP.toString());
                updateButton.setEnabled(false);
            }
        });
    }

    private void setUpdateButton() {
        this.updateButton = new JButton("Update job posting");
        this.updateButton.setEnabled(false);
        this.updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTitle = jobPostingList.getSelectedValue();
                BranchJobPosting selectedJP = updatableJPs.get(selectedTitle);
                HRAddOrUpdatePostingForm updateForm = new HRAddOrUpdatePostingForm(hrBackend, false, selectedJP);
                if (parent.getComponents().length > 8) {
                    parent.remove(8);
                }
                parent.add(updateForm, HRPanel.UPDATE_POSTING_FORM, 8);
                ((CardLayout) parent.getLayout()).show(parent, HRPanel.UPDATE_POSTING_FORM);
            }
        });
    }

    private void setJobPostingList(JSplitPane splitDisplay) {
        this.jobPostingList.setListData(updatableJPs.keySet().toArray(new String[updatableJPs.size()]));
        this.jobPostingList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.jobPostingList.setLayoutOrientation(JList.VERTICAL);
        splitDisplay.setLeftComponent(new JScrollPane(this.jobPostingList));
    }

    private void setInfoBox(JSplitPane splitDisplay) {
        this.info = new JTextArea("Select a job posting to view information.");
        this.info.setEditable(false);
        this.info.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        splitDisplay.setRightComponent(new JScrollPane(this.info));
    }

}

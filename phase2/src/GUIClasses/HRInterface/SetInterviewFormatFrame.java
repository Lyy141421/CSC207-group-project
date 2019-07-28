package GUIClasses.HRInterface;

import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetInterviewFormatFrame extends JInternalFrame {

    int rounds = 1;

    SetInterviewFormatFrame(MethodsTheGUICallsInHR HRInterface, BranchJobPosting branchJobPosting) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.addSelectPanel();
        this.addNewRoundButton();
        this.addSubmitAndCancel();
    }

    void addSelectPanel() {
        JPanel selectPanel = new JPanel(new GridBagLayout());
        selectPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Interview round " + this.rounds));
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        JLabel formatLabel = new JLabel("Format");
        selectPanel.add(formatLabel, c);
        ButtonGroup buttonGroup = new ButtonGroup();
        c.gridx++;
        JRadioButton oneOnOne = new JRadioButton("One-on-One", true);
        selectPanel.add(oneOnOne, c);
        c.gridx++;
        JRadioButton group = new JRadioButton("Group");
        selectPanel.add(group, c);
        buttonGroup.add(oneOnOne);
        buttonGroup.add(group);

        c.gridx = 0;
        c.gridy = 1;
        JLabel descriptionLabel = new JLabel("Description");
        selectPanel.add(descriptionLabel, c);
        c.gridx++;
        c.gridwidth = 2;
        JTextField descriptionInput = new JTextField();
        selectPanel.add(descriptionInput, c);

        this.add(selectPanel);
    }

    void addNewRoundButton() {
        JButton newRoundButton = new JButton("Add one round");
        newRoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectPanel();
                //TODO: shift the action buttons down to make room for new selection panel
            }
        });

        this.add(newRoundButton);
    }

    void addSubmitAndCancel() {
        JPanel submitAndCancelPanel = new JPanel(new FlowLayout());
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        submitAndCancelPanel.add(submitButton);
        submitAndCancelPanel.add(cancelButton);

        this.add(submitAndCancelPanel);
    }
}

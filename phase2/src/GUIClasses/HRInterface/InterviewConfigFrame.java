package GUIClasses.HRInterface;

import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InterviewConfigFrame extends JInternalFrame {

    MethodsTheGUICallsInHR HRInterface;
    BranchJobPosting branchJobPosting;

    JInternalFrame container = this;

    private int rounds = 0;

    ArrayList<JRadioButton> oneOnOneButtonList = new ArrayList<>();
    ArrayList<JTextField> descriptionInputList = new ArrayList<>();

    JPanel formatPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    JButton returnButton;

    InterviewConfigFrame(MethodsTheGUICallsInHR HRInterface, BranchJobPosting branchJobPosting, JButton returnButton) {
        this.HRInterface = HRInterface;
        this.branchJobPosting = branchJobPosting;
        this.returnButton = returnButton;

        this.setLayout(new BorderLayout());
        this.formatPanel.setLayout(new BoxLayout(this.formatPanel, BoxLayout.Y_AXIS));
        this.add(new JScrollPane(this.formatPanel), BorderLayout.CENTER);
        this.buttonPanel.setLayout(new BorderLayout());
        this.add(this.buttonPanel, BorderLayout.SOUTH);

        this.addSelectPanel();
        this.addNewRoundButton();
        this.createSubmitButton();
    }

    void addSelectPanel() {
        this.rounds++;

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
        this.oneOnOneButtonList.add(oneOnOne);
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
        this.descriptionInputList.add(descriptionInput);
        selectPanel.add(descriptionInput, c);

        this.formatPanel.add(selectPanel);
    }

    void addNewRoundButton() {
        JButton newRoundButton = new JButton("Add one round");
        newRoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectPanel();
            }
        });

        this.buttonPanel.add(newRoundButton, BorderLayout.CENTER);
    }

    void createSubmitButton() {
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HRInterface.setInterviewConfiguration(branchJobPosting, getIsOneOnOne(), getDescriptions());
                JOptionPane.showInternalMessageDialog(container, "The interview configurations have been set.");
                returnButton.setVisible(true);
                dispose();
            }
        });
        submitPanel.add(submitButton);

        this.buttonPanel.add(submitPanel, BorderLayout.SOUTH);
    }

    ArrayList<Boolean> getIsOneOnOne() {
        ArrayList<Boolean> isOneOnOne = new ArrayList<>();
        for (JRadioButton oneOnOne: this.oneOnOneButtonList) {
            isOneOnOne.add(oneOnOne.isSelected());
        }

        return isOneOnOne;
    }

    ArrayList<String> getDescriptions() {
        ArrayList<String> descriptions = new ArrayList<>();
        for (JTextField description: this.descriptionInputList) {
            descriptions.add(description.getText());
        }

        return descriptions;
    }
}

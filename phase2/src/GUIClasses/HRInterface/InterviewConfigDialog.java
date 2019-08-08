package GUIClasses.HRInterface;

import CompanyStuff.JobPostings.BranchJobPosting;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class InterviewConfigDialog extends JDialog {

    HRBackend hrBackend;
    private JFrame parent;
    private JDialog container = this;
    private BranchJobPosting branchJobPosting;

    private int rounds = 0;

    private ArrayList<JRadioButton> oneOnOneButtonList = new ArrayList<>();
    private ArrayList<JTextField> descriptionInputList = new ArrayList<>();

    private JPanel formatPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    private JButton returnButton;

    InterviewConfigDialog(JFrame parent, HRBackend hrBackend, BranchJobPosting branchJobPosting, JButton returnButton) {
        super(parent, "Set interview formats");
        this.hrBackend = hrBackend;
        this.parent = parent;
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

    private void addSelectPanel() {
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
        JLabel descriptionLabel = new JLabel("Description ");
        selectPanel.add(descriptionLabel, c);
        c.gridx++;
        c.gridwidth = 2;
        JTextField descriptionInput = new JTextField(30);
        this.descriptionInputList.add(descriptionInput);
        selectPanel.add(descriptionInput, c);

        this.formatPanel.add(selectPanel);

        this.setVisible(true);
    }

    private void addNewRoundButton() {
        JPanel buttonPanel = new JPanel();
        JButton newRoundButton = new JButton("Add one round");
        newRoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectPanel();
            }
        });

        buttonPanel.add(newRoundButton);
        this.buttonPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    private void createSubmitButton() {
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hrBackend.setInterviewConfiguration(branchJobPosting, getIsOneOnOne(), getDescriptions());
                returnButton.setVisible(true);
                setModalityType(ModalityType.MODELESS);
                JOptionPane.showMessageDialog(container, "Interview Configuration Has Been Set");
                dispose();
            }
        });
        submitPanel.add(submitButton);

        this.buttonPanel.add(submitPanel, BorderLayout.SOUTH);
    }

    private ArrayList<Boolean> getIsOneOnOne() {
        ArrayList<Boolean> isOneOnOne = new ArrayList<>();
        for (JRadioButton oneOnOne : this.oneOnOneButtonList) {
            isOneOnOne.add(oneOnOne.isSelected());
        }

        return isOneOnOne;
    }

    private ArrayList<String> getDescriptions() {
        ArrayList<String> descriptions = new ArrayList<>();
        for (JTextField description : this.descriptionInputList) {
            descriptions.add(description.getText());
        }

        return descriptions;
    }
}

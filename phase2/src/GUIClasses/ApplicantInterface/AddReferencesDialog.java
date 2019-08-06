package GUIClasses.ApplicantInterface;

import ApplicantStuff.JobApplication;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

class AddReferencesDialog extends JDialog {

    private JDialog container = this;
    private ApplicantBackend applicantBackend;
    private JobApplication jobApp;

    private int numReferences = 0;

    private ArrayList<JTextField> referenceInputList = new ArrayList<>();

    private JPanel formatPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    AddReferencesDialog(JFrame frame, ApplicantBackend applicantBackend, JobApplication jobApp) {
        super(frame, "Add References");
        this.setLayout(new BorderLayout());
        this.applicantBackend = applicantBackend;
        this.jobApp = jobApp;
        this.formatPanel.setLayout(new BoxLayout(this.formatPanel, BoxLayout.Y_AXIS));
        this.add(new JScrollPane(this.formatPanel), BorderLayout.CENTER);
        this.buttonPanel.setLayout(new BorderLayout());
        this.add(this.buttonPanel, BorderLayout.SOUTH);

        this.addSelectPanel();
        this.addNewReferenceButton();
        this.createSubmitButton();
        this.setSize(new Dimension(500, 350));
        this.setResizable(false);
    }

    private void addSelectPanel() {
        this.numReferences++;

        JPanel selectPanel = new JPanel(new GridBagLayout());
        selectPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Reference " + this.numReferences));
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        JLabel descriptionLabel = new JLabel("Email: ");
        selectPanel.add(descriptionLabel, c);
        c.gridx++;
        c.gridwidth = 2;
        JTextField descriptionInput = new JTextField(null, 30);
        this.referenceInputList.add(descriptionInput);
        selectPanel.add(descriptionInput, c);
        this.formatPanel.add(selectPanel);
        this.setVisible(true);
    }

    private void addNewReferenceButton() {
        JPanel buttonPanel = new JPanel();
        JButton newReferenceButton = new JButton("Add another reference");
        newReferenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectPanel();
            }
        });

        buttonPanel.add(newReferenceButton);
        this.buttonPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    private void createSubmitButton() {
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> emails = getReferences();
                if (!emailsValid(emails)) {
                    JOptionPane.showMessageDialog(container, "One or more emails are invalid.");
                } else {
                    applicantBackend.addReferences(jobApp, emails);
                    JOptionPane.showMessageDialog(container, "References have been added.");
                    dispose();
                }
            }
        });
        submitPanel.add(submitButton);
        this.buttonPanel.add(submitPanel, BorderLayout.SOUTH);
    }

    private ArrayList<String> getReferences() {
        ArrayList<String> references = new ArrayList<>();
        for (JTextField reference : this.referenceInputList) {
            if (!reference.getText().isEmpty()) {
                references.add(reference.getText());
            }
        }
        return references;
    }

    private boolean emailsValid(ArrayList<String> emails) {
        for (String email : emails) {
            String regex = "^([A-Za-z0-9]+(?:([._\\-])[A-Za-z0-9]+|[A-Za-z0-9]*))+@([A-Za-z0-9]+\\.)+[A-Za-z]+$";
            if (!Pattern.matches(regex, email)) {
                return false;
            }
        }
        return true;
    }
}

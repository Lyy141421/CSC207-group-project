package GUIClasses.ApplicantInterface;

import CompanyStuff.JobPostings.CompanyJobPosting;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

class ApplicantBrowsePostings extends JPanel {
    private ApplicantBackend backend;
    private ApplicantPanel masterPanel;
    private ApplicantBrowsePostings thisPanel = this;

    ApplicantBrowsePostings(ApplicantBackend backend, ApplicantPanel masterPanel) {
        this.backend = backend;
        this.masterPanel = masterPanel;
        this.setLayout(null);
        this.addText();
        String[] fields = this.addFields();
        this.addSearchButton(fields);
    }

    private void addText() {
        JLabel titleText = new JLabel("Search Job Postings", SwingConstants.CENTER);
        titleText.setBounds(170, 35, 300, 40);
        titleText.setFont(new Font("Serif", Font.PLAIN, 27));
        this.add(titleText);

        JLabel companyText = new JLabel("Company :", SwingConstants.CENTER);
        companyText.setBounds(145, 110, 100, 30);

        JLabel fieldText = new JLabel("Job Field :", SwingConstants.CENTER);
        fieldText.setBounds(145, 155, 100, 30);

        JLabel idText = new JLabel("ID Number :", SwingConstants.CENTER);
        idText.setBounds(145, 200, 100, 30);

        JLabel tagsText = new JLabel("Tag(s) (e.g: Tag1, Tag2) :", SwingConstants.CENTER);
        tagsText.setBounds(87, 245, 145, 30);

        this.add(companyText); this.add(fieldText); this.add(idText); this.add(tagsText);
    }

    private String[] addFields() {
        JTextField companyField = new JTextField();
        companyField.setBounds(235, 110, 225, 30);

        JTextField fieldField = new JTextField();
        fieldField.setBounds(235, 155, 225, 30);

        JFormattedTextField idField = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
        idField.setBounds(235, 200, 225, 30);

        JTextField tagsField = new JTextField();
        tagsField.setBounds(235, 245, 225, 30);

        JCheckBox location = new JCheckBox("View nearby jobs only?");
        location.setBounds(95, 290, 200, 30);
        location.setHorizontalTextPosition(SwingConstants.LEFT);

        this.add(companyField); this.add(fieldField); this.add(idField); this.add(tagsField); this.add(location);
        return new String[]{companyField.getText(), fieldField.getText(), idField.getText(),
                tagsField.getText(), String.valueOf(location.isSelected())};
    }

    private void addSearchButton(String[] fields) {
        JButton searchButton = new JButton("Browse Postings");
        searchButton.setBounds(245, 360, 150, 30);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Pattern.matches("^[A-Za-z'\\- ]*(, [A-Za-z'\\- ]+)*$", fields[3])) {
                    JOptionPane.showMessageDialog(thisPanel, "Tags are not written in the correct format");
                } else {
                    ArrayList<CompanyJobPosting> postings = backend.findApplicablePostings(fields[0],
                            fields[1], fields[2], fields[3], Boolean.parseBoolean(fields[4]));
                    masterPanel.add(new ApplicantViewSearchResults(postings, backend,
                            masterPanel), "SearchResults");
                    ((CardLayout) masterPanel.getLayout()).show(masterPanel, "SearchResults");
                }
            }
        });
        this.add(searchButton);
    }
}

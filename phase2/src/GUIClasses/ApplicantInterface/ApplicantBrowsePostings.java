package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import CompanyStuff.JobPostings.BranchJobPosting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

class ApplicantBrowsePostings extends JPanel {
    private ApplicantBackend backend;
    private Applicant applicant;
    private ApplicantPanel masterPanel;

    ApplicantBrowsePostings(Applicant applicant, ApplicantPanel masterPanel) {
        this.applicant = applicant;
        this.backend = new ApplicantBackend(applicant);
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
        fieldText.setBounds(145, 170, 100, 30);

        JLabel idText = new JLabel("ID Number :", SwingConstants.CENTER);
        idText.setBounds(145, 230, 100, 30);

        JLabel tagsText = new JLabel("Tag(s) :", SwingConstants.CENTER);
        tagsText.setBounds(145, 290, 100, 30);

        this.add(companyText); this.add(fieldText); this.add(idText); this.add(tagsText);
    }

    private String[] addFields() {
        JTextField companyField = new JTextField();
        companyField.setBounds(235, 110, 225, 30);

        JTextField fieldField = new JTextField();
        fieldField.setBounds(235, 170, 225, 30);

        JTextField idField = new JTextField();
        idField.setBounds(235, 230, 225, 30);

        JTextField tagsField = new JTextField();
        tagsField.setBounds(235, 290, 225, 30);

        this.add(companyField); this.add(fieldField); this.add(idField); this.add(tagsField);
        String[] ret = {companyField.getText(), fieldField.getText(), idField.getText(), tagsField.getText()};
        return ret;
    }

    private void addSearchButton(String[] fields) {
        JButton searchButton = new JButton("Browse Postings");
        searchButton.setBounds(245, 360, 150, 30);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<BranchJobPosting> postings =
                        backend.findApplicablePostings(fields[0], fields[1], fields[2], fields[3]);
                masterPanel.add(new ApplicantViewSearchResults(postings, backend), "SEARCH_RESULTS");
                ((CardLayout)masterPanel.getLayout()).show(masterPanel, "SEARCH_RESULTS");
            }
        });
        this.add(searchButton);
    }
}

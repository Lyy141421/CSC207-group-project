package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.ActionListeners.SubmitDocumentsActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.HashMap;

public class ApplicantTextDocSubmission extends JPanel {

    Applicant applicant;
    JobApplication jobApp;

    ApplicantTextDocSubmission(Applicant applicant, JobApplication jobApp) {
        this.applicant = applicant;
        this.jobApp = jobApp;

        JLabel titleText = new JLabel("Document Submission");
        titleText.setFont(new Font("Serif", Font.PLAIN, 22));
        titleText.setBounds(327, 20, 200, 40);

        JLabel resumeText = new JLabel("Please paste your resume here:");
        resumeText.setBounds(150, 75, 554, 20);

        JTextArea resumeArea = new JTextArea();
        JScrollPane resumeScroll = new JScrollPane(resumeArea);
        resumeScroll.setBounds(150, 95, 554, 125);

        JLabel coverText = new JLabel("Please paste your cover letter here:");
        coverText.setBounds(150, 235, 554, 20);

        JTextArea coverArea = new JTextArea();
        JScrollPane coverScroll = new JScrollPane(coverArea);
        coverScroll.setBounds(150, 255, 554, 125);

        JButton submit = new JButton("Apply!");
        submit.setBounds(387, 400, 80, 30);

        HashMap<String, JTextArea> fileTypeToContents = new HashMap<String, JTextArea>() {{
            put("resume", (JTextArea) resumeScroll.getViewport().getView());
            put("coverLetter", (JTextArea) coverScroll.getViewport().getView());
        }};
        submit.addActionListener(new SubmitDocumentsActionListener(applicant, jobApp, fileTypeToContents));

        this.add(titleText); this.add(submit);
        this.add(resumeText); this.add(resumeScroll);
        this.add(coverText); this.add(coverScroll);
    }
}

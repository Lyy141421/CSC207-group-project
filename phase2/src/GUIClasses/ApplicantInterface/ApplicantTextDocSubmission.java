package GUIClasses.ApplicantInterface;

import ApplicantStuff.JobApplication;
import GUIClasses.ActionListeners.SubmitDocumentsActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

class ApplicantTextDocSubmission extends JPanel {

    ApplicantTextDocSubmission(JPanel masterPanel, ApplicantBackend backend, JobApplication jobApp) {
        this.setLayout(null);

        JLabel titleText = new JLabel("Document Submission", SwingConstants.CENTER);
        titleText.setFont(new Font("Serif", Font.PLAIN, 22));
        titleText.setBounds(227, 20, 400, 40);

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
        submit.setBounds(470, 400, 80, 30);

        JButton returnButton = new JButton("Back");
        returnButton.setBounds(20, 20, 80, 30);

        JButton addReferencesButton = new JButton("Add References");
        addReferencesButton.setBounds(300, 400, 150, 30);

        HashMap<String, JTextArea> fileTypeToContents = new HashMap<String, JTextArea>() {{
            put("resume", (JTextArea) resumeScroll.getViewport().getView());
            put("coverLetter", (JTextArea) coverScroll.getViewport().getView());
        }};
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SubmitDocumentsActionListener(masterPanel, jobApp.getApplicant(), jobApp, fileTypeToContents).actionPerformed(e);
            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) masterPanel.getLayout()).show(masterPanel, "SearchResults");
            }
        });
        addReferencesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.windowForComponent(masterPanel);
                new AddReferencesDialog(frame, backend, jobApp);
            }
        });

        this.add(titleText); this.add(submit);
        this.add(resumeText); this.add(resumeScroll);
        this.add(coverText);
        this.add(coverScroll);
        this.add(returnButton);
        this.add(addReferencesButton);
    }
}
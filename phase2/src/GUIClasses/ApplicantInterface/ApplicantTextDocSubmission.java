package GUIClasses.ApplicantInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplicantTextDocSubmission extends JPanel {

    ApplicantTextDocSubmission() {
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
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: Backend
            }
        } );

        this.add(titleText); this.add(submit);
        this.add(resumeText); this.add(resumeScroll);
        this.add(coverText); this.add(coverScroll);
    }
}

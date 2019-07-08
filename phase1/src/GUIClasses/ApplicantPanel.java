package GUIClasses;

import UsersAndJobObjects.JobPosting;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * JPanel containing all of the GUI needed for Applicant users functionality
 */

class ApplicantPanel extends JPanel{
    ApplicantPanel() {
        super(new CardLayout());

        JPanel applicantStart = this.buildStartPanel();

        JPanel viewJobs = this.buildViewJobs(new JobPosting[0]); //TODO: Pass proper list

        JPanel viewApps = new JPanel(new GridLayout(1, 3)); //TODO: Implement construction

        this.add(applicantStart, "applicantStart");
        this.add(viewJobs, "viewJobs");
        this.add(viewApps, "viewApps");
    }

    /**
     * Builds and returns the basic start panel; what the applicant sees on login
     */
    private JPanel buildStartPanel() {
        JPanel applicantStart = new JPanel(new GridLayout(3, 1));

        JPanel startTitle = new JPanel(null);
        JLabel titleText = new JLabel("Applicant Portal", SwingConstants.CENTER);
        titleText.setBounds(227, 70, 400, 70);
        titleText.setFont(new Font("Serif", Font.PLAIN, 25));
        startTitle.add(titleText);

        JPanel startForm = this.startPanelForm();

        JPanel startMngButton = new JPanel(null);
        JButton manageApps = new JButton("Manage applications");
        manageApps.setBounds(327, 40, 200, 20);
        startMngButton.add(manageApps);

        applicantStart.add(startTitle); applicantStart.add(startForm); applicantStart.add(startMngButton);
        return applicantStart;
    }

    /**
     * Helper for buildStartPanel, takes care of the form panel in the center
     */
    private JPanel startPanelForm() {
        JPanel startForm = new JPanel(null);

        JLabel fieldText = new JLabel("Search by job field");
        fieldText.setBounds(227, 35, 150, 20);
        startForm.add(fieldText);

        JTextField fieldEntry = new JTextField();
        fieldEntry.setBounds(227, 60, 150, 20);
        startForm.add(fieldEntry);

        JButton fieldButton = new JButton("Find jobs");
        fieldButton.setBounds(252, 100, 100, 20);
        startForm.add(fieldButton);

        JLabel companyText = new JLabel("Search by company");
        companyText.setBounds(477, 35, 150, 20);
        startForm.add(companyText);

        JTextField companyEntry = new JTextField();
        companyEntry.setBounds(477, 60, 150, 20);
        startForm.add(companyEntry);

        JButton companyButton = new JButton("Find jobs");
        companyButton.setBounds(502, 100, 100, 20);
        startForm.add(companyButton);

        return startForm;
    }

    /**
     * Builds the panel which displays all jobs the applicant can apply to.
     * @param jobPostings postings the applicant has NOT applied to
     */
    private JPanel buildViewJobs(JobPosting[] jobPostings) {
        JPanel viewJobs = new JPanel(new GridLayout(1, 3));

        JPanel viewJobs0 = this.buildViewJobs0(jobPostings); viewJobs.add(viewJobs0);

        JPanel viewJobs1 = this.buildViewJobs1(jobPostings); viewJobs.add(viewJobs1);

        JPanel viewJobs2 = this.buildViewJobs2(jobPostings); viewJobs.add(viewJobs2);

        JList<String> jobTitlesList = new JList<>();
        for(Component c : viewJobs0.getComponents()) {
            if(c instanceof JList) {
                jobTitlesList = (JList<String>)c;
            }
        }
        jobTitlesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList actor =(JList)e.getSource();
                String cardFromActor = actor.getSelectedValue().toString().replaceAll("[^\\d.]", "");
                CardLayout c1 = (CardLayout)viewJobs1.getLayout();
                c1.show(viewJobs1, cardFromActor);
                CardLayout c2 = (CardLayout)viewJobs2.getLayout();
                c2.show(viewJobs2, cardFromActor);
            }
        });

        return viewJobs;
    }

    /**
     * Builds the panel which allows navigation when viewing jobs
     */
    private JPanel buildViewJobs0(JobPosting[] jobPostings) {
        JPanel viewJobsList = new JPanel(null);

        String[] jobTitleArray = this.getListNames(jobPostings);
        JList<String> jobTitlesList = new JList<>(jobTitleArray);
        jobTitlesList.setBounds(25, 40, 234, 360);
        viewJobsList.add(jobTitlesList);

        JLabel jobTitlesText = new JLabel("Found the following job postings:", SwingConstants.CENTER);
        jobTitlesText.setBounds(42, 10, 200, 20);
        viewJobsList.add(jobTitlesText);

        JButton viewJobsExit = new JButton("Back");
        viewJobsExit.setBounds(92, 415, 100, 20);
        viewJobsList.add(viewJobsExit);

        return viewJobsList;
    }

    /**
     * Builds and returns the panel which contains all job titles, company and descriptions
     */
    private JPanel buildViewJobs1(JobPosting[] jobPostings) {
        JPanel viewJobs1 = new JPanel(new CardLayout());

        for(JobPosting j : jobPostings) {
            JPanel viewJobsAdded = new JPanel(null);

            JLabel viewJobTitle = new JLabel(j.getTitle());
            viewJobTitle.setFont(new Font("Serif", Font.PLAIN, 20));
            viewJobTitle.setBounds(17, 70, 150, 40);
            viewJobsAdded.add(viewJobTitle);

            JLabel viewJobCompany = new JLabel(j.getCompany().getName());
            viewJobCompany.setFont(new Font("Serif", Font.BOLD, 15));
            viewJobCompany.setBounds(17, 100, 150, 30);
            viewJobsAdded.add(viewJobCompany);

            JLabel viewJobDesc = new JLabel(j.getDescription());
            viewJobDesc.setBounds(17, 150, 250, 200);
            viewJobDesc.setVerticalAlignment(JLabel.TOP);
            viewJobsAdded.add(viewJobDesc);

            viewJobs1.add(viewJobsAdded, j.getId());
        }

        return viewJobs1;
    }

    /**
     * Returns the panel containing the remainder of the details for each job posting
     */
    private JPanel buildViewJobs2(JobPosting[] jobpostings) {
        JPanel viewJobs2 = new JPanel(new CardLayout());

        for(JobPosting j : jobpostings) {
            JPanel viewJobsAdded2 = new JPanel(null);

            JLabel viewJobReqs = new JLabel("Requirements: " + j.getRequirements());
            viewJobReqs.setBounds(17, 150, 250, 20);
            viewJobsAdded2.add(viewJobReqs);

            JLabel viewJobPos = new JLabel("# Of Positions: " + j.getNumPositions());
            viewJobPos.setBounds(17, 170, 250, 20);
            viewJobsAdded2.add(viewJobPos);

            JLabel viewJobDatePosted = new JLabel("Date Posted: " + j.getPostDate().toString());
            viewJobDatePosted.setBounds(17, 190, 250, 20);
            viewJobsAdded2.add(viewJobDatePosted);

            JLabel viewJobDeadline = new JLabel("Deadline to Apply: " + j.getCloseDate().toString());
            viewJobDeadline.setBounds(17, 210, 250, 20);
            viewJobsAdded2.add(viewJobDeadline);

            JLabel viewJobIDNum = new JLabel("Posting ID: " + j.getId());
            viewJobIDNum.setBounds(17, 230, 250, 20);
            viewJobsAdded2.add(viewJobIDNum);

            JButton applyForJob = new JButton("Apply now");
            applyForJob.setBounds(45, 300, 100, 20);
            viewJobsAdded2.add(applyForJob);
            viewJobs2.add(viewJobsAdded2);
        }

        return viewJobs2;
    }

    /**
     * Takes a list of postings and converts them to Name - ID form for card/navigation purposes
     * @param jobPostings the postings in question
     */
    private String[] getListNames(JobPosting[] jobPostings) {
        int len = jobPostings.length;
        String[] ret = new String[len];

        for(int i=0; i < len - 1; i++) {
            ret[i] = jobPostings[i].getTitle() + " - " + jobPostings[i].getId();
        }

        return ret;
    }

    public static void main(String[] args) {
        JFrame tester = new JFrame("Interface Test");
        ApplicantPanel test = new ApplicantPanel(); tester.add(test);
        tester.setSize(854, 480); tester.setVisible(true);
    }
}

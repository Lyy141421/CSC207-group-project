package GUIClasses;

import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.JobPosting;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * JPanel containing all of the GUI needed for Applicant users functionality
 */

class ApplicantPanel extends JPanel{
    private LocalDate date;
    private ApplicantInterfaceTest BackEnd;
    private CardLayout masterLayout;
    private Container parent;
    private Applicant loggedUser;
    private JTextField searchBar;

    ApplicantPanel(String username, LocalDate date, Container parent, CardLayout masterLayout) {
        super(new CardLayout());

        //this.loggedUser = (Applicant)JobApplicationSystem.getUserManager().findUserByUsername(username); TODO uncomment
        this.loggedUser = new Applicant();
        this.date = date;
        this.BackEnd = new ApplicantInterfaceTest(this.loggedUser);
        this.parent = parent;
        this.masterLayout = masterLayout;

        JPanel applicantStart = this.buildStartPanel();

        JPanel viewJobs = this.buildViewJobs(new ArrayList<>()); //TODO: Pass proper list

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

        JPanel startTitle = this.buildTitle();

        JPanel startForm = this.buildStartForm();

        JPanel welcomeLabels = this.buildWelcome();

        applicantStart.add(startTitle); applicantStart.add(startForm); applicantStart.add(welcomeLabels);
        return applicantStart;
    }

    /**
     * Helpers to build Applicant start panel
     */
    private JPanel buildStartForm() {
        JPanel startForm = new JPanel(null);

        JTextField infoEntry = new JTextField("Looking for a job?");
        infoEntry.setBounds(252, 40, 350, 28);
        infoEntry.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                infoEntry.setText("");
            }
        });
        startForm.add(infoEntry); this.searchBar = infoEntry;

        JButton fieldButton = new JButton("Search by field");
        fieldButton.setBounds(267, 90, 150, 25);
        fieldButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(infoEntry.getText().equals("") || infoEntry.getText().equals("Looking for a job?")) {
                    ArrayList<JobPosting> postings = BackEnd.findAppliablePostings
                            (date, null, null);
                    JPanel newPanel = buildViewJobs(postings);
                    add(newPanel, "viewJobs");
                    ((CardLayout)getLayout()).show(getThis(), "viewJobs");
                    resetSearch();
                } else {
                    ArrayList<JobPosting> postings = BackEnd.findAppliablePostings
                            (date, infoEntry.getText(), null);
                    JPanel newPanel = buildViewJobs(postings);
                    add(newPanel, "viewJobs");
                    ((CardLayout)getLayout()).show(getThis(), "viewJobs");
                    resetSearch();
                }
            }
        });
        startForm.add(fieldButton);

        JButton companyButton = new JButton("Search by company");
        companyButton.setBounds(437, 90, 150, 25);
        companyButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(infoEntry.getText().equals("") || infoEntry.getText().equals("Looking for a job?")) {
                    ArrayList<JobPosting> postings = BackEnd.findAppliablePostings
                            (date, null, null);
                    JPanel newPanel = buildViewJobs(postings);
                    add(newPanel, "viewJobs");
                    ((CardLayout)getLayout()).show(getThis(), "viewJobs");
                    resetSearch();
                } else {
                    ArrayList<JobPosting> postings = BackEnd.findAppliablePostings
                            (date, null, infoEntry.getText());
                    JPanel newPanel = buildViewJobs(postings);
                    add(newPanel, "viewJobs");
                    ((CardLayout)getLayout()).show(getThis(), "viewJobs");
                    resetSearch();
                }
            }
        });
        startForm.add(companyButton);

        return startForm;
    }

    private JPanel buildTitle() {
        JPanel startTitle = new JPanel(null);

        JButton logOut = new JButton("Logout");
        logOut.setBounds(5, 5, 80, 20);
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                masterLayout.show(parent, "LOGIN");
            }
        });
        startTitle.add(logOut);

        JButton manageFiles = new JButton("Files");
        manageFiles.setBounds(90, 5, 80, 20);
        startTitle.add(manageFiles);

        JButton viewApps = new JButton("View Applications");
        viewApps.setBounds(675, 5, 160, 20);
        startTitle.add(viewApps);

        JLabel titleText = new JLabel("Job Applicant Portal", SwingConstants.CENTER);
        titleText.setBounds(227, 110, 400, 40);
        titleText.setFont(new Font("Serif", Font.PLAIN, 27));
        startTitle.add(titleText);

        return startTitle;
    }

    private JPanel buildWelcome() {
        JPanel welcomeLabels = new JPanel(null);

        JLabel feelingLucky = new JLabel("Feeling lucky, " + this.loggedUser.getLegalName()
                + "?", SwingConstants.CENTER);
        feelingLucky.setFont(new Font("Serif", Font.PLAIN, 14));
        feelingLucky.setBounds(227, 0, 400, 20);
        welcomeLabels.add(feelingLucky);

        JLabel tutorialLabel = new JLabel("Leave the search bar blank " +
                "to view all postings!", SwingConstants.CENTER);
        tutorialLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        tutorialLabel.setBounds(227, 20, 400, 20);
        welcomeLabels.add(tutorialLabel);

        return welcomeLabels;
    }

    /**
     * Builds the panel which displays all jobs the applicant can apply to.
     * @param jobPostings postings the applicant has NOT applied to
     */
    private JPanel buildViewJobs(ArrayList<JobPosting> jobPostings) {
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
    private JPanel buildViewJobs0(ArrayList<JobPosting> jobPostings) {
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
        viewJobsExit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ((CardLayout)getLayout()).show(getThis(), "applicantStart");
            }
        });
        viewJobsList.add(viewJobsExit);

        return viewJobsList;
    }

    /**
     * Builds and returns the panel which contains all job titles, company and descriptions
     */
    private JPanel buildViewJobs1(ArrayList<JobPosting> jobPostings) {
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
    private JPanel buildViewJobs2(ArrayList<JobPosting> jobpostings) {
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
    private String[] getListNames(ArrayList<JobPosting> jobPostings) {
        int len = jobPostings.size();
        String[] ret = new String[len];

        for(int i=0; i < len - 1; i++) {
            ret[i] = jobPostings.get(i).getTitle() + " - " + jobPostings.get(i).getId();
        }

        return ret;
    }

    private void resetSearch() {
        this.searchBar.setText("Looking for a job?");
    }

    /**
     * Because nested methods
     */
    private ApplicantPanel getThis() {
        return this;
    }

    public static void main(String[] args) {
        JFrame tester = new JFrame("Interface Test");
        ApplicantPanel test = new ApplicantPanel("i dont exist", LocalDate.now(),
                new Container(), new CardLayout()); tester.add(test);
        tester.setSize(854, 480); tester.setVisible(true); tester.setResizable(false);
    }
}

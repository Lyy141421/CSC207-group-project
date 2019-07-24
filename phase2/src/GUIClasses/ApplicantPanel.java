package GUIClasses;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;
import FileLoadingAndStoring.DataLoaderAndStorer;
import Main.JobApplicationSystem;

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

class ApplicantPanel extends JPanel {
    private JobApplicationSystem jobAppSystem;
    private LocalDate date;
    private ApplicantInterfaceTest backend;
    private CardLayout masterLayout;
    private Container parent;
    private Applicant loggedUser;
    private JTextField searchBar;

    public ApplicantPanel(String username, JobApplicationSystem jobAppSystem, Container parent, CardLayout masterLayout) {
        super(new CardLayout());

        this.loggedUser = (Applicant) jobAppSystem.getUserManager().findUserByUsername(username);
        this.date = jobAppSystem.getToday();
        this.backend = new ApplicantInterfaceTest(this.loggedUser);
        this.parent = parent;
        this.masterLayout = masterLayout;

        JPanel applicantStart = this.buildStartPanel();

        JPanel history = this.buildHistory();

        this.add(applicantStart, "applicantStart");
        this.add(history, "accountHistory");

        if (backend.checkUpcomingInterviews(date)) {
            this.add(this.buildReminder());
        }
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
                    ArrayList<CompanyJobPosting> postings = backend.findApplicablePostings
                            (date, null, null);
                    JPanel newPanel = buildViewJobs(postings);
                    add(newPanel, "viewJobs");
                    ((CardLayout)getLayout()).show(getThis(), "viewJobs");
                    resetSearch();
                } else {
                    ArrayList<CompanyJobPosting> postings = backend.findApplicablePostings
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
                    ArrayList<JobPosting> postings = backend.findApplicablePostings
                            (date, null, null);
                    JPanel newPanel = buildViewJobs(postings);
                    add(newPanel, "viewJobs");
                    ((CardLayout)getLayout()).show(getThis(), "viewJobs");
                    resetSearch();
                } else {
                    ArrayList<JobPosting> postings = backend.findApplicablePostings
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
                new DataLoaderAndStorer(jobAppSystem);
            }
        });
        startTitle.add(logOut);

        JButton manageFiles = new JButton("Files"); //TODO Phase 2
        manageFiles.setBounds(590, 5, 80, 20);
        startTitle.add(manageFiles);

        JButton history = new JButton("Account History");
        history.setBounds(90, 5, 140, 20);
        history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout)(getThis().getLayout())).show(getThis(), "accountHistory");
            }
        });
        startTitle.add(history);

        JButton viewApps = new JButton("View Applications");
        viewApps.setBounds(675, 5, 160, 20);
        viewApps.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel viewApps = buildViewApps(backend.helperPostings(), backend.getApps());
                getThis().add(viewApps, "viewApps");
                ((CardLayout)(getThis().getLayout())).show(getThis(), "viewApps");
            }
        });
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
    private JPanel buildViewJobs0(ArrayList<CompanyJobPosting> jobPostings) {
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
    private JPanel buildViewJobs1(ArrayList<CompanyJobPosting> jobPostings) {
        JPanel viewJobs1 = new JPanel(new CardLayout());

        for (CompanyJobPosting j : jobPostings) {
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
    private JPanel buildViewJobs2(ArrayList<CompanyJobPosting> jobpostings) {
        JPanel viewJobs2 = new JPanel(new CardLayout());

        for (CompanyJobPosting j : jobpostings) {
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

            JLabel viewJobDeadline = new JLabel("Deadline to Apply: " + j.getApplicantCloseDate().toString());
            viewJobDeadline.setBounds(17, 210, 250, 20);
            viewJobsAdded2.add(viewJobDeadline);

            JLabel viewJobIDNum = new JLabel("Posting ID: " + j.getId());
            viewJobIDNum.setBounds(17, 230, 250, 20);
            viewJobsAdded2.add(viewJobIDNum);

            JButton applyForJob = new JButton("Apply now");
            applyForJob.setBounds(45, 300, 100, 20);
            applyForJob.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JPanel docPanel = buildDocApply(j);
                    add(docPanel, "FORMS");
                    ((CardLayout)getLayout()).show(getThis(), "FORMS");
                }
            } );
            viewJobsAdded2.add(applyForJob);
            viewJobs2.add(viewJobsAdded2, j.getId());
        }

        return viewJobs2;
    }

    /**
     * Builds the document submission panel for a job posting
     */
    private JPanel buildDocApply(BranchJobPosting posting) {
        JPanel formEntry = new JPanel(null);

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
                JobApplication app = backend.createAppThroughInput(resumeArea.getText(), coverArea.getText(), posting);
                posting.addJobApplication(app);
                loggedUser.registerJobApplication(app);
                ((CardLayout)getLayout()).show(getThis(), "applicantStart");
            }
        } );

        formEntry.add(titleText); formEntry.add(submit);
        formEntry.add(resumeText); formEntry.add(resumeScroll);
        formEntry.add(coverText); formEntry.add(coverScroll);

        return formEntry;
    }

    /**
     * Builds the panel which displays current applications
*/
    private JPanel buildViewApps(ArrayList<JobPosting> jobPostings, ArrayList<JobApplication> applications) {
        JPanel viewApps = new JPanel(new GridLayout(1, 3));

        JPanel viewApps0 = this.buildViewJobs0(jobPostings); viewApps.add(viewApps0);

        JPanel viewApps1 = this.buildViewJobs1(jobPostings); viewApps.add(viewApps1);

        JPanel viewApps2 = this.buildViewApps0(applications); viewApps.add(viewApps2);

        JList<String> jobTitlesList = new JList<>();
        for(Component c : viewApps0.getComponents()) {
            if(c instanceof JList) {
                jobTitlesList = (JList<String>)c;
            }
        }
        jobTitlesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList actor =(JList)e.getSource();
                String cardFromActor = actor.getSelectedValue().toString().replaceAll("[^\\d.]", "");
                CardLayout c1 = (CardLayout)viewApps1.getLayout();
                c1.show(viewApps1, cardFromActor);
                CardLayout c2 = (CardLayout)viewApps2.getLayout();
                c2.show(viewApps2, cardFromActor);
            }
        });

        return viewApps;
    }

    /**
     * Builds status and withdraw aspect of view applications
     */
    private JPanel buildViewApps0(ArrayList<JobApplication> applications) {
        JPanel viewApps = new JPanel(new CardLayout());

        for(JobApplication app : applications) {
            JPanel viewAppsAdded = new JPanel(null);

            JLabel viewJobReqs = new JLabel("Status: " + app.getStatus());
            viewJobReqs.setBounds(17, 150, 250, 20);
            viewAppsAdded.add(viewJobReqs);

            JButton applyForJob = new JButton("Apply now");
            applyForJob.setBounds(45, 300, 100, 20);
            applyForJob.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean success = backend.withdrawApp(app);
                    getThis().buildWithdrawWindow(success);
                }
            } );
            viewApps.add(viewAppsAdded, app.getJobPosting().getId());
        }

        return viewApps;
    }

    /**
     * Builds reminder dialog if the applicant has an interview on "today"
     */
    private JDialog buildReminder() {
        JDialog d = new JDialog(new JFrame() , "Reminder", true);
        d.setLayout( new FlowLayout() );
        JButton b = new JButton ("OK");
        b.addActionListener ( new ActionListener() {
            public void actionPerformed( ActionEvent e )
            {
                d.setVisible(false);
            }
        });
        d.add( new JLabel ("You have interview(s) today!"), SwingConstants.CENTER);
        d.add(b);
        d.setSize(300,80);
        d.setVisible(true);

        return d;
    }

    /**
     * Builds dialog after attempting to withdraw an application
     */
    private JDialog buildWithdrawWindow(boolean success) {
        JDialog d = new JDialog(new JFrame(), "Withdraw Status", true);
        String text;

        d.setLayout( new FlowLayout() );
        JButton b = new JButton ("Back");
        b.addActionListener ( new ActionListener() {
            public void actionPerformed( ActionEvent e )
            {
                d.setVisible(false);
                ((CardLayout)(getThis().getLayout())).show(getThis(), "applicantStart");
            }
        });
        if (success) {text = "Application withdrawn";} else {text = "Can't withdraw - posting filled";}
        d.add( new JLabel (text), SwingConstants.CENTER);
        d.add(b);
        d.setSize(400,80);
        d.setVisible(true);

        return d;
    }

    /**
     * Builds account history
     */
    private JPanel buildHistory() {
        JPanel viewHistory = new JPanel(null);

        JButton logOut = new JButton("Back");
        logOut.setBounds(387, 10, 80, 25);
        logOut.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ((CardLayout)getLayout()).show(getThis(), "applicantStart");
            }
        });
        viewHistory.add(logOut);

        JLabel infoBox = new JLabel("Account Created: " + loggedUser.getDateCreated());
        infoBox.setBounds(100, 50, 400, 20);
        viewHistory.add(infoBox);

        JLabel daysSince = new JLabel(backend.daysSince(date));
        daysSince.setBounds(100, 70, 400, 20);
        viewHistory.add(daysSince);

        if(!daysSince.getText().equals("You have not yet submitted any job applications.")) {
            JLabel appHistory = new JLabel(backend.oldApps());
            appHistory.setVerticalAlignment(SwingConstants.TOP);
            appHistory.setBounds(100, 90, 400, 350);
            viewHistory.add(appHistory);
        }

        return viewHistory;
    }

    /**
     * Takes a list of postings and converts them to Name - ID form for card/navigation purposes
     * @param jobPostings the postings in question
     */
    private String[] getListNames(ArrayList<BranchJobPosting> jobPostings) {
        int len = jobPostings.size();
        String[] ret = new String[len];

        for(int i=0; i < len; i++) {
            ret[i] = jobPostings.get(i).getTitle() + " - " + jobPostings.get(i).getId();
        }

        return ret;
    }

    /**
     * Resets the search bar to its "default" value
     */
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
        JobApplicationSystem jobAppSystem = new JobApplicationSystem();
        jobAppSystem.setToday(LocalDate.now());
        ApplicantPanel test = new ApplicantPanel("i dont exist", jobAppSystem,
                new Container(), new CardLayout()); tester.add(test);
        tester.setSize(854, 480); tester.setVisible(true); tester.setResizable(false);
    }
}

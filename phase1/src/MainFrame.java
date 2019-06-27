import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainFrame extends JFrame {

    public MainFrame () {
        super("GET A JOB");
        initUI();
    }

    private void initUI () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLayout(new CardLayout());
        addCards();
        setVisible(true);
        setResizable(false);
    }

    // Call methods that create each interface and add to main frame.
    private void addCards () {
        add(LoginInterface());
        add(HRInterface());
    }

    // Create interface for login screen
    private JPanel LoginInterface() {
        JPanel loginScreen = new JPanel(null);
        JLabel welcomeLabel = new JLabel("CSC207 Summer 2019 Job Application System", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 30));
            welcomeLabel.setBounds(320, 140, 640, 90);
            //welcomeLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        JLabel userNameText = new JLabel("Username: ", SwingConstants.CENTER);
            userNameText.setBounds(540, 340, 100, 30);
        JTextField userNameEntry = new JTextField();
            userNameEntry.setBounds(640, 340, 100, 30);
        JLabel passwordText = new JLabel("Password: ", SwingConstants.CENTER);
            passwordText.setBounds(540, 375, 100, 30);
        JPasswordField passwordEntry = new JPasswordField();
            passwordEntry.setBounds(640, 375, 100, 30);
        JButton loginButton = new JButton("Login/Register");
            loginButton.setBounds(580, 420, 120, 20);

        JLabel createNewText = new JLabel("User not found.", SwingConstants.CENTER);
            createNewText.setBounds(540, 420, 100, 20);
        JButton createNewButton = new JButton("Create?");
            createNewButton.setBounds(640, 420, 100, 20);

        JLabel wrongPass = new JLabel("Incorrect password!", SwingConstants.CENTER);
            wrongPass.setBounds(580, 305, 120, 30);

        loginScreen.add(welcomeLabel); loginScreen.add(userNameText); loginScreen.add(passwordText);
        loginScreen.add(userNameEntry); loginScreen.add(passwordEntry); loginScreen.add(loginButton);
        //loginScreen.add(createNewText); loginScreen.add(createNewButton); loginButton.setVisible(false);
        //loginScreen.add(wrongPass);

        return loginScreen;
    }

    // Create interface for new User account
    private JPanel NewUserInterface() { //TODO: Split into helper functions
        JPanel createUser = new JPanel();
        createUser.setLayout(new GridLayout(3, 1));

        JPanel selectorPanel = new JPanel(); selectorPanel.setLayout(null);
        JPanel formPanel = new JPanel(); formPanel.setLayout(new CardLayout());
        JPanel buttonPanel = new JPanel(); buttonPanel.setLayout(null);

        //Selector panel
        JLabel titleText = new JLabel("Create New Account", SwingConstants.CENTER);
        titleText.setBounds(440, 75, 400, 90);
        titleText.setFont(new Font("Serif", Font.PLAIN, 30));
        selectorPanel.add(titleText);
        JLabel typeSelector = new JLabel("What type of account is being created?", SwingConstants.CENTER);
        typeSelector.setBounds(465, 200, 250, 30);
        selectorPanel.add(typeSelector);
        String[] userTypes = {"Applicant", "Interviewer", "HR Coordinator"};
        JComboBox selectorBox = new JComboBox(userTypes);
        selectorBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout c1 = (CardLayout)(formPanel.getLayout()); c1.show(formPanel, (String)selectorBox.getSelectedItem());
            }
        });
        selectorBox.setBounds(715, 200, 100, 30);
        selectorPanel.add(selectorBox);

        //Form panel - Applicant Card
        JPanel applicantCard = new JPanel(); applicantCard.setLayout(null);
        JLabel appPassText = new JLabel("Password: ", SwingConstants.CENTER);
        appPassText.setBounds(480, 45, 150, 30);
        applicantCard.add(appPassText);
        JPasswordField appPassEntry = new JPasswordField();
        appPassEntry.setBounds(650, 45, 150, 30);
        applicantCard.add(appPassEntry);
        JLabel appNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        appNameText.setBounds(480, 105, 150, 30);
        applicantCard.add(appNameText);
        JTextField appNameEntry = new JTextField();
        appNameEntry.setBounds(650, 105, 150, 30);
        applicantCard.add(appNameEntry);
        JLabel appEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        appEmailText.setBounds(480, 165, 150, 30);
        applicantCard.add(appEmailText);
        JTextField appEmailEntry = new JTextField();
        appEmailEntry.setBounds(650, 165, 150, 30);
        applicantCard.add(appEmailEntry);
        //Form panel - Interviewer card
        JPanel interviewerCard = new JPanel(); interviewerCard.setLayout(null);
        JLabel intPassText = new JLabel("Password: ", SwingConstants.CENTER);
        intPassText.setBounds(480, 45, 150, 30);
        interviewerCard.add(intPassText);
        JPasswordField intPassEntry = new JPasswordField();
        intPassEntry.setBounds(650, 45, 150, 30);
        interviewerCard.add(intPassEntry);
        JLabel intEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        intEmailText.setBounds(480, 105, 150, 30);
        interviewerCard.add(intEmailText);
        JTextField intEmailEntry = new JTextField();
        intEmailEntry.setBounds(650, 105, 150, 30);
        interviewerCard.add(intEmailEntry);
        JLabel intCompanyText = new JLabel("Company: ", SwingConstants.CENTER);
        intCompanyText.setBounds(480, 165, 150, 30);
        interviewerCard.add(intCompanyText);
        JTextField intCompanyEntry = new JTextField();
        intCompanyEntry.setBounds(650, 165, 150, 30);
        interviewerCard.add(intCompanyEntry);
        //Form panel - HRC card
        JPanel HRCCard = new JPanel(); HRCCard.setLayout(null);
        JLabel hrcPassText = new JLabel("Password: ", SwingConstants.CENTER);
        hrcPassText.setBounds(480, 33, 150, 30);
        HRCCard.add(hrcPassText);
        JPasswordField hrcPassEntry = new JPasswordField();
        hrcPassEntry.setBounds(650, 33, 150, 30);
        HRCCard.add(hrcPassEntry);
        JLabel hrcNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        hrcNameText.setBounds(480, 81, 150, 30);
        HRCCard.add(hrcNameText);
        JTextField hrcNameEntry = new JTextField();
        hrcNameEntry.setBounds(650, 81, 150, 30);
        HRCCard.add(hrcNameEntry);
        JLabel hrcEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        hrcEmailText.setBounds(480, 129, 150, 30);
        HRCCard.add(hrcEmailText);
        JTextField hrcEmailEntry = new JTextField();
        hrcEmailEntry.setBounds(650, 129, 150, 30);
        HRCCard.add(hrcEmailEntry);
        JLabel hrcCompanyText = new JLabel("Company: ", SwingConstants.CENTER);
        hrcCompanyText.setBounds(480, 177, 150, 30);
        HRCCard.add(hrcCompanyText);
        JTextField hrcCompanyEntry = new JTextField();
        hrcCompanyEntry.setBounds(650, 177, 150, 30);
        HRCCard.add(hrcCompanyEntry);
        //Form panel - add all
        formPanel.add(applicantCard, "Applicant");
        formPanel.add(interviewerCard, "Interviewer");
        formPanel.add(HRCCard, "HR Coordinator");
        //Button panel
        JButton submitButton = new JButton("Create Account");
        submitButton.setBounds(565, 15, 150, 30);
        buttonPanel.add(submitButton);
        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(565, 170, 150, 30);
        buttonPanel.add(backButton);

        createUser.add(selectorPanel); createUser.add(formPanel); createUser.add(buttonPanel);
        
        return createUser;
    }

    // Create interface for HR
    private JPanel HRInterface () {
        setTitle("HR Coordinator");
        JPanel HRPanel = new JPanel();
        HRPanel.setLayout(new CardLayout());
        HRPanel.add(HRHome());
        HRPanel.add(HRBrowsePosting());

        return HRPanel;
    }

    private JPanel HRHome () {
        JPanel HomePanel = new JPanel();
        HomePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JButton toDo = new JButton("To-Do");

        c.gridx = 0;
        c.gridy = 0;
        HomePanel.add(toDo, c);


        JPanel manual = new JPanel();
        manual.setLayout(new BoxLayout(manual, BoxLayout.Y_AXIS));
        manual.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

        JButton browsePosting = new JButton("Browse all job postings");
        JButton searchApplicant = new JButton("Search applicant");
        JButton addPosting = new JButton("Add job posting");

        browsePosting.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchApplicant.setAlignmentX(Component.CENTER_ALIGNMENT);
        addPosting.setAlignmentX(Component.CENTER_ALIGNMENT);

        manual.add(addPosting);
        manual.add(browsePosting);
        manual.add(searchApplicant);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        HomePanel.add(manual, c);

        JButton logout = new JButton("Logout");

        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(20, 100, 0, 0);
        HomePanel.add(logout, c);

        return HomePanel;
    }

    // Need to pass in list of job postings (all for browse all or particular for to-do)
    private JPanel HRBrowsePosting () {
        JPanel postingPanel = new JPanel();
        postingPanel.setLayout(new BoxLayout(postingPanel, BoxLayout.PAGE_AXIS));
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JList<String> jobPostings = new JList();
        JLabel status = new JLabel("Job posting status here. Changes according to JobPosting selected in JList.");
        JButton scheduleInterview = new JButton("Schedule");
        JButton hiring = new JButton("Hiring decision");
        JButton home = new JButton("Home");

        jobPostings.setAlignmentX(Component.CENTER_ALIGNMENT);
        status.setAlignmentX(Component.CENTER_ALIGNMENT);
        scheduleInterview.setAlignmentX(Component.CENTER_ALIGNMENT);
        hiring.setAlignmentX(Component.CENTER_ALIGNMENT);
        home.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttons.add(scheduleInterview);
        buttons.add(hiring);
        buttons.add(home);

        postingPanel.add(jobPostings);
        postingPanel.add(status);
        postingPanel.add(buttons);

        return postingPanel;
    }
}

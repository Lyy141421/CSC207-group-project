import javax.swing.*;
import java.awt.*;

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
    }

    // Call methods that create each interface and add to main frame.
    private void addCards () {
        add(HRInterface());
    }

    // Create interface for login screen
    private JPanel LoginInterface() {
        JPanel loginScreen = new JPanel(null);
        JLabel welcomeLabel = new JLabel("CSC207 Summer 2019 Job Application System", SwingConstants.CENTER);//TODO: Text
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        welcomeLabel.setBounds(320, 90, 640, 90);
        welcomeLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        JLabel userNameText = new JLabel("Username: ", SwingConstants.CENTER);
        userNameText.setBounds(540, 280, 100, 30);
        JTextField userNameEntry = new JTextField();
        userNameEntry.setBounds(640, 280, 100, 30);
        JLabel passwordText = new JLabel("Password: ", SwingConstants.CENTER);
        passwordText.setBounds(540, 310, 100, 30);
        JPasswordField passwordEntry = new JPasswordField();
        passwordEntry.setBounds(640, 310, 100, 30);
        JButton loginButton = new JButton("Login/Register");
        loginButton.setBounds(580, 360, 120, 20);

        loginScreen.add(welcomeLabel); loginScreen.add(userNameText); loginScreen.add(passwordText);
        loginScreen.add(userNameEntry); loginScreen.add(passwordEntry); loginScreen.add(loginButton);

        return loginScreen;
    }

    // Create interface for HR
    private JPanel HRInterface () {
        setTitle("HR Coordinator");
        JPanel HRPanel = new JPanel();
        HRPanel.setLayout(new CardLayout());
        HRPanel.add(HRHome());

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
}

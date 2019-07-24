package GUIClasses.StartInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * REMEMBER:
 * Along with NewGUI.LoginNewUser.LoginMain, NewUserPanel is one of the mandatory cards that should be present in NewGUI.MainFrame.
 */

public class NewUserPanel extends JPanel {
    private LoginBackend BackEnd;
    private CardLayout masterLayout;
    private Container parent;
    private String newUsername;

    public NewUserPanel(Container parent, CardLayout masterLayout) {
        this.parent = parent;
        this.masterLayout = masterLayout;
        this.setLayout(new GridLayout(3, 1));

        NewUserSelector selector = new NewUserSelector();
        JPanel forms = this.buildForms();

        selector.getSelector().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox actor = (JComboBox)e.getSource();
                CardLayout cl = (CardLayout)forms.getLayout();
                cl.show(forms, (String)(actor.getSelectedItem()));
            }
        });

        JDialog success = this.buildDialog();
        JPanel buttons = this.buildButtons(selector.getSelector(), success);

        this.add(selector); this.add(forms); this.add(buttons);
        this.BackEnd = new LoginBackend();
    }

    /**
     * Adds the different registration forms in card form to the master panel
     */
    private JPanel buildForms() {
        JPanel formPanel = new JPanel(new CardLayout()); formPanel.setName("formPanel");

        formPanel.add(new NewApplicantForm(), "Applicant");
        formPanel.add(new NewInterviewerForm(), "Interviewer");
        formPanel.add(new NewHRCForm(), "HR Coordinator");
        formPanel.add(new NewReferenceForm(), "Reference");

        return formPanel;
    }

    /**
     * Adds buttons to create account as well as return to login
     */
    private JPanel buildButtons(JComboBox selector, JDialog success) {
        JPanel buttonPanel = new JPanel(null); buttonPanel.setName("buttonPanel");

        JButton submitButton = new JButton("Create Account");
        submitButton.setBounds(352, 15, 150, 30);
        submitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int status = createUser(selector);
                postCreation(status, success);
            }
        });
        buttonPanel.add(submitButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(352, 80, 150, 30);
        backButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                clearEntries();
                masterLayout.show(parent, "LOGIN");
            }
        });
        buttonPanel.add(backButton);

        JLabel blankError = new JLabel("Error - Blank Entry");
        blankError.setBounds(522, 15, 200, 20);
        blankError.setVisible(false);
        buttonPanel.add(blankError);

        JLabel emailError = new JLabel("Error - Invalid Email");
        emailError.setBounds(522, 15, 200, 20);
        emailError.setVisible(false);
        buttonPanel.add(emailError);

        JLabel companyError = new JLabel("Error - Branch not Found");
        companyError.setBounds(522, 15, 200, 20);
        companyError.setVisible(false);
        buttonPanel.add(companyError);

        return buttonPanel;
    }

    /**
     * Attempts to create the selected user type with the given inputs
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    private int createUser(JComboBox selector) {
        switch(selector.getSelectedItem().toString()) {
            case "Applicant":
                return this.createApplicant();
            case "Interviewer":
                return this.createInterviewer();
            case "HR Coordinator":
                return this.createHRC();
        }
        return 0;
    }

    /**
     * The below functions simply attempt to instantiate their corresponding user types
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    private int createApplicant() {
        JPanel forms = this.getPanelByName(this, "formPanel");
        Component[] items = this.getPanelByName(forms, "applicantCard").getComponents();
        return BackEnd.createNewApplicant(this.BackEnd.getInputs(items, this.newUsername));
    }

    private int createInterviewer() {
        JPanel forms = this.getPanelByName(this, "formPanel");
        Component[] items = this.getPanelByName(forms, "interviewerCard").getComponents();
        return BackEnd.createNewInterviewer(this.BackEnd.getInputs(items, this.newUsername));
    }

    private int createHRC() {
        JPanel forms = this.getPanelByName(this, "formPanel");
        Component[] items = this.getPanelByName(forms, "HRCCard").getComponents();
        return BackEnd.createNewHRC(this.BackEnd.getInputs(items, this.newUsername));
    }

    /**
     * Finds a panel within another panel by its name as set by .setName()
     */
    private JPanel getPanelByName(JPanel panel, String name) {
        JPanel ret = new JPanel();
        for(Component c : panel.getComponents()) {
            if(c instanceof JPanel && c.getName().equals(name)) {
                ret = (JPanel)c;
            }
        }
        return ret;
    }

    /**
     * Updates the GUI's visuals based on the result of an account creation
     * @param status 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    private void postCreation(int status, JDialog success) {
        switch(status) {
            case 0:
                this.postUpdater("Error - Blank Entry");
                break;
            case 1:
                this.postUpdater("Error - Invalid Email");
                break;
            case 2:
                this.postUpdater("Error - Branch not Found");
                break;
            case 3:
                success.setVisible(true);
                this.clearEntries();
                this.masterLayout.show(this.parent, "LOGIN");
                break;
        }
    }

    /**
     *
     * @param text content of the JLabel which is to be shown
     */
    private void postUpdater(String text) {
        JPanel buttons = this.getPanelByName(this, "buttonPanel");
        for(Component c : buttons.getComponents()) {
            if(c instanceof JLabel) {
                if (((JLabel)c).getText().equals(text)) {
                    c.setVisible(true);
                } else {
                    c.setVisible(false);
                }
            }
        }
    }

    private void clearEntries() {
        JPanel forms = this.getPanelByName(this, "formPanel");
        for(Component c : forms.getComponents()) {
            for(Component c2 : ((JPanel)c).getComponents()) {
                if(c2 instanceof JTextField) {
                    ((JTextField) c2).setText("");
                } else if(c2 instanceof JPasswordField) { //Intellij error is incorrect - this works
                    ((JPasswordField) c2).setText("");
                }
            }
        }
        this.setNewUsername(null);
        this.postUpdater("");
    }

    private JDialog buildDialog() {
        JDialog d = new JDialog(new JFrame() , "Account Created", true);
        d.setLayout( new FlowLayout() );
        JButton b = new JButton("OK");
        b.addActionListener ( new ActionListener() {
            public void actionPerformed( ActionEvent e )
            {
                d.setVisible(false);
            }
        });
        d.add( new JLabel("Account creation successful"), SwingConstants.CENTER);
        d.add(b);
        d.setSize(300,80);
        d.setVisible(false);

        return d;
    }

    void setNewUsername(String username) {
        this.newUsername = username;
    }

    public static void main(String[] args) {
        JFrame show = new JFrame();
        show.setVisible(true);
        show.setSize(854, 480);
        show.add(new NewUserPanel(new Container(), new CardLayout()));
    }
}

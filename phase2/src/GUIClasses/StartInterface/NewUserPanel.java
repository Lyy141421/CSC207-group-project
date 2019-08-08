package GUIClasses.StartInterface;

import GUIClasses.MainFrame;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

/**
 * REMEMBER:
 * Along with NewGUI.LoginNewUser.LoginMain, NewUserPanel is one of the mandatory cards that should be present in NewGUI.MainFrame.
 */

public class NewUserPanel extends JPanel {

    private static String FORM_PANEL_NAME = "formPanel";
    static final String BLANK_ENTRY = "Error - Blank entry";
    static final String INVALID_EMAIL = "Error - Invalid email";
    static final String INVALID_COMPANY = "Error - Company not found";
    static final String INVALID_BRANCH = "Error - Branch not found";
    static final String INVALID_LEGAL_NAME = "Error - Invalid legal name";
    static final String INVALID_POSTAL_CODE = "Error - Invalid postal code";
    static final String SUCCESS = "Success";

    private LoginBackend backend;
    private CardLayout masterLayout;
    private Container parent;
    private String newUsername;

    public NewUserPanel(Container parent, CardLayout masterLayout, JobApplicationSystem jobAppSystem) {
        this.parent = parent;
        this.masterLayout = masterLayout;
        this.setLayout(new GridLayout(3, 1));

        NewUserSelector selector = new NewUserSelector();
        JPanel forms = this.buildForms();

        selector.getSelector().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox actor = (JComboBox) e.getSource();
                CardLayout cl = (CardLayout) forms.getLayout();
                cl.show(forms, (String) (actor.getSelectedItem()));
            }
        });

        JDialog success = this.buildDialog();
        JPanel buttons = this.buildButtons(selector.getSelector(), success);

        this.add(selector);
        this.add(forms);
        this.add(buttons);
        this.backend = new LoginBackend(jobAppSystem);
    }

    /**
     * Adds the different registration forms in card form to the master panel
     */
    private JPanel buildForms() {
        JPanel formPanel = new JPanel(new CardLayout());
        formPanel.setName(FORM_PANEL_NAME);

        formPanel.add(new NewApplicantForm(), "Applicant");
        formPanel.add(new NewInterviewerForm(), "Interviewer");
        formPanel.add(new NewHRCForm(), "HR Coordinator");

        return formPanel;
    }

    /**
     * Adds buttons to create account as well as return to login
     */
    private JPanel buildButtons(JComboBox selector, JDialog success) {
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setName("buttonPanel");

        JButton submitButton = new JButton("Create Account");
        submitButton.setBounds(352, 15, 150, 30);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("From submit button action listener");
                System.out.println(Thread.activeCount());
                String status = createUser(selector);
                postCreation(status, success);
            }
        });
        buttonPanel.add(submitButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(352, 80, 150, 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearEntries();
                masterLayout.show(parent, MainFrame.LOGIN);
            }
        });
        buttonPanel.add(backButton);

        String[] errorMessages = new String[]{BLANK_ENTRY, INVALID_EMAIL, INVALID_BRANCH, INVALID_COMPANY, INVALID_LEGAL_NAME,
                INVALID_POSTAL_CODE};

        for (String errorMessage : errorMessages) {
            JLabel errorLabel = new JLabel(errorMessage);
            errorLabel.setBounds(522, 15, 200, 20);
            errorLabel.setVisible(false);
            buttonPanel.add(errorLabel);
        }
        return buttonPanel;
    }

    /**
     * Attempts to create the selected user type with the given inputs
     *
     * @return constants based on the login status
     */
    private String createUser(JComboBox selector) {
        switch (selector.getSelectedItem().toString()) {
            case "Applicant":
                return this.createApplicant();
            case "Interviewer":
                return this.createInterviewer();
            case "HR Coordinator":
                return this.createHRC();
        }
        return BLANK_ENTRY;
    }

    /**
     * The below functions simply attempt to instantiate their corresponding user types
     *
     * @return contants based on the status of the login
     */
    private String createApplicant() {
        JPanel forms = this.getPanelByName(this, FORM_PANEL_NAME);
        Component[] items = this.getPanelByName(forms, NewApplicantForm.NAME).getComponents();
        return backend.createNewApplicant(this.backend.getInputs(items, this.newUsername));
    }

    private String createInterviewer() {
        JPanel forms = this.getPanelByName(this, FORM_PANEL_NAME);
        Component[] items = this.getPanelByName(forms, NewInterviewerForm.NAME).getComponents();
        return backend.createNewInterviewer(this.backend.getInputs(items, this.newUsername));
    }

    private String createHRC() {
        JPanel forms = this.getPanelByName(this, FORM_PANEL_NAME);
        Component[] items = this.getPanelByName(forms, NewHRCForm.NAME).getComponents();
        return backend.createNewHRC(this.backend.getInputs(items, this.newUsername));
    }

    /**
     * Finds a panel within another panel by its name as set by .setName()
     */
    private JPanel getPanelByName(JPanel panel, String name) {
        JPanel ret = new JPanel();
        for (Component c : panel.getComponents()) {
            if (c instanceof JPanel && c.getName() != null && c.getName().equals(name)) {
                ret = (JPanel) c;
            }
        }
        return ret;
    }

    /**
     * Updates the GUI's visuals based on the result of an account creation
     *
     * @param status the status of the login
     */
    private void postCreation(String status, JDialog success) {
        if (status.equals(SUCCESS)) {
            this.hideErrorMessage();
            success.setVisible(true);
            this.clearEntries();
            this.masterLayout.show(this.parent, MainFrame.LOGIN);
        } else {
            this.postUpdater(status);
        }
    }

    /**
     * @param text content of the JLabel which is to be shown
     */
    private void postUpdater(String text) {
        JPanel buttons = this.getPanelByName(this, "buttonPanel");
        for (Component c : buttons.getComponents()) {
            if (c instanceof JLabel) {
                if (((JLabel) c).getText().equals(text)) {
                    c.setVisible(true);
                } else {
                    c.setVisible(false);
                }
            }
        }
    }

    private void hideErrorMessage() {
        JPanel buttons = this.getPanelByName(this, "buttonPanel");
        for (Component c : buttons.getComponents()) {
            if (c instanceof JLabel) {
                c.setVisible(false);
            }
        }
    }

    private void clearEntries() {
        JPanel forms = this.getPanelByName(this, FORM_PANEL_NAME);
        for (Component c : forms.getComponents()) {
            for (Component c2 : ((JPanel) c).getComponents()) {
                if (c2 instanceof JTextField) {
                    ((JTextField) c2).setText("");
                } else if (c2 instanceof JPasswordField) { //Intellij error is incorrect - this works
                    ((JPasswordField) c2).setText("");
                }
            }
        }
        this.setNewUsername(null);
        this.postUpdater("");
    }

    private JDialog buildDialog() {
        JDialog d = new JDialog(new JFrame(), "Account Created", true);
        d.setLayout(new FlowLayout());
        JButton b = new JButton("OK");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                d.setVisible(false);
            }
        });
        d.add(new JLabel("Account creation successful"), SwingConstants.CENTER);
        d.add(b);
        d.setSize(300, 80);
        d.setVisible(false);

        return d;
    }

    void setNewUsername(String username) {
        this.newUsername = username;
    }
}

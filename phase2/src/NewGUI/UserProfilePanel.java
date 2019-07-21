package NewGUI;

import Main.User;

import javax.swing.*;
import java.awt.*;

public class UserProfilePanel extends JPanel {

    private User user;

    public UserProfilePanel(User user) {
        this.user = user;
        this.setLayout(new BorderLayout());
        JPanel title = new FrequentlyUsedMethods().createTitlePanel("User Information", 30);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        this.add(title, BorderLayout.PAGE_START);
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new GridBagLayout());
        userInfoPanel.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        this.addProfileCategoryLabels(c, userInfoPanel);
        this.addProfileCategoryValues(c, userInfoPanel);
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        this.add(userInfoPanel, BorderLayout.CENTER);
    }

    /**
     * Adds the job posting category labels to the main job posting panel.
     *
     * @param c         The grid bag constraints for layout purposes.
     * @param mainPanel The panel where these labels are going to end up.
     */
    private void addProfileCategoryLabels(GridBagConstraints c, JPanel mainPanel) {
        c.insets = new Insets(0, 0, 20, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(new JLabel("User type: "), c);
        c.gridy++;
        mainPanel.add(new JLabel("Email:"), c);
        c.gridy++;
        mainPanel.add(new JLabel("Account created:"), c);
    }

    /**
     * Adds the profile category values to the main profile panel.
     *
     * @param c         The grid bag constraints for layout purposes.
     * @param mainPanel The main panel where the profile information will be displayed.
     */
    private void addProfileCategoryValues(GridBagConstraints c, JPanel mainPanel) {
        String[] values = user.getDisplayedInformation();
        c.insets = new Insets(0, 30, 20, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(new JTextArea(values[0]), c);
        c.gridy++;
        JTextArea jobDescriptionContent = new JTextArea(values[1]);
        mainPanel.add(jobDescriptionContent, c);
        c.gridy++;
        mainPanel.add(new JTextArea(values[2]), c);
    }
}

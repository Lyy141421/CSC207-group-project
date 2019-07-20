package NewGUI;

import Main.User;

import javax.swing.*;
import java.awt.*;

class UserProfilePanel extends JPanel {

    UserProfilePanel(User user) {
        this.setLayout(new BorderLayout());
        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setBackground(Color.WHITE);
        String[] userString = user.toString().split("\n");
        this.add(new FrequentlyUsedMethods().createTitlePanel("User Information", 20), BorderLayout.PAGE_START);
        for (String info : userString) {
            JLabel infoLabel = new JLabel(info);
            userInfo.add(infoLabel);
        }
        this.add(userInfo, BorderLayout.CENTER);    // TODO center this!!!
    }
}

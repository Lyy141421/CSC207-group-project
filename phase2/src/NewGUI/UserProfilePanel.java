package NewGUI;

import Main.User;

import javax.swing.*;
import java.awt.*;

class UserProfilePanel extends JPanel {

    UserProfilePanel(User user) {
        this.setLayout(new BorderLayout());
        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        String[] userString = user.toString().split("\n");
        for (String info : userString) {
            JLabel infoLabel = new JLabel(info);
            userInfo.add(infoLabel);
            infoLabel.setHorizontalAlignment(JLabel.CENTER);
            infoLabel.revalidate();
        }
        this.add(userInfo, BorderLayout.CENTER); // TODO move to center
    }
}

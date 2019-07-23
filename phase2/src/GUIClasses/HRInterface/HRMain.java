package GUIClasses.HRInterface;

import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class HRMain extends JPanel {

    Container contentPane;
    MethodsTheGUICallsInHR HRInterface;
    LocalDate today;

    HRHome homePanel;
    HRViewPosting viewPostingPanel;
    HRViewApp viewAppPanel;
    HRSearchApplicant searchPanel;
    HRAddPosting addPostingPanel;

    private HRMain (Container contentPane, MethodsTheGUICallsInHR HRInterface, LocalDate today) {
        this.setLayout(new CardLayout());

        this.contentPane = contentPane;
        this.HRInterface = HRInterface;
        this.today = today;

        this.addPanels();
    }

    private void addPanels() {
        this.add(this.homePanel = new HRHome(this, this.HRInterface, this.today), HRPanel.HOME);
        this.add(this.viewPostingPanel = new HRViewPosting(this, this.HRInterface, this.today), HRPanel.POSTING);
        this.add(this.viewAppPanel = new HRViewApp(this, this.HRInterface, this.today), HRPanel.APPLICATION);
        this.add(this.searchPanel = new HRSearchApplicant(this, this.HRInterface, this.today), HRPanel.SEARCH);
        this.add(this.addPostingPanel = new HRAddPosting(this, this.HRInterface, this.today), HRPanel.ADD_POSTING);
    }

    private void setLogoutAction () {
        this.homePanel.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: save all changes.
                //TODO: Replace "LOGIN" with static constant.
                ((CardLayout) contentPane.getLayout()).show(contentPane, "LOGIN");
            }
        });
    }
    

}

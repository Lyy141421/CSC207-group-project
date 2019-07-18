package ActionListeners.HRActionListeners;

import CompanyStuff.HRCoordinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewJobPostingsActionListener implements ActionListener {

    private HRCoordinator HRC;
    private String category;

    public ViewJobPostingsActionListener(HRCoordinator HRC, String category) {
        this.HRC = HRC;
        this.category = category;
    }

    public void actionPerformed(ActionEvent e) {

    }
}

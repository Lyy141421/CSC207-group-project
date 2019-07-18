package ActionListeners.HRActionListeners;

import CompanyStuff.HRCoordinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewJobPostingsActionListener implements ActionListener {

    private HRCoordinator hrCoordinator;
    private String category;

    public ViewJobPostingsActionListener(HRCoordinator hrCoordinator, String category) {
        this.hrCoordinator = hrCoordinator;
        this.category = category;
    }

    public void actionPerformed(ActionEvent e) {

    }
}

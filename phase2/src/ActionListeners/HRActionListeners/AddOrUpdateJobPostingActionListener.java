package ActionListeners.HRActionListeners;

import CompanyStuff.HRCoordinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddOrUpdateJobPostingActionListener extends HRActionListener {

    private String action;

    public AddOrUpdateJobPostingActionListener(HRCoordinator HRC, String action) {
        super(HRC);
        this.action = action;
    }

    public void actionPerformed(ActionEvent e) {

    }
}

package ActionListeners.HRActionListeners;

import CompanyStuff.HRCoordinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class HRActionListener implements ActionListener {

    private HRCoordinator HRC;

    HRActionListener(HRCoordinator HRC) {
        this.HRC = HRC;
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);
}

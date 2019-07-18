package ActionListeners.ApplicantActionListeners;

import ApplicantStuff.Applicant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ApplicantActionListener implements ActionListener {

    private Applicant applicant;

    ApplicantActionListener(Applicant applicant) {
        this.applicant = applicant;
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);
}

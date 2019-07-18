package ActionListeners.InterviewerActionListeners;

import CompanyStuff.Interviewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class InterviewerActionListener implements ActionListener {

    private Interviewer interviewer;

    InterviewerActionListener(Interviewer interviewer) {
        this.interviewer = interviewer;
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);
}

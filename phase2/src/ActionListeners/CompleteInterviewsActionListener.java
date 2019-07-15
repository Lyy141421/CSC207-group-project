package ActionListeners;

import CompanyStuff.Interviewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompleteInterviewsActionListener implements ActionListener {

    private Interviewer interviewer;

    public CompleteInterviewsActionListener(Interviewer interviewer) {
        this.interviewer = interviewer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

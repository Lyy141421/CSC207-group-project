package ActionListeners;

import FileLoadingAndStoring.DataLoaderAndStorer;
import Main.JobApplicationSystem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoutActionListener implements ActionListener {

    private JobApplicationSystem jobApplicationSystem;
    private Container contentPane;

    public LogoutActionListener(JobApplicationSystem jobApplicationSystem, Container contentPane) {
        this.jobApplicationSystem = jobApplicationSystem;
        this.contentPane = contentPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new DataLoaderAndStorer(jobApplicationSystem).storeAllData();
        ((CardLayout) contentPane.getLayout()).show(contentPane, "LOGIN");
    }
}

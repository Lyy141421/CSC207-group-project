package ActionListeners;

import FileLoadingAndStoring.DataLoaderAndStorer;
import Main.JobApplicationSystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoutActionListener implements ActionListener {

    private JobApplicationSystem jobApplicationSystem;

    public LogoutActionListener(JobApplicationSystem jobApplicationSystem) {
        this.jobApplicationSystem = jobApplicationSystem;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new DataLoaderAndStorer(jobApplicationSystem).storeAllData();
        System.exit(0); // TODO change it to going back to log-in page
    }
}

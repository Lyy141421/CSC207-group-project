package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class SubmitDocumentListener implements ActionListener {

    private ArrayList<File> filesToSubmit;

    public SubmitDocumentListener(ArrayList<File> files) {
        this.filesToSubmit = files;
    }

    public void actionPerformed(ActionEvent e) {
        if (this.filesToSubmit.size() >= 1) {
            // Create job application document object with file
            // Add file to appropriate folder
        }
    }
}

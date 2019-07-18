package ActionListeners.UserActionListeners;

import Main.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class SubmitDocumentActionListener extends UserActionListener {

    private ArrayList<File> filesToSubmit;

    public SubmitDocumentActionListener(User user, ArrayList<File> files) {
        super(user);
        this.filesToSubmit = files;
    }

    public void actionPerformed(ActionEvent e) {
        if (this.filesToSubmit.size() >= 1) {
            // Create job application document object with file -- if applicant
            // Add file to appropriate folder
        }
    }
}

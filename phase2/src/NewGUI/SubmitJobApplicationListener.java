package NewGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class SubmitJobApplicationListener implements ActionListener {

    private ArrayList<File> filesToSubmit;

    SubmitJobApplicationListener(ArrayList<File> files) {
        this.filesToSubmit = files;
    }

    public void actionPerformed(ActionEvent e) {
        if (this.filesToSubmit.size() >= 1) {
            //notify whoever is submitting it
        }
    }
}

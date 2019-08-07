package GUIClasses.CommonUserGUI;

import FileLoadingAndStoring.DataLoaderAndStorer;
import Main.JobApplicationSystem;

import javax.swing.*;

abstract public class UserMain extends JPanel {
    /**
     * A user panel.
     */

    // === Class variables ===
    // Keys for common cards
    public static final String HOME = "HOME";
    public static final String PROFILE = "PROFILE";

    private JobApplicationSystem jobAppSystem;

    public UserMain(JobApplicationSystem jobAppSystem) {
        this.jobAppSystem = jobAppSystem;
    }

    /**
     * Set the cards.
     */
    abstract public void setCards();

    /**
     * Refresh the page.
     */
    public void refresh() {
        DataLoaderAndStorer dataLoaderAndStorer = new DataLoaderAndStorer(jobAppSystem);
        dataLoaderAndStorer.refreshAllData();
    }

}
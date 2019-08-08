package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import CompanyStuff.Interview;
import CompanyStuff.InterviewManager;
import GUIClasses.CommonUserGUI.GUIElementsCreator;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Displays upcoming interviews of an applicant
 */
class ApplicantSchedule extends JPanel {

    ApplicantSchedule(ApplicantBackend applicantBackend) {
        this.setLayout(new BorderLayout());

        JPanel titleText = new GUIElementsCreator().createLabelPanel("Schedule for " + applicantBackend.getApplicant().getLegalName(), 20, true);
        this.add(titleText, BorderLayout.NORTH);

        ArrayList<Interview> interviews = applicantBackend.getUpcomingInterviews();
        this.buildTable(interviews);
    }

    /**
     * Builds the table containing an applicant's upcoming interviews
     */
    private void buildTable(ArrayList<Interview> interviews) {
        Object[][] data = this.getInterviewData(interviews);
        String[] columnNames = {"Position", "Company", "Time", "Current Round"};

        JPanel table = new GUIElementsCreator().createTablePanel(columnNames, data);
        this.add(table, BorderLayout.CENTER);
    }

    /**
     * Helper function for buildTable
     * Converts interviews into data passable to a JTable
     */
    private Object[][] getInterviewData(ArrayList<Interview> interviews) {
        ArrayList<Object[]> data = new ArrayList<>();
        for(Interview I: interviews) {
            InterviewManager manager = I.getInterviewManager();
            Object[] add = {manager.getBranchJobPosting().getTitle(), manager.getBranchJobPosting().getCompany().getName(),
                    I.getTime(), I.getRoundNumber()};
            data.add(add);
        }
        return this.convertArrayList(data);
    }

    /**
     * Helper function for getInterviewData
     * Converts the arraylist of arrays into a 2D array
     */
    private Object[][] convertArrayList(ArrayList<Object[]> list) {
        Object[][] array = new Object[list.size()][];
        for(int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}

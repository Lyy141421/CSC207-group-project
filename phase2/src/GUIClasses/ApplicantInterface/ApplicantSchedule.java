package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import CompanyStuff.Interview;
import CompanyStuff.InterviewManager;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Displays upcoming interviews of an applicant
 */
class ApplicantSchedule extends JPanel {

    ApplicantSchedule(Applicant applicant, JobApplicationSystem jobAppSystem) {
        this.setLayout(null);
        
        JLabel titleText = new JLabel("Schedule for " + applicant.getLegalName(), SwingConstants.CENTER);
        titleText.setBounds(170, 35, 300, 40);
        titleText.setFont(new Font("Serif", Font.PLAIN, 27));
        this.add(titleText);

        ArrayList<Interview> interviews = applicant.getJobApplicationManager().
                getUpcomingInterviews(jobAppSystem.getToday());
        this.buildTable(interviews);
    }

    /**
     * Builds the table containing an applicant's upcoming interviews
     */
    private void buildTable(ArrayList<Interview> interviews) {
        Object[][] data = this.getInterviewData(interviews);
        String[] columnNames = {"Position", "Company", "Time", "Current Round"};

        JTable table = new JTable(data, columnNames);
        table.setBounds(170, 150, 300, data.length * 25);
        this.add(table);
    }

    /**
     * Helper function for buildTable
     * Converts interviews into data passable to a JTable
     */
    private Object[][] getInterviewData(ArrayList<Interview> interviews) {
        ArrayList<Object[]> data = new ArrayList<>();
        for(Interview I: interviews) {
            InterviewManager manager = I.getInterviewManager();
            Object[] add = {manager.getBranchJobPosting().getTitle(), manager.getBranchJobPosting().getCompany(),
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

package NewGUI;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import CompanyStuff.Branch;
import JobPostings.BranchJobPosting;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ReferenceGUI {

    // === Instance variables ===
    private JPanel fullPanel;
    private Reference reference;
    private JPanel cards = new JPanel(new CardLayout());
    private final String HOME = "Home";

    ReferenceGUI(Reference reference) {
        fullPanel = new JPanel(new BorderLayout());
        fullPanel.add(new ReferenceSideBarMenuPanel(reference).createPanel(), BorderLayout.WEST);
        this.reference = reference;
        cards.add(new ReferenceHomePanel(this.reference).createPanel(), HOME);
        fullPanel.add(cards, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Reference Home Page");
        Reference reference = new Reference("bob@gmail.com", LocalDate.now());
        Branch branch = new Branch("Branch", "L4B3Z9");
        Applicant applicant = new Applicant("username", "password", "Legal Name", "email@gmail.com", LocalDate.now(), "L4B4P8");
        BranchJobPosting jobPosting = new BranchJobPosting("title", "field", "description",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.now(), LocalDate.now());
        JobApplication jobApp = new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.now());
        Applicant applicant2 = new Applicant("username2", "password", "Legal Name2", "email@gmail.com", LocalDate.now(), "L4B4P8");
        BranchJobPosting jobPosting2 = new BranchJobPosting("title", "field", "description",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.now(), LocalDate.now());
        JobApplication jobApp2 = new JobApplication(applicant2, jobPosting2, new ArrayList<>(), LocalDate.now());
        reference.addJobApplicationForReference(jobApp);
        reference.addJobApplicationForReference(jobApp2);
        frame.add(new ReferenceGUI(reference).fullPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.CommonUserGUI.FileChooser;
import Main.JobApplicationSystem;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ApplicantFileSubmissionFromLocal extends JPanel {

    ApplicantFileSubmissionFromLocal(ApplicantBackend applicantBackend, BranchJobPosting jobPosting) {
        JobApplication jobApp = applicantBackend.createJobApplication(jobPosting);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(this.createTitle());
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        FileChooser fileChooser = new FileChooser(applicantBackend.getApplicant(), jobApp);
        fileChooser.enableUploadButton();
        fileChooser.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        this.add(fileChooser);
    }

    private JLabel createTitle() {
        JLabel titleText = new JLabel("Document Submission");
        titleText.setFont(new Font("Serif", Font.PLAIN, 22));
        titleText.setAlignmentX(Component.CENTER_ALIGNMENT);
        return titleText;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Reference Home Page");
        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
        jobApplicationSystem.setToday(LocalDate.of(2019, 7, 20));
        jobApplicationSystem.setPreviousLoginDate(LocalDate.of(2019, 7, 19));
        Company company = jobApplicationSystem.createCompany("Company");
        Branch branch = company.createBranch("Branch", "Toronto");
        Applicant applicant = jobApplicationSystem.getUserManager().createApplicant("username", "password", "Legal Name", "email@gmail.com", "L4B4P8", LocalDate.now());
        HRCoordinator hrc = new HRCoordinator("HR", "password", "HRC", "email@gmail.com", branch, LocalDate.of(2019, 7, 19));
        BranchJobPosting jobPosting = hrc.addJobPosting("title", "field", "descriptionhujedkfnvsgrhjegskamkagjrwuiladkvmkajgirwouskvmzkjgiskdzvn,mkngs\niznvjgsirklzngjslitw4gsijlkznjsirtwtrsigjlzknvmJDEI0   IPUOwrahektdznmv\nlpox-98uy7gufhvnb tmwkeafoisCXU*yygchbvn    4mk2RWFsvzx\nwgudkngrhadkjn\nwhaegkjsc\ngwaeihfkncMZ<ghaecsknm,z\n",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 19), LocalDate.now());
        frame.add(new ApplicantFileSubmissionFromLocal(new ApplicantBackend(applicant, jobApplicationSystem), jobPosting));
        frame.setSize(854, 480);
        frame.setVisible(true);
    }
}

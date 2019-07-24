package GUIClasses.InterviewerInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import DocumentManagers.CompanyDocumentManager;
import GUIClasses.CommonUserGUI.DocumentViewer;
import GUIClasses.MethodsTheGUICallsInInterviewer;
import Main.JobApplicationSystem;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InterviewerViewCopy extends InterviewerPanel {

    String OVERVIEW = "Overview";
    String FILE = "View Files";

    JTabbedPane infoPane;
    JTextArea overview;
    JPanel documentViewer;
    JobApplication jobAppSelected;
    JList applicantList;

    InterviewerViewCopy(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today) {
        super(contentPane, interviewerInterface, today);
        this.interviews = getInterviewMap();
        this.setLayout(new BorderLayout());

        JSplitPane splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(250);
        this.setInterviewList(splitDisplay);
        this.setInfoPane(splitDisplay);

        this.setListSelectionListener();
    }

    void reload() {
        //TODO: reload display panels;
        this.interviewList.setListData(interviews.keySet().toArray(new String[interviews.size()]));
    }

    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getScheduledUpcomingInterviews(today));
    }

    void setInterviewList(JSplitPane splitPane) {
        this.interviewList = new JList<>();
        this.interviewList.setListData(interviews.keySet().toArray(new String[interviews.size()]));
        this.interviewList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.interviewList.setLayoutOrientation(JList.VERTICAL);

        splitPane.setLeftComponent(new JScrollPane(this.interviewList));
    }

    void setInfoPane (JSplitPane splitDisplay) {
        this.infoPane = new JTabbedPane();
        this.infoPane.addTab(this.OVERVIEW, makeOverviewTab("Select an interview to view overview."));
        this.documentViewer = new JPanel();
        this.infoPane.addTab(this.FILE, this.documentViewer);

        splitDisplay.setRightComponent(this.infoPane);
    }

    JPanel createDocumentViewer() {
        CompanyDocumentManager cdm = new CompanyDocumentManager(this.interviewerInterface.getInterviewer().getBranch().getCompany());
        return new DocumentViewer(cdm.getFolderForJobApplication(jobAppSelected));
    }

    JPanel createApplicantListPanel(Interview interview) {
        JPanel applicantListPanel = new JPanel();
        DefaultListModel listModel = new DefaultListModel();
        for (JobApplication jobApp : interview.getJobApplications()) {
            listModel.addElement(jobApp.getApplicant().getLegalName());
        }
        applicantList = new JList(listModel);
        applicantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        applicantList.setSelectedIndex(-1);
        applicantList.setLayoutOrientation(JList.VERTICAL);
        applicantList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (applicantList.getSelectedIndex() != -1) {
                        jobAppSelected = interview.getJobApplications().get(applicantList.getSelectedIndex());
                    }
                }
            }
        });
        applicantListPanel.add(applicantList);
        return applicantListPanel;
    }

    JComponent makeOverviewTab (String text) {
        this.overview = new JTextArea(text);
        this.overview.setEditable(false);
        this.overview.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        return new JScrollPane(this.overview);
    }

    void setListSelectionListener() {
        this.interviewList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedTitle = interviewList.getSelectedValue();
                Interview selectedInterview = interviews.get(selectedTitle);
                overview.setText(selectedInterview.toString());
                documentViewer.removeAll();
                documentViewer.add(createApplicantListPanel(selectedInterview));
                documentViewer.add(createDocumentViewer());
            }
        });
    }

    public static void main(String[] args) {
        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
        Applicant applicant = new Applicant("jsmith", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "L4B3Z9");
        Company company = new Company("Company");
        Branch branch = new Branch("Branch", "L4B3Z9", company);
        BranchJobPosting jobPosting = new BranchJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10));
        new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 21));
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        Interviewer interviewer = new Interviewer("Interviewer", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getBranch().addInterviewer(interviewer);
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "Phone interview"});
        jobPosting.getInterviewManager().setInterviewConfiguration(interviewConfiguration);
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();

        JFrame frame = new JFrame();
        frame.add(new InterviewerViewCopy(new Container(), new MethodsTheGUICallsInInterviewer(interviewer), LocalDate.now()));
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

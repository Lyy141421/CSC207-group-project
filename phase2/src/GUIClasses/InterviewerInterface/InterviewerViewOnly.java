package GUIClasses.InterviewerInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import GUIClasses.CommonUserGUI.DocumentViewer;
import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.HashMap;

public class InterviewerViewOnly extends InterviewerPanel implements ListSelectionListener {

    private static String OVERVIEW = "Overview";
    private static String FILE = "View Files";
    private static String VIEW_NOTES = "View Notes";

    JTabbedPane infoPane;
    private JTextArea overview;
    private JPanel viewNotesPanel = new JPanel();
    private JPanel documentViewerPanel = new JPanel(new BorderLayout());
    private JPanel documentViewer;
    private JobApplication jobAppSelected;
    private JList applicantList = new JList();
    Interview interviewSelected;

    InterviewerViewOnly(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
        this.setInterviews(this.getInterviewMap());
        this.setLayout(new BorderLayout());

        splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(200);
        this.setInterviewList();
        this.setInfoPane();
        this.setInterviewListSelectionListener();
        this.add(splitDisplay);
    }

    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getAllIncompleteInterviews());
    }

    private void setInfoPane() {
        infoPane = new JTabbedPane();
        infoPane.addTab(OVERVIEW, makeOverviewTab());
        infoPane.addTab(FILE, documentViewerPanel);
        infoPane.addTab(VIEW_NOTES, makeNotesTab());

        splitDisplay.setRightComponent(this.infoPane);
    }

    private JPanel createApplicantListPanel(Interview interview) {
        DefaultListModel listModel = new DefaultListModel();
        for (JobApplication jobApp : interview.getJobApplications()) {
            listModel.addElement(jobApp.getApplicant().getLegalName());
        }
        applicantList = new JList(listModel);
        applicantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        applicantList.setSelectedIndex(-1);
        applicantList.setLayoutOrientation(JList.VERTICAL);
        this.setApplicantListSelectionListener(interview);
        JPanel applicantListPanel = new JPanel();
        applicantListPanel.add(applicantList);
        return applicantListPanel;
    }

    private void setApplicantListSelectionListener(Interview interview) {
        applicantList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (applicantList.getSelectedIndex() != -1) {
                        jobAppSelected = interview.getJobApplications().get(applicantList.getSelectedIndex());
                        documentViewer = new DocumentViewer(interviewerInterface.getFolderForJobApplication(jobAppSelected));
                        documentViewerPanel.add(documentViewer, BorderLayout.CENTER);
                        documentViewerPanel.revalidate();
                    }
                }
            }
        });
    }

    private JComponent makeOverviewTab() {
        this.overview = new JTextArea("Select an interview to view overview.");
        this.overview.setEditable(false);
        this.overview.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        return new JScrollPane(this.overview);
    }

    private JComponent makeNotesTab() {
        this.viewNotesPanel.setLayout(new BoxLayout(viewNotesPanel, BoxLayout.Y_AXIS));
        return new JScrollPane(this.viewNotesPanel);
    }

    private void setViewNotesPanel(Interview interview) {
        HashMap<Interviewer, String> interviewerToNotes = this.interviewerInterface.getInterviewerToNotes(interview);
        this.viewNotesPanel.removeAll();
        for (Interviewer interviewer : interviewerToNotes.keySet()) {
            if (interviewerToNotes.get(interviewer) != null) {
                JLabel interviewerName = new JLabel(interviewer.getLegalName());
                interviewerName.setHorizontalAlignment(SwingConstants.LEFT);
                JTextArea notes = new JTextArea(interviewerToNotes.get(interviewer));
                notes.setEditable(false);
                this.viewNotesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                this.viewNotesPanel.add(interviewerName);
                this.viewNotesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                this.viewNotesPanel.add(notes);
                this.viewNotesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
    }

    private void setInterviewListSelectionListener() {
        interviewList.addListSelectionListener(this);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (interviewList.getSelectedIndex() != -1) {
            String selectedTitle = interviewList.getSelectedValue();
            interviewSelected = interviews.get(selectedTitle);
            loadTabContents(interviewSelected);
        }
    }

    private JLabel createApplicantListPrompt() {
        return new JLabel("Select an applicant:");
    }

    void loadTabContents(Interview selectedInterview) {
        overview.setText(selectedInterview.toString());
        documentViewerPanel.removeAll();
        documentViewerPanel.add(createApplicantListPrompt(), BorderLayout.BEFORE_FIRST_LINE);
        documentViewerPanel.add(createApplicantListPanel(selectedInterview), BorderLayout.WEST);
        documentViewerPanel.revalidate();
        setViewNotesPanel(selectedInterview);
    }

}

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
import java.time.LocalDate;
import java.util.HashMap;

public class InterviewerView extends InterviewerPanel {

    String OVERVIEW = "Overview";
    String FILE = "View Files";
    String VIEW_NOTES = "View Notes";

    JTabbedPane infoPane;
    JSplitPane splitDisplay;
    JTextArea overview;
    JPanel notesPanel;
    JPanel documentViewer;
    JobApplication jobAppSelected;
    JList applicantList;

    InterviewerView(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
        this.interviews = getInterviewMap();
        this.setLayout(new BorderLayout());

        splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(250);
        this.setInterviewList(splitDisplay);
        this.setInfoPane(splitDisplay);

        this.setListSelectionListener();
        this.add(splitDisplay);
    }

    void reload() {
        //TODO: reload display panels;
        this.interviewList.setListData(interviews.keySet().toArray(new String[interviews.size()]));
    }

    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getAllIncompleteInterviews());
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
        this.documentViewer.setLayout(new BorderLayout());
        this.infoPane.addTab(this.FILE, this.documentViewer);
        this.infoPane.addTab(this.VIEW_NOTES, makeNotesTab());

        splitDisplay.setRightComponent(this.infoPane);
    }

    JPanel createDocumentViewer() {
        return new DocumentViewer(this.interviewerInterface.getFolderForJobApplication(jobAppSelected));
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
                        documentViewer.add(createDocumentViewer(), BorderLayout.CENTER);
                        documentViewer.revalidate();
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

    JComponent makeNotesTab() {
        this.notesPanel = new JPanel();
        this.notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));
        return new JScrollPane(this.notesPanel);
    }

    void setNotesPanel(Interview interview) {
        // TODO i don't know why the interviewer pops up twice but there is only one interviewer
        HashMap<Interviewer, String> interviewerToNotes = this.interviewerInterface.getInterviewerToNotes(interview);
        for (Interviewer interviewer : interviewerToNotes.keySet()) {
            JLabel interviewerName = new JLabel(interviewer.getLegalName());
            interviewerName.setHorizontalAlignment(SwingConstants.LEFT);
            JTextArea notes = new JTextArea(interviewerToNotes.get(interviewer));
            notes.setEditable(false);
            this.notesPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            this.notesPanel.add(interviewerName);
            this.notesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            this.notesPanel.add(notes);
        }
    }

    void setListSelectionListener() {
        this.interviewList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedTitle = interviewList.getSelectedValue();
                Interview selectedInterview = interviews.get(selectedTitle);
                overview.setText(selectedInterview.toString());
                documentViewer.removeAll();
                documentViewer.add(createApplicantListPanel(selectedInterview), BorderLayout.WEST);
                setNotesPanel(selectedInterview);
            }
        });
    }
}

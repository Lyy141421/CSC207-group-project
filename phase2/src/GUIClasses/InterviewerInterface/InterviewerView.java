package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import DocumentManagers.CompanyDocumentManager;
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

    private String OVERVIEW = "Overview";
    private String FILE = "View Files";

    private HashMap<String, Interview> interviews;

    private JList<String> interviewList;
    private JTabbedPane infoPane;
    private JTextArea overview;
    private JPanel documentViewer;

    InterviewerView(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today) {
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
        return getTitleToInterviewMap(interviewerInterface.getScheduledUpcomingInterviews());
    }

    private void setInterviewList(JSplitPane splitPane) {
        this.interviewList = new JList<>();
        this.interviewList.setListData(interviews.keySet().toArray(new String[interviews.size()]));
        this.interviewList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.interviewList.setLayoutOrientation(JList.VERTICAL);

        splitPane.setLeftComponent(new JScrollPane(this.interviewList));
    }

    private void setInfoPane (JSplitPane splitDisplay) {
        this.infoPane = new JTabbedPane();
        this.infoPane.addTab(this.OVERVIEW, makeOverviewTab("Select an interview to view overview."));
        this.documentViewer = new JPanel();
        this.infoPane.addTab(this.FILE, this.documentViewer);

        splitDisplay.setRightComponent(this.infoPane);
    }

    private JPanel createDocumentViewer(Interview interview) {
        CompanyDocumentManager CDM = new CompanyDocumentManager(this.interviewerInterface.getInterviewer().getBranch().getCompany());
        //TODO: need to adapt to multiple applicant in interview
        DocumentViewer DV = new DocumentViewer(CDM.getFolderForJobApplication(selectedInterview));

        return DV;
    }

    private JComponent makeOverviewTab (String text) {
        this.overview = new JTextArea(text);
        this.overview.setEditable(false);
        this.overview.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        return new JScrollPane(this.overview);
    }

    private void setListSelectionListener() {
        this.interviewList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedTitle = interviewList.getSelectedValue();
                Interview selectedInterview = interviews.get(selectedTitle);
                overview.setText(selectedInterview.toString());
                documentViewer.removeAll();
                //TODO: adapt this to multiple applications under one interview
                documentViewer.add(createDocumentViewer(selectedInterview.getJobApplications()));
            }
        });
    }
}

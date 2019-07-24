package GUIClasses.InterviewerInterface;

import ApplicantStuff.JobApplication;
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

    String OVERVIEW = "Overview";
    String FILE = "View Files";

    JTabbedPane infoPane;
    JTextArea overview;
    JPanel documentViewer;
    JobApplication jobAppSelected;
    JList applicantList;

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
}

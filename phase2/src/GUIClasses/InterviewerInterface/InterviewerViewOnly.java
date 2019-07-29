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

public class InterviewerViewOnly extends AbstractInterviewerPanel implements ListSelectionListener {
    /**
     * Panel for only viewing interview information.
     */

    // === Static variables ===
    // Keys for the tabs
    private static String OVERVIEW = "Overview";
    private static String FILE = "View Files";
    private static String VIEW_NOTES = "View Notes";

    // === Instance variables ===
    JTabbedPane infoPane;   // The tabbed pane
    private JTextArea overview; // The area where the overview for an interview is displayed.
    private JPanel viewNotesPanel = new JPanel();   // The panel for viewing notes
    private JPanel documentViewerPanel = new JPanel(new BorderLayout());    // The panel for viewing documents submitted
    private JPanel documentViewer;  // The panel with the document viewer object
    private JobApplication jobAppSelected;  // The job application selected
    private JList applicantList = new JList();  // The application list for the interview that is selected
    Interview interviewSelected;    // The interview selected

    // === Constructor ===
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

    /**
     * Get the interview map for the interviews that are to be displayed.
     *
     * @return the map of interviews to be displayed.
     */
    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getAllIncompleteInterviews());
    }

    /**
     * Set the tabbed pane.
     */
    private void setInfoPane() {
        infoPane = new JTabbedPane();
        infoPane.addTab(OVERVIEW, makeOverviewTab());
        infoPane.addTab(FILE, documentViewerPanel);
        infoPane.addTab(VIEW_NOTES, makeNotesTab());

        splitDisplay.setRightComponent(this.infoPane);
    }

    /**
     * Create the panel with the applicant list for viewing documents.
     * @param interview The interview selected.
     * @return the panel with the applicant list
     */
    private JPanel createApplicantListPanel(Interview interview) {
        DefaultListModel listModel = new DefaultListModel();
        for (JobApplication jobApp : interview.getJobApplications()) {
            listModel.addElement(jobApp.getApplicant().getLegalName());
        }
        applicantList = new JList(listModel);
        applicantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        applicantList.setSelectedIndex(-1);
        applicantList.setLayoutOrientation(JList.VERTICAL);
        this.setApplicantListSelectionListener();
        JPanel applicantListPanel = new JPanel();
        applicantListPanel.add(applicantList);
        return applicantListPanel;
    }

    /**
     * Set the list selection listener for the applicant list.
     */
    private void setApplicantListSelectionListener() {
        applicantList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (applicantList.getSelectedIndex() != -1) {
                        jobAppSelected = interviewSelected.getJobApplications().get(applicantList.getSelectedIndex());
                        documentViewer = new DocumentViewer(interviewerInterface.getFolderForJobApplication(jobAppSelected));
                        documentViewerPanel.add(documentViewer, BorderLayout.CENTER);
                        documentViewerPanel.revalidate();
                    }
                }
            }
        });
    }

    /**
     * Create the overview tab.
     * @return the tab created.
     */
    private JComponent makeOverviewTab() {
        this.overview = new JTextArea("Select an interview to view overview.");
        this.overview.setEditable(false);
        this.overview.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        return new JScrollPane(this.overview);
    }

    /**
     * Create the notes tab.
     * @return the tab created.
     */
    private JComponent makeNotesTab() {
        this.viewNotesPanel.setLayout(new BoxLayout(viewNotesPanel, BoxLayout.Y_AXIS));
        return new JScrollPane(this.viewNotesPanel);
    }

    /**
     * Set the contents of the view notes panel
     */
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

    /**
     * Set the interview list selection listener.
     */
    private void setInterviewListSelectionListener() {
        interviewList.addListSelectionListener(this);
    }

    /**
     * Set the action for when an interview is selected from the list.
     * @param e The list selection event.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (interviewList.getSelectedIndex() != -1) {
            String selectedTitle = interviewList.getSelectedValue();
            interviewSelected = interviews.get(selectedTitle);
            loadTabContents();
        }
    }

    /**
     * Create the applicant list prompt.
     * @return the label created.
     */
    private JLabel createApplicantListPrompt() {
        return new JLabel("Select an applicant:");
    }

    /**
     * Load the contents of the tabs.
     */
    void loadTabContents() {
        overview.setText(interviewSelected.toString());
        documentViewerPanel.removeAll();
        documentViewerPanel.add(createApplicantListPrompt(), BorderLayout.BEFORE_FIRST_LINE);
        documentViewerPanel.add(createApplicantListPanel(interviewSelected), BorderLayout.WEST);
        documentViewerPanel.revalidate();
        setViewNotesPanel(interviewSelected);
    }

}

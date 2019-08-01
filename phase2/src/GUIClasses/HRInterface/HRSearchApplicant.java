package GUIClasses.HRInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import GUIClasses.CommonUserGUI.GUIElementsCreator;
import org.junit.jupiter.api.condition.JRE;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

class HRSearchApplicant extends HRPanel {

    private JPanel parent;
    private JTextField nameInput;
    private JPanel searchPanel;
    private HashMap<String, Applicant> applicantHashMap;
    private JList<String> applicantList;
    private Applicant applicantSelected;

    HRSearchApplicant(HRBackend hrBackend, JPanel parent) {
        super(hrBackend);
        this.parent = parent;
        this.applicantHashMap = this.hrBackend.getApplicantHashMap();

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel mainTitle = new GUIElementsCreator().createLabelPanel("Search for an applicant", 20, true);
        mainTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel manualSearchTitle = new GUIElementsCreator().createLabelPanel("Manual search", 17, true);
        manualSearchTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        JPanel listSearchTitle = new GUIElementsCreator().createLabelPanel("Search from list", 17, true);
        listSearchTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        this.setSearchPanel();

        JPanel[] panels = new JPanel[]{mainTitle, manualSearchTitle, searchPanel, listSearchTitle,
                this.createApplicantListPanel()};
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        for (JPanel panel : panels) {
            this.add(panel, c);
            c.gridy++;
        }
    }

    private void setSearchPanel() {
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel applicantName = new JLabel("Applicant username");
        this.nameInput = new JTextField(30);
        this.nameInput.setMinimumSize(new Dimension(100, 30));

        searchPanel.add(applicantName);
        searchPanel.add(this.nameInput);
        searchPanel.add(createSearchButton());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private JButton createSearchButton () {
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<JobApplication> apps = hrBackend.getAllJobApplicationsToCompany(nameInput.getText());
                if (apps.isEmpty()) {
                    JOptionPane.showMessageDialog(searchPanel, "The applicant cannot be found.");
                } else {
                    showApplicationPanel(apps);
                }
            }
        });

        return searchButton;
    }

    private void showApplicationPanel(ArrayList<JobApplication> apps) {
        HRViewApp appPanel = new HRViewApp(parent, hrBackend, getTitleToAppMap(apps), HRPanel.SEARCH_APPLICANT, 0);
        if (parent.getComponents().length > 6) {
            parent.remove(6);
        }
        parent.add(appPanel, APPLICATION);
        ((CardLayout) parent.getLayout()).show(parent, APPLICATION);
    }

    private JPanel createApplicantListPanel() {
        JPanel listPanel = new JPanel();
        this.applicantList = new JList<>();
        this.applicantList.setListData(applicantHashMap.keySet().toArray(new String[applicantHashMap.size()]));
        this.applicantList.setSelectedIndex(-1);
        this.applicantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.applicantList.setLayoutOrientation(JList.VERTICAL);
        this.setApplicantListSelectionListener();
        listPanel.add(this.applicantList);
        return listPanel;
    }

    private void setApplicantListSelectionListener() {
        this.applicantList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!applicantList.getValueIsAdjusting() && applicantList.getSelectedIndex() != -1) {
                    String selectedTitle = applicantList.getSelectedValue();
                    applicantSelected = applicantHashMap.get(selectedTitle);
                    ArrayList<JobApplication> jobApps = hrBackend.getJobAppsByApplicantForCompany(applicantSelected);
                    showApplicationPanel(jobApps);
                }
            }
        });
    }

}

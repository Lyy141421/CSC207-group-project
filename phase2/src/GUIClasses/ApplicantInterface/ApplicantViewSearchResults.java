package GUIClasses.ApplicantInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class ApplicantViewSearchResults extends JPanel {
    private ApplicantBackend backEnd;
    private JPanel masterPanel;

    ApplicantViewSearchResults(ArrayList<CompanyJobPosting> jobPostings, ApplicantBackend backEnd, JPanel masterPanel) {
        this.backEnd = backEnd; this.masterPanel = masterPanel;
        this.setLayout(new GridLayout(1, 3));

        JPanel viewJobs0 = this.buildViewJobs0(jobPostings); this.add(viewJobs0);
        JPanel viewJobs1 = this.buildViewJobs1(jobPostings); this.add(viewJobs1);
        JPanel viewJobs2 = this.buildViewJobs2(jobPostings); this.add(viewJobs2);

        JList<String> jobTitlesList = new JList<>();
        for(Component c : viewJobs0.getComponents()) {
            if(c instanceof JList) {
                jobTitlesList = (JList<String>)c;
            }
        }
        jobTitlesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList actor =(JList)e.getSource();
                String cardFromActor = actor.getSelectedValue().toString().replaceAll("[^\\d.]", "");
                CardLayout c1 = (CardLayout)viewJobs1.getLayout();
                c1.show(viewJobs1, cardFromActor);
                CardLayout c2 = (CardLayout)viewJobs2.getLayout();
                c2.show(viewJobs2, cardFromActor);
            }
        });
    }

    /**
     * Builds the panel which allows navigation when viewing jobs
     */
    private JPanel buildViewJobs0(ArrayList<CompanyJobPosting> jobPostings) {
        JPanel viewJobsList = new JPanel(null);

        String[] jobTitleArray = backEnd.getListNames(jobPostings);
        JList<String> jobTitlesList = new JList<>(jobTitleArray);
        jobTitlesList.setBounds(25, 40, 234, 360);
        viewJobsList.add(jobTitlesList);

        JLabel jobTitlesText = new JLabel("Found the following job postings:", SwingConstants.CENTER);
        jobTitlesText.setBounds(42, 10, 200, 20);
        viewJobsList.add(jobTitlesText);

        JButton viewJobsExit = new JButton("Back");
        viewJobsExit.setBounds(92, 415, 100, 20);
        viewJobsExit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ((CardLayout)masterPanel.getLayout()).show(masterPanel, "Main");
            }
        });
        viewJobsList.add(viewJobsExit);

        return viewJobsList;
    }

    /**
     * Builds and returns the panel which contains all job titles, company and descriptions
     */
    private JPanel buildViewJobs1(ArrayList<CompanyJobPosting> jobPostings) {
        JPanel viewJobs1 = new JPanel(new CardLayout());

        for (CompanyJobPosting j : jobPostings) {
            JPanel viewJobsAdded = new JPanel(null);

            JLabel viewJobTitle = new JLabel(j.getTitle());
            viewJobTitle.setFont(new Font("Serif", Font.PLAIN, 20));
            viewJobTitle.setBounds(17, 70, 150, 40);
            viewJobsAdded.add(viewJobTitle);

            JLabel viewJobCompany = new JLabel(j.getCompany().getName());
            viewJobCompany.setFont(new Font("Serif", Font.BOLD, 15));
            viewJobCompany.setBounds(17, 100, 150, 30);
            viewJobsAdded.add(viewJobCompany);

            JLabel viewJobDesc = new JLabel("<html>" + j.getDescription() + "</html>");
            viewJobDesc.setBounds(17, 150, 255, 200);
            viewJobDesc.setVerticalAlignment(JLabel.TOP);
            viewJobsAdded.add(viewJobDesc);

            viewJobs1.add(viewJobsAdded, String.valueOf(j.getId()));
        }

        return viewJobs1;
    }

    /**
     * Returns the panel containing the remainder of the details for each job posting
     */
    private JPanel buildViewJobs2(ArrayList<CompanyJobPosting> companyJobPostings) {
        JPanel viewJobs2 = new JPanel(new CardLayout());

        for(CompanyJobPosting c : companyJobPostings) {
            JPanel viewJobsAdded2 = new JPanel(null);

            ArrayList<BranchJobPosting> jobpostings = backEnd.getApplicableBranchJobPostings(c);
            ArrayList<String> branches = new ArrayList<>();
            for(BranchJobPosting j: jobpostings) {
                branches.add(j.getBranch().getName());
            }

            JLabel branchText = new JLabel("Branch:", SwingConstants.CENTER);
            branchText.setBounds(17, 100, 100, 20);
            branchText.setName("STATIC"); viewJobsAdded2.add(branchText);

            for(BranchJobPosting j: jobpostings) {
                buildViewJobs2Helper(j, viewJobsAdded2);
            }

            JComboBox branchSelector = new JComboBox(branches.toArray());
            branchSelector.setBounds(117, 100, 150, 20);
            branchSelector.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String branchName = ((JComboBox)e.getSource()).getSelectedItem().toString();
                    for(Component c: viewJobsAdded2.getComponents()) {
                        if(c.getName().equals("STATIC")) {
                            c.setVisible(true);
                        } else if(c.getName().equals(branchName)) {
                            c.setVisible(true);
                        } else {
                            c.setVisible(false);
                        }
                    }
                }
            });
            branchSelector.setName("STATIC"); viewJobsAdded2.add(branchSelector);

            viewJobs2.add(viewJobsAdded2, String.valueOf(c.getId()));
        }
        return viewJobs2;
    }

    /**
     * Info of each individual branchposting
     */
    private void buildViewJobs2Helper(BranchJobPosting j, JPanel viewJobsAdded2) {
        String branchName = j.getBranch().getName();

        JLabel viewJobReqs = new JLabel("Requirements: " + j.getRequiredDocuments());
        viewJobReqs.setBounds(17, 150, 250, 20);
        viewJobReqs.setName(branchName); viewJobsAdded2.add(viewJobReqs);

        JLabel viewJobPos = new JLabel("# Of Positions: " + j.getNumPositions());
        viewJobPos.setBounds(17, 170, 250, 20);
        viewJobPos.setName(branchName); viewJobsAdded2.add(viewJobPos);

        JLabel viewJobDatePosted = new JLabel("Date Posted: " + j.getPostDate().toString());
        viewJobDatePosted.setBounds(17, 190, 250, 20);
        viewJobDatePosted.setName(branchName); viewJobsAdded2.add(viewJobDatePosted);

        JLabel viewJobDeadline = new JLabel("Deadline to Apply: " + j.getApplicantCloseDate().toString());
        viewJobDeadline.setBounds(17, 210, 250, 20);
        viewJobDeadline.setName(branchName); viewJobsAdded2.add(viewJobDeadline);

        JLabel viewJobIDNum = new JLabel("Posting ID: " + j.getId());
        viewJobIDNum.setBounds(17, 230, 250, 20);
        viewJobIDNum.setName(branchName); viewJobsAdded2.add(viewJobIDNum);

        JButton applyViaText = new JButton("Apply now with new documents");
        applyViaText.setBounds(25, 255, 230, 20);
        applyViaText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JobApplication jobApp = backEnd.createJobApplication(j);
                JPanel docPanel = new ApplicantTextDocSubmission(jobApp);
                add(docPanel, "FORMS");
                ((CardLayout)masterPanel.getLayout()).show(masterPanel, "FORMS");
            }
        } );
        applyViaText.setName(branchName); viewJobsAdded2.add(applyViaText);

        JButton applyViaExistingDocs = new JButton("Apply now with existing files");
        applyViaExistingDocs.setBounds(25, 285, 230, 20);
        applyViaExistingDocs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!backEnd.hasFilesInAccount()) {
                    JOptionPane.showMessageDialog(viewJobsAdded2, "You have no documents in your account");
                } else {
                    JPanel docPanel = new ApplicantFileSubmission(backEnd, j);
                    add(docPanel, "PICKDOCUMENT");
                    ((CardLayout) getLayout()).show(getThis(), "PICKDOCUMENT");
                }
            }
        } );
        applyViaExistingDocs.setName(branchName); viewJobsAdded2.add(applyViaExistingDocs);

        JButton applyViaNewDocs = new JButton("Apply now with new files");
        applyViaNewDocs.setBounds(25, 315, 230, 20);
        applyViaNewDocs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel docPanel = new ApplicantFileSubmission(backEnd, j);
                add(docPanel, "PICKDOCUMENT");
                ((CardLayout) masterPanel.getLayout()).show(masterPanel, "PICKDOCUMENT");
            }
        });
        applyViaNewDocs.setName(branchName); viewJobsAdded2.add(applyViaNewDocs);
    }

    /**
     * Because action listeners
     */
    private ApplicantViewSearchResults getThis() {
        return this;
    }
}

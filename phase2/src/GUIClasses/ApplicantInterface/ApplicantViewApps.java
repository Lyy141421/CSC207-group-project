package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Panel in which an applicant can view and withdraw from their open applications
 */
class ApplicantViewApps extends JPanel {
    private Applicant applicant;
    private ApplicantBackend backend;

    ApplicantViewApps(Applicant applicant) {
        this.applicant = applicant;
        this.backend = new ApplicantBackend(applicant);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel applicationInfo = this.makeAppInfo(applicant.getJobApplicationManager().getJobApplications());
        JPanel staticItems = this.makeStaticPanel(applicationInfo);

        c.weightx = 0.5; c.weighty = 0.3;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0; c.gridy = 0;
        this.add(staticItems, c);

        c.weightx = 0.5; c.weighty = 0.6;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0; c.gridy = 1; c.gridheight = 3;
        this.add(applicationInfo, c);
    }

    private JPanel makeStaticPanel(JPanel applicationInfo){
        JPanel ret = new JPanel(null);

        JLabel titleText = new JLabel("Current Applications", SwingConstants.CENTER);
        titleText.setBounds(170, 35, 300, 40);
        titleText.setFont(new Font("Serif", Font.PLAIN, 27));

        String[] postings = backend.getListNames(applicant.getJobApplicationManager().getCurrentJobAppsPostings());
        JComboBox selector = new JComboBox(postings);
        selector.setBounds(220, 100, 200, 30);
        selector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ((CardLayout)applicationInfo.getLayout()).show(applicationInfo,
                        ((String)selector.getSelectedItem()).replaceAll("[^\\d]", "" ));
            }
        });

        ret.add(titleText); ret.add(selector);
        return ret;
    }

    private JPanel makeAppInfo(ArrayList<JobApplication> applications) {
        JPanel ret = new JPanel(new CardLayout());

        for(JobApplication app : applications) {
            JPanel subRet = new JPanel(null);

            JLabel companyText1 = new JLabel("Company name: " + app.getJobPosting().getCompany().getName());
            companyText1.setBounds(100, 10, 300, 30);

            JLabel titleText1 = new JLabel("Job title: " + app.getJobPosting().getTitle());
            titleText1.setBounds(100, 50, 300, 30);

            JLabel statusText1 = new JLabel("Application Status: " + app.getStatus());
            statusText1.setBounds(100, 90, 300, 30);

            JButton withdrawButton1 = new JButton("Withdraw");
            withdrawButton1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    backend.withdrawApp(app);
                }
            } );
            withdrawButton1.setBounds(270, 200, 100, 30);

            subRet.add(companyText1); subRet.add(titleText1); subRet.add(statusText1);
            subRet.add(withdrawButton1);
            ret.add(subRet, app.getJobPosting().getId());
        }

        return ret;
    }
}

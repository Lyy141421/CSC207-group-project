package GUIClasses.HRInterface;

import CompanyStuff.JobPostings.CompanyJobPosting;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class HRAddPosting extends HRPanel {

    static int MAX_NUM_POSITIONS = 1000;

    JPanel containerPane = this;
    GridBagConstraints c;
    List<JComponent> entryBoxes = new ArrayList();
    HashMap<String, CompanyJobPosting> companyJPMap = new HashMap<>();

    JComboBox<String> companyPostingList;
    DefaultComboBoxModel<String> companyPostingModel;

    HRAddPosting(HRBackend hrBackEnd) {
        super(hrBackEnd);

        this.setLayout(new GridBagLayout());
        this.c = new GridBagConstraints();
        this.c.insets = new Insets(1, 0, 1, 0);

        this.addAllLabels();
        this.addAllFields();
        this.addButtons();
        this.setCompanyPostingListListener();
    }

    void reload() {
        //TODO: erase content of everything in entryBoxes
        revalidate();
        repaint();
    }

    private void addLabelToPanel(JComponent component) {
        c.gridy++;
        this.add(component, this.c);
    }

    private void addFieldToPanel (JComponent component) {
        addLabelToPanel(component);
        this.entryBoxes.add(component);
    }

    private void addAllLabels () {
        this.c.gridx = 0;
        this.c.gridy = -1;
        JLabel jobTitle = new JLabel("Job title");
        this.addLabelToPanel(jobTitle);
        JLabel jobField = new JLabel("Job field");
        this.addLabelToPanel(jobField);
        JLabel jobDescription = new JLabel("Job description");
        this.addLabelToPanel(jobDescription);
        JLabel requirements = new JLabel("Required document(s) (e.g: '3 Reference Letters')");
        this.addLabelToPanel(requirements);
        JLabel tags = new JLabel("Tag(s)");
        this.addLabelToPanel(tags);
        JLabel numPositions = new JLabel("Number of positions");
        this.addLabelToPanel(numPositions);
        JLabel closeDate = new JLabel("Applicant Close date");
        this.addLabelToPanel(closeDate);
        JLabel refCloseDate = new JLabel("Reference close date");
        this.addLabelToPanel(refCloseDate);
    }

    private void addAllFields () {
        this.c.gridx = 1;
        this.c.gridy = -1;
        this.addJobTitleSelection();
        //TODO: the textField isn't showing up at the right size.
        JTextField jobFieldInput = new JTextField(30);
        jobFieldInput.setMinimumSize(new Dimension(200, 30));
        this.addFieldToPanel(jobFieldInput);
        //TODO: the textArea isn't showing up at the right size.
        JTextArea jobDescriptionInput = new JTextArea(4, 30);
        jobDescriptionInput.setMinimumSize(new Dimension(100, 30));
        this.addFieldToPanel(jobDescriptionInput);
        this.addSelectionBox(CompanyJobPosting.RECOMMENDED_DOCUMENTS);
        this.addSelectionBox(CompanyJobPosting.RECOMMENDED_TAGS);
        this.addNumField();
        this.addDatePicker();
        this.addDatePicker();
    }

    private void addJobTitleSelection() {
        this.companyPostingList = new JComboBox<>();
        this.companyPostingList.setEditable(true);
        this.setCompanyJPMap(this.hrBackEnd.getHR().getBranch().getCompany().getCompanyJobPostings());
        this.companyPostingModel = new DefaultComboBoxModel<>(companyJPMap.keySet().toArray(new String[companyJPMap.size()]));
        this.companyPostingList.setModel(this.companyPostingModel);

        this.addFieldToPanel(this.companyPostingList);
    }

    private void setCompanyPostingListListener() {
        this.companyPostingList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //TODO: this needs testing 1. select an existing posting
                //                         2. select a different posting
                //                         3. input new title
                //                         4. select posting again
                String selectedTitle = (String)companyPostingList.getSelectedItem();
                if (companyJPMap.containsKey(selectedTitle)) {
                    CompanyJobPosting selectedJP = companyJPMap.get(selectedTitle);
                    //0.title, 1.field, 2.description, 3.required documents, 4.tags, 5.numOfPos, 6.close date, 7.numOfRef, 8.reference close date
                    //Company default： 0, 1, 2, 3, 4, 7
                    fillDefaultValue(selectedJP);
                    disableDefaultFields();
                } else {
                    for (JComponent component: entryBoxes) {
                        component.setEnabled(true);
                    }
                }
            }
        });
    }

    private void fillDefaultValue(CompanyJobPosting companyJobPosting) {
        ((JTextField)this.entryBoxes.get(1)).setText(companyJobPosting.getField());
        ((JTextArea)this.entryBoxes.get(2)).setText(companyJobPosting.getDescription());
        ((JTextField)this.entryBoxes.get(3)).setText(companyJobPosting.getDocsString());
        ((JTextField)this.entryBoxes.get(4)).setText(companyJobPosting.getTagsString());
    }

    private void disableDefaultFields() {
        this.entryBoxes.get(0).setEnabled(false);
        this.entryBoxes.get(1).setEnabled(false);
        this.entryBoxes.get(2).setEnabled(false);
        this.entryBoxes.get(3).setEnabled(false);
        this.entryBoxes.get(4).setEnabled(false);
        this.entryBoxes.get(7).setEnabled(false);
    }

    /**
     * Sets hash map of titles to company job postings from a list of job postings.
     * @param JPList a list of company job postings.
     */
    private void setCompanyJPMap(ArrayList<CompanyJobPosting> JPList) {
        for (CompanyJobPosting JP : JPList) {
            companyJPMap.put(this.toCompanyJPTitle(JP), JP);
        }
    }

    /**
     * Gets a string representation of the title of this company job posting.
     * @param companyJobPosting a company job posting.
     * @return the title to be displayed of this company job posting.
     */
    private String toCompanyJPTitle(CompanyJobPosting companyJobPosting) {
        return companyJobPosting.getId() + "-" + companyJobPosting.getTitle();
    }

    private void addDatePicker() {
        UtilDateModel dateModel = new UtilDateModel();
        int[] todayComponents = hrBackEnd.getTodayComponents();
        dateModel.setDate(todayComponents[0], todayComponents[1] - 1, todayComponents[2]);
        dateModel.setSelected(true);
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        JDatePickerImpl closeDateInput = new JDatePickerImpl(datePanel);
        this.addFieldToPanel(closeDateInput);
    }

    private void addNumField () {
        SpinnerNumberModel numModel = new SpinnerNumberModel(1, 1, MAX_NUM_POSITIONS, 1);
        JSpinner numInput = new JSpinner(numModel);

        this.addFieldToPanel(numInput);
    }

    private void addSelectionBox (String[] recommended) {
        JPanel selectionPanel = new JPanel();
        JTextField textInput = new JTextField(30);
        selectionPanel.add(textInput, BorderLayout.WEST);
        JPanel recommendedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (String label: recommended) {
            JCheckBox checkBox = new JCheckBox(label);
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    String currText = textInput.getText();
                    if (e.getStateChange()==ItemEvent.SELECTED) {
                        if (!currText.endsWith(";")) {
                            currText += ";";
                        }
                        currText += checkBox.getText() + ";";
                    } else if (e.getStateChange()==ItemEvent.DESELECTED) {
                        if (currText.contains(checkBox.getText())) {
                            currText = currText.replaceAll(checkBox.getText()+";*", "");
                        }
                    }
                    textInput.setText(currText);
                }
            });
            recommendedPanel.add(checkBox);
        }
        JScrollPane labelPane = new JScrollPane(recommendedPanel);
        labelPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        labelPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        selectionPanel.add(labelPane, BorderLayout.EAST);
        addLabelToPanel(selectionPanel);
        this.entryBoxes.add(textInput);
    }

    private JButton createSubmitButton() {
        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] mandatoryFields = getMandatoryFields();
                Optional<String[]> defaultFields = getDefaultFields();
                if (isValidInput(mandatoryFields, defaultFields)) {
                    if (!defaultFields.isPresent()) {   //TODO the isEmpty call was giving me an error
                        CompanyJobPosting companyJobPosting= companyJPMap.get(((JTextField)entryBoxes.get(0)).getText());
                        hrBackEnd.implementJobPosting(companyJobPosting, mandatoryFields);
                    } else {

                        hrBackEnd.addJobPosting(mandatoryFields, defaultFields.get());
                    }
                    JOptionPane.showMessageDialog(containerPane, "Job posting has been added.");
                } else {
                    JOptionPane.showMessageDialog(containerPane, "One or more fields have illegal input.");
                }
            }
        });

        return submit;
    }

    private Optional<String[]> getDefaultFields() {
        if (this.companyJPMap.containsKey(((JTextField)entryBoxes.get(0)).getText())) {
            return Optional.empty();
        }
        return Optional.of(new String[]{((JTextField)entryBoxes.get(0)).getText(),
                ((JTextField)entryBoxes.get(1)).getText(),
                ((JTextArea)entryBoxes.get(2)).getText(),
                ((JTextArea)entryBoxes.get(3)).getText(),
                ((JTextArea)entryBoxes.get(4)).getText(),
        });
    }

    private Object[] getMandatoryFields() {
        return new Object[]{((SpinnerNumberModel)((JSpinner)entryBoxes.get(5)).getModel()).getNumber(),
                ((Date)((JDatePanelImpl)entryBoxes.get(6)).getModel().getValue()).toInstant().
                        atZone(ZoneId.systemDefault()).toLocalDate(),
                ((Date)((JDatePanelImpl)entryBoxes.get(7)).getModel().getValue()).toInstant().
                        atZone(ZoneId.systemDefault()).toLocalDate(),
        };
    }

    private void addButtons () {
        JPanel buttons = new JPanel(new FlowLayout());
        JButton submit = this.createSubmitButton();

        buttons.add(submit);

        this.addLabelToPanel(buttons);
    }

    private boolean isValidInput(Object[] mandatoryFields, Optional<String[]> defaultFields) {
        boolean valid = true;

        if (defaultFields.isPresent()) {
            int i = 0;
            String[] defaultEntries = defaultFields.get();
            while (valid && i <= 4) {
                if (defaultEntries[i].equals("")) {
                    valid = false;
                }
                i++;
            }
        }

        if (!this.hrBackEnd.getToday().isBefore(((LocalDate) mandatoryFields[1]))) {
            valid = false;
        } else if (((LocalDate) mandatoryFields[2]).isBefore(((LocalDate) mandatoryFields[1]))) {
            valid = false;
        }

        return valid;
    }
}

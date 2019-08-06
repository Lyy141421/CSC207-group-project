package GUIClasses.HRInterface;

import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;
import GUIClasses.CommonUserGUI.GUIElementsCreator;
import GUIClasses.CommonUserGUI.UserMain;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

class HRAddOrUpdatePostingForm extends HRPanel {

    private static final int MAX_NUM_POSITIONS = 1000;

    // === Labels ===
    private static final String TITLE_LABEL = "Title: ";
    private static final String FIELD_LABEL = "Field: ";
    private static final String DESCRIPTION_LABEL = "Description: ";
    private static final String DOCUMENTS_LABEL = "Required Document(s): ";
    private static final String TAGS_LABEL = "Tag(s): ";
    private static final String POSITIONS_LABEL = "Number of Positions: ";
    private static final String APPLICANT_CLOSE_DATE_LABEL = "Applicant Close Date: ";

    // === Instance variables ===
    private JPanel containerPane = this;    // The container pane from which message dialogs will pop up from
    private List<JComponent> entryBoxes = new ArrayList<>();    // The list of optional entry boxes
    private HashMap<String, CompanyJobPosting> companyJPMap = new HashMap<>();  // The map of company job postings to be displayed
    private JScrollPane requiredDocumentsEntry;
    private JScrollPane tagsEntry;
    private JTextArea extraDocumentsEntry;
    private String documentInputInstructions;
    private JTextArea extraTagsEntry;
    private String tagsInputInstructions;
    private ArrayList<ArrayList<String>> fullOptionalSelectionInput = new ArrayList<>();
    private CompanyJobPosting selectedJP;
    private boolean toAdd;

    private JComboBox<String> companyPostingList;               // The list of company job postings to be displayed
    private DefaultComboBoxModel<String> companyPostingModel;   // The combo box for selecting a company posting

    // === Constructor ===
    HRAddOrUpdatePostingForm(HRBackend hrBackend, boolean toAdd, BranchJobPosting jobPostingToUpdate) {
        super(hrBackend);
        this.setLayout(null);
        if (jobPostingToUpdate != null) {
            selectedJP = jobPostingToUpdate;
        }
        this.toAdd = toAdd;
        this.addFieldsAndSubmitButton();
        this.addText();
        this.setCompanyPostingListListener();
        this.setJobTitleDocumentListener();
        this.resetForm();
    }

    private void addText() {
        Rectangle rect = new Rectangle(20, 40, 150, 30);
        ArrayList<String> labels = this.getArrayListOfLabels();

        for (String text : labels) {
            JLabel label = new JLabel(text, SwingConstants.LEFT);
            if (text.equals(FIELD_LABEL) || text.equals(APPLICANT_CLOSE_DATE_LABEL)) {
                label = new JLabel(text, SwingConstants.RIGHT);
                rect.x = 330;
                label.setBounds(rect);
                rect.x = 20;
            } else {
                label.setBounds(rect);
            }
            this.add(label);
            if (text.equals(DESCRIPTION_LABEL)) {
                rect.y += 110;
            } else if (text.equals(DOCUMENTS_LABEL) || text.equals(TAGS_LABEL)) {
                rect.y += 80;
            } else if (!(text.equals(TITLE_LABEL) || text.equals(POSITIONS_LABEL))) {
                rect.y += 40;
            }
        }
    }

    private ArrayList<String> getArrayListOfLabels() {
        return new ArrayList<>(Arrays.asList(TITLE_LABEL, FIELD_LABEL, DESCRIPTION_LABEL, DOCUMENTS_LABEL,
                TAGS_LABEL, POSITIONS_LABEL, APPLICANT_CLOSE_DATE_LABEL));
    }

    private void addFieldsAndSubmitButton() {
        Rectangle rect = new Rectangle(170, 40, 150, 30);
        this.addJobTitleEntry(rect);
        this.addFieldEntry(rect);
        this.addJobDescriptionEntry(rect);
        this.addRequiredDocumentsEntry(rect);
        this.addTagsEntry(rect);
        this.addNumberOfPositionsEntry(rect);
        this.addCloseDateEntry(rect);
        this.addSubmitButton(rect);
    }

    private void addJobTitleEntry(Rectangle rect) {
        this.addJobTitleSelection();
        companyPostingList.setBounds(rect);
        this.add(companyPostingList);
        this.entryBoxes.add(companyPostingList);
    }

    private void addFieldEntry(Rectangle rect) {
        JTextField jobFieldEntry = new JTextField();
        rect.x = 500;
        jobFieldEntry.setBounds(rect);
        this.add(jobFieldEntry);
        this.entryBoxes.add(jobFieldEntry);
    }

    private void addJobDescriptionEntry(Rectangle rect) {
        JScrollPane jobDescriptionEntry = new GUIElementsCreator().createTextAreaWithScrollBar("", true);
        rect.x = 170;
        rect.y += 40;
        rect.height = 100;
        rect.width = 300;
        jobDescriptionEntry.setBounds(rect);
        this.add(jobDescriptionEntry);
        this.entryBoxes.add((JTextArea) jobDescriptionEntry.getViewport().getView());
    }

    private void addRequiredDocumentsEntry(Rectangle rect) {
        requiredDocumentsEntry = this.createSelectionBox(CompanyJobPosting.RECOMMENDED_DOCUMENTS);
        rect.height = 70;
        rect.width = 225;
        rect.y += 110;
        requiredDocumentsEntry.setBounds(rect);
        this.add(requiredDocumentsEntry);

        rect.x = 430;
        documentInputInstructions = "Enter the other documents in a semi-colon separated list with no spaces. (e.g: 3 reference letters;Transcript)";
        JScrollPane extraDocumentsEntryScrollPane = new GUIElementsCreator().createTextAreaWithScrollBar(documentInputInstructions, true);
        extraDocumentsEntryScrollPane.setBounds(rect);
        this.add(extraDocumentsEntryScrollPane);
        extraDocumentsEntry = (JTextArea) extraDocumentsEntryScrollPane.getViewport().getView();
        this.entryBoxes.add(extraDocumentsEntry);
    }

    private void addTagsEntry(Rectangle rect) {
        tagsEntry = this.createSelectionBox(CompanyJobPosting.RECOMMENDED_TAGS);
        rect.x = 170;
        rect.y += 80;
        tagsEntry.setBounds(rect);
        this.add(tagsEntry);

        rect.x = 430;
        tagsInputInstructions = "Enter the other tags in a semi-colon separated list with no spaces. (e.g: University of Toronto;Java)";
        JScrollPane extraTagsEntryScrollPane = new GUIElementsCreator().createTextAreaWithScrollBar(tagsInputInstructions, true);
        extraTagsEntryScrollPane.setBounds(rect);
        this.add(extraTagsEntryScrollPane);
        extraTagsEntry = (JTextArea) extraTagsEntryScrollPane.getViewport().getView();
        this.entryBoxes.add(extraTagsEntry);
    }

    private void addNumberOfPositionsEntry(Rectangle rect) {
        JSpinner numPositionsEntry = this.createNumField();
        rect.x = 170;
        rect.height = 30;
        rect.width = 150;
        rect.y += 80;
        numPositionsEntry.setBounds(rect);
        this.add(numPositionsEntry);
        this.entryBoxes.add(numPositionsEntry);
    }

    private void addCloseDateEntry(Rectangle rect) {
        JDatePickerImpl applicantCloseDateEntry = this.createDatePicker();
        rect.x = 500;
        applicantCloseDateEntry.setBounds(rect);
        this.add(applicantCloseDateEntry);
        this.entryBoxes.add(applicantCloseDateEntry);
    }

    private void addJobTitleSelection() {
        this.companyPostingList = new JComboBox<>();
        this.companyPostingList.setEditable(true);
        if (this.toAdd) {
            this.setCompanyJPMap(this.hrBackend.getCompanyJobPostingsThatCanBeExtended());
        } else {
            this.setCompanyJPMap(new ArrayList<>(Arrays.asList(selectedJP)));
        }
        this.companyPostingModel = new DefaultComboBoxModel<>(companyJPMap.keySet().toArray(new String[companyJPMap.size()]));
        if (toAdd) {
            this.companyPostingModel.setSelectedItem(null); // default no item selected
        } else {
            this.companyPostingList.setSelectedItem(toCompanyJPTitle(selectedJP));
        }
        this.companyPostingList.setModel(this.companyPostingModel);
    }

    private void setJobTitleDocumentListener() {
        ((JTextComponent) ((JComboBox) entryBoxes.get(0)).getEditor().getEditorComponent()).getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                resetForm();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private void resetForm() {
        String jobTitleText = ((JTextComponent) ((JComboBox) entryBoxes.get(0)).getEditor().getEditorComponent()).getText();
        if (companyJPMap.keySet().contains(jobTitleText)) {
            if (!this.toAdd) {
                companyPostingList.setEditable(false);
                companyPostingList.setEnabled(false);
            }
            fillDefaultValues();
            disableDefaultFields();
        } else {
            if (!this.entryBoxes.get(1).isEnabled()) {
                enableAndClearAllInput();
            }
        }
    }

    private void enableAndClearAllInput() {
        for (JComponent component : entryBoxes) {
            component.setEnabled(true);
            if (component instanceof JTextComponent) {
                ((JTextComponent) component).setText("");
                component.revalidate();
            }
        }
        enableAllCheckBoxes(requiredDocumentsEntry);
        requiredDocumentsEntry.revalidate();
        enableAllCheckBoxes(tagsEntry);
        tagsEntry.revalidate();
        extraDocumentsEntry.setText(documentInputInstructions);
        extraDocumentsEntry.revalidate();
        extraTagsEntry.setText(tagsInputInstructions);
        extraTagsEntry.revalidate();
    }

    private void setCompanyPostingListListener() {
        this.companyPostingList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String selectedTitle = (String) companyPostingList.getSelectedItem();
                if (companyJPMap.containsKey(selectedTitle)) {
                    selectedJP = companyJPMap.get(selectedTitle);
                    resetForm();
                }
            }
        });
    }

    private void fillDefaultValues() {
        ((JTextField) this.entryBoxes.get(1)).setText(selectedJP.getField());
        ((JTextArea) entryBoxes.get(2)).setText(selectedJP.getDescription());
        ((JTextArea) entryBoxes.get(3)).setText(selectedJP.getDocsStringNoRecommended());
        ((JTextArea) entryBoxes.get(4)).setText(selectedJP.getTagsStringNoRecommended());
        this.setDefaultCheckBoxesForDocsAndTags();
    }

    private void setDefaultCheckBoxesForDocsAndTags() {
        this.setDefaultCheckBoxes(requiredDocumentsEntry, selectedJP.getRecommendedDocumentsUsed());
        this.setDefaultCheckBoxes(tagsEntry, selectedJP.getRecommendedTagsUsed());
    }

    private void setDefaultCheckBoxes(JScrollPane scrollPaneWithCheckBoxes, ArrayList<String> recommendedItemsIncluded) {
        Component[] checkBoxesDocs = ((JPanel) scrollPaneWithCheckBoxes.getViewport().getView()).getComponents();
        for (Component checkBox : checkBoxesDocs) {
            if (recommendedItemsIncluded.contains(((JCheckBox) checkBox).getText())) {
                ((JCheckBox) checkBox).setSelected(true);
            }
            checkBox.setEnabled(false);
        }
    }

    private void enableAllCheckBoxes(JScrollPane scrollPaneWithCheckBoxes) {
        Component[] checkBoxesDocs = ((JPanel) scrollPaneWithCheckBoxes.getViewport().getView()).getComponents();
        for (Component checkBox : checkBoxesDocs) {
            checkBox.setEnabled(true);
            ((JCheckBox) checkBox).setSelected(false);
        }
    }

    private void disableDefaultFields() {
        for (int i = 1; i < 5; i++) {
            entryBoxes.get(i).setEnabled(false);
        }
    }

    /**
     * Sets hash map of titles to company job postings from a list of job postings.
     *
     * @param JPList a list of company job postings.
     */
    private void setCompanyJPMap(ArrayList<CompanyJobPosting> JPList) {
        for (CompanyJobPosting JP : JPList) {
            companyJPMap.put(this.toCompanyJPTitle(JP), JP);
        }
    }

    /**
     * Gets a string representation of the title of this company job posting.
     *
     * @param companyJobPosting a company job posting.
     * @return the title to be displayed of this company job posting.
     */
    private String toCompanyJPTitle(CompanyJobPosting companyJobPosting) {
        return companyJobPosting.getId() + " - " + companyJobPosting.getTitle();
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel dateModel = new UtilDateModel();
        int[] todayComponents = hrBackend.getTomorrowComponents();
        dateModel.setDate(todayComponents[0], todayComponents[1] - 1, todayComponents[2]);
        dateModel.setSelected(true);
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        JDatePickerImpl closeDateInput = new JDatePickerImpl(datePanel);
        return closeDateInput;
    }

    private JSpinner createNumField() {
        SpinnerNumberModel numModel = new SpinnerNumberModel(1, 1, MAX_NUM_POSITIONS, 1);
        JSpinner numInput = new JSpinner(numModel);
        return numInput;
    }

    private JScrollPane createSelectionBox(String[] recommended) {
        JPanel recommendedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ArrayList<String> checkedInput = new ArrayList<>();
        for (String label : recommended) {
            JCheckBox checkBox = new JCheckBox(label);
            this.addCheckBoxItemListener(checkBox, checkedInput);
            recommendedPanel.add(checkBox);
        }
        JScrollPane labelPane = new JScrollPane(recommendedPanel);
        labelPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        labelPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.fullOptionalSelectionInput.add(checkedInput);
        return labelPane;
    }

    private void setAllSelectedAndInputtedItems(int index, JTextArea textInput, String instructions) {
        String text = textInput.getText();
        if (!text.equals(instructions)) {
            if (text.charAt(text.length() - 1) == ';') {
                text = text.substring(0, text.length() - 1);
            }
        } else {
            text = "";
        }
        String[] inputtedItems = text.split(";");
        for (String item : inputtedItems) {
            if (!item.isEmpty()) {
                fullOptionalSelectionInput.get(index).add(item);
            }
        }
        this.removeListDuplicates(fullOptionalSelectionInput.get(index));
    }

    private void removeListDuplicates(ArrayList<String> list) {
        ArrayList<String> listClone = (ArrayList<String>) list.clone();
        list.clear();
        for (String item : listClone) {
            String itemFormatted = hrBackend.formatCase(item);
            if (!list.contains(itemFormatted)) {
                list.add(itemFormatted);
            }
        }
    }

    private void addCheckBoxItemListener(JCheckBox checkBox, ArrayList<String> checkedInput) {
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkedInput.add(checkBox.getText());
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    checkedInput.remove(checkBox.getText());
                }
            }
        });
    }

    private void addSubmitButton(Rectangle rect) {
        JButton submitButton = this.createSubmitButton();
        rect.x = 270;
        rect.y += 40;
        submitButton.setBounds(rect);
        this.add(submitButton);
    }

    private JButton createSubmitButton() {
        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] mandatoryFields = getMandatoryFields();
                Optional<String[]> defaultFields = getDefaultFields();
                if (isValidInput(mandatoryFields, defaultFields)) {
                    if (toAdd) {
                        addJobPosting(defaultFields, mandatoryFields);
                    } else {
                        updateJobPosting(mandatoryFields);
                    }
                    Thread newThread = new Thread() {
                        public void run() {
                            try {
                                SwingUtilities.invokeAndWait(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((UserMain) containerPane.getParent().getParent()).refresh();
                                    }
                                });
                            } catch (InterruptedException | InvocationTargetException ex) {
                                ex.printStackTrace();
                            }
                        }
                    };
                    newThread.start();
                } else {
                    JOptionPane.showMessageDialog(containerPane, "One or more fields have illegal input.");
                }
            }
        });
        return submit;
    }

    private void addJobPosting(Optional<String[]> defaultFields, Object[] mandatoryFields) {
        String field;
        if (defaultFields.isPresent()) {
            field = defaultFields.get()[1];
        } else {
            field = selectedJP.getField();
        }
        boolean create = true;
        if (!hrBackend.hasInterviewerOfField(field)) {
            create = JOptionPane.showConfirmDialog(containerPane, "There are no interviewers for the field you entered. Would you like to create the job posting anyways?",
                    "Warning", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;
        }
        if (create) {
            if (!defaultFields.isPresent()) {
                hrBackend.implementJobPosting(selectedJP, mandatoryFields);
            } else {
                hrBackend.addJobPosting(mandatoryFields, defaultFields.get());
            }
            JOptionPane.showMessageDialog(containerPane, "Job posting has been added.");
            this.resetForm();
        }
    }

    private void updateJobPosting(Object[] mandatoryFields) {
        hrBackend.updateJobPosting((BranchJobPosting) selectedJP, mandatoryFields);
        JOptionPane.showMessageDialog(containerPane, "Job posting has been updated.");
    }

    private Optional<String[]> getDefaultFields() {
        String jobTitleText = ((JTextComponent) ((JComboBox) entryBoxes.get(0)).getEditor().getEditorComponent()).getText();
        if (this.companyJPMap.containsKey(jobTitleText)) {
            return Optional.empty();
        }
        this.setAllSelectedAndInputtedItems(0, extraDocumentsEntry, documentInputInstructions);
        this.setAllSelectedAndInputtedItems(1, extraTagsEntry, tagsInputInstructions);
        return Optional.of(new String[]{jobTitleText,  // title
                ((JTextField) entryBoxes.get(1)).getText(),  // field
                ((JTextArea) entryBoxes.get(2)).getText(),   // description
                (this.convertListToString(this.fullOptionalSelectionInput.get(0))),   // documents
                (this.convertListToString(this.fullOptionalSelectionInput.get(1))),   // tags
        });
    }

    private String convertListToString(ArrayList<String> items) {
        String s = "";
        for (String item : items) {
            s += ";" + item;
        }
        if (!s.isEmpty()) {
            s = s.substring(1);
        }
        return s;
    }

    private Object[] getMandatoryFields() {
        return new Object[]{((SpinnerNumberModel) ((JSpinner) entryBoxes.get(5)).getModel()).getNumber(),
                ((Date) ((JDatePickerImpl) entryBoxes.get(6)).getModel().getValue()).toInstant().
                        atZone(ZoneId.systemDefault()).toLocalDate()};
    }

    private boolean isValidInput(Object[] mandatoryFields, Optional<String[]> defaultFields) {
        boolean valid = true;

        if (defaultFields.isPresent()) {
            int i = 0;
            String[] defaultEntries = defaultFields.get();
            while (valid && i < 3) {
                if (defaultEntries[i].equals("")) {
                    valid = false;
                }
                i++;
            }
            //TODO: regex to match something other than ; or " ".
            if (defaultEntries[4].equals("")) {
                valid = false;
            }
        }

        if (!this.hrBackend.getToday().isBefore(((LocalDate) mandatoryFields[1]))) {
            valid = false;
        }
        return valid;
    }
}

package GUIClasses.HRInterface;

import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;
import GUIClasses.CommonUserGUI.GUIElementsCreator;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

class HRAddOrUpdatePostingForm extends HRPanel {

    private static final int MAX_NUM_POSITIONS = 1000;

    // === Bound positions ===
    private static final int X_COLUMN_1 = 20;
    private static final int X_COLUMN_2 = 170;
    private static final int X_COLUMN_3 = 330;
    private static final int X_COLUMN_3_HALF = 430;
    private static final int X_COLUMN_4 = 500;
    private static final int X_CENTER = 270;
    private static final int STANDARD_WIDTH = 150;
    private static final int MEDIUM_WIDTH = 225;
    private static final int LARGER_WIDTH = 300;
    private static final int Y_START = 10;
    private static final int STANDARD_Y_INCREMENT = 40;
    private static final int MEDIUM_Y_INCREMENT = 80;
    private static final int LARGER_Y_INCREMENT = 110;
    private static final int STANDARD_HEIGHT = 30;
    private static final int MEDIUM_HEIGHT = 70;
    private static final int LARGER_HEIGHT = 100;

    // === Labels ===
    private static final String TITLE_LABEL = "Title: ";
    private static final String FIELD_LABEL = "Field: ";
    private static final String DESCRIPTION_LABEL = "Description: ";
    private static final String DOCUMENTS_LABEL = "Required Document(s): ";
    private static final String TAGS_LABEL = "Tag(s): ";
    private static final String POSITIONS_LABEL = "Number of Positions: ";
    private static final String APPLICANT_CLOSE_DATE_LABEL = "Applicant Close Date: ";
    private static final String REFERENCE_CLOSE_DATE_LABEL = "Reference Close Date: ";

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
    private ArrayList<StringBuilder> fullOptionalSelectionInput = new ArrayList<>();
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
    }

    private void addText() {
        Rectangle rect = new Rectangle(X_COLUMN_1, Y_START, STANDARD_WIDTH, STANDARD_HEIGHT);
        ArrayList<String> labels = this.getArrayListOfLabels();

        for (String text : labels) {
            JLabel label = new JLabel(text, SwingConstants.LEFT);
            if (text.equals(FIELD_LABEL) || text.equals(REFERENCE_CLOSE_DATE_LABEL)) {
                label = new JLabel(text, SwingConstants.RIGHT);
                rect.x = X_COLUMN_3;
                label.setBounds(rect);
                rect.x = X_COLUMN_1;
            } else {
                label.setBounds(rect);
            }
            this.add(label);
            if (text.equals(DESCRIPTION_LABEL)) {
                rect.y += LARGER_Y_INCREMENT;
            } else if (text.equals(DOCUMENTS_LABEL) || text.equals(TAGS_LABEL)) {
                rect.y += MEDIUM_Y_INCREMENT;
            } else if (!(text.equals(TITLE_LABEL) || text.equals(APPLICANT_CLOSE_DATE_LABEL))) {
                rect.y += STANDARD_Y_INCREMENT;
            }
        }
    }

    private ArrayList<String> getArrayListOfLabels() {
        return new ArrayList<>(Arrays.asList(TITLE_LABEL, FIELD_LABEL, DESCRIPTION_LABEL, DOCUMENTS_LABEL,
                TAGS_LABEL, POSITIONS_LABEL, APPLICANT_CLOSE_DATE_LABEL, REFERENCE_CLOSE_DATE_LABEL));
    }

    private void addFieldsAndSubmitButton() {
        Rectangle rect = new Rectangle(X_COLUMN_2, Y_START, STANDARD_WIDTH,
                STANDARD_HEIGHT);
        this.addJobTitleEntry(rect);
        this.addFieldEntry(rect);
        this.addJobDescriptionEntry(rect);
        this.addRequiredDocumentsEntry(rect);
        this.addTagsEntry(rect);
        this.addNumberOfPositionsEntry(rect);
        this.addCloseDateEntries(rect);
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
        rect.x = X_COLUMN_4;
        jobFieldEntry.setBounds(rect);
        this.add(jobFieldEntry);
        this.entryBoxes.add(jobFieldEntry);
    }

    private void addJobDescriptionEntry(Rectangle rect) {
        JScrollPane jobDescriptionEntry = new GUIElementsCreator().createTextAreaWithScrollBar("", true);
        rect.x = X_COLUMN_2;
        rect.y += STANDARD_Y_INCREMENT;
        rect.height = LARGER_HEIGHT;
        rect.width = LARGER_WIDTH;
        jobDescriptionEntry.setBounds(rect);
        this.add(jobDescriptionEntry);
        this.entryBoxes.add((JTextArea) jobDescriptionEntry.getViewport().getView());
    }

    private void addRequiredDocumentsEntry(Rectangle rect) {
        requiredDocumentsEntry = this.createSelectionBox(CompanyJobPosting.RECOMMENDED_DOCUMENTS);
        rect.height = MEDIUM_HEIGHT;
        rect.width = MEDIUM_WIDTH;
        rect.y += LARGER_Y_INCREMENT;
        requiredDocumentsEntry.setBounds(rect);
        this.add(requiredDocumentsEntry);

        rect.x = X_COLUMN_3_HALF;
        documentInputInstructions = "Enter the documents in a semi-colon separated list with no spaces. (e.g: 3 reference letters;Transcript)";
        JScrollPane extraDocumentsEntryScrollPane = new GUIElementsCreator().createTextAreaWithScrollBar(documentInputInstructions, true);
        extraDocumentsEntryScrollPane.setBounds(rect);
        this.add(extraDocumentsEntryScrollPane);
        extraDocumentsEntry = (JTextArea) extraDocumentsEntryScrollPane.getViewport().getView();
        this.entryBoxes.add(extraDocumentsEntry);
    }

    private void addTagsEntry(Rectangle rect) {
        tagsEntry = this.createSelectionBox(CompanyJobPosting.RECOMMENDED_TAGS);
        rect.x = X_COLUMN_2;
        rect.y += MEDIUM_Y_INCREMENT;
        tagsEntry.setBounds(rect);
        this.add(tagsEntry);

        rect.x = X_COLUMN_3_HALF;
        tagsInputInstructions = "Enter the tags in a semi-colon separated list with no spaces. (e.g: University of Toronto;Java)";
        JScrollPane extraTagsEntryScrollPane = new GUIElementsCreator().createTextAreaWithScrollBar(tagsInputInstructions, true);
        extraTagsEntryScrollPane.setBounds(rect);
        this.add(extraTagsEntryScrollPane);
        extraTagsEntry = (JTextArea) extraTagsEntryScrollPane.getViewport().getView();
        this.entryBoxes.add(extraTagsEntry);
    }

    private void addNumberOfPositionsEntry(Rectangle rect) {
        JSpinner numPositionsEntry = this.createNumField();
        rect.x = X_COLUMN_2;
        rect.height = STANDARD_HEIGHT;
        rect.width = STANDARD_WIDTH;
        rect.y += MEDIUM_Y_INCREMENT;
        numPositionsEntry.setBounds(rect);
        this.add(numPositionsEntry);
        this.entryBoxes.add(numPositionsEntry);
    }

    private void addCloseDateEntries(Rectangle rect) {
        JDatePickerImpl applicantCloseDateEntry = this.createDatePicker();
        rect.y += STANDARD_Y_INCREMENT;
        applicantCloseDateEntry.setBounds(rect);
        this.add(applicantCloseDateEntry);
        this.entryBoxes.add(applicantCloseDateEntry);

        JDatePickerImpl referenceCloseDateEntry = this.createDatePicker();
        rect.x = X_COLUMN_4;
        referenceCloseDateEntry.setBounds(rect);
        this.add(referenceCloseDateEntry);
        this.entryBoxes.add(referenceCloseDateEntry);
    }

    private void addJobTitleSelection() {
        this.companyPostingList = new JComboBox<>();
        this.companyPostingList.setEditable(true);
        this.setCompanyJPMap(this.hrBackend.getHR().getBranch().getCompany().getCompanyJobPostings());
        this.companyPostingModel = new DefaultComboBoxModel<>(companyJPMap.keySet().toArray(new String[companyJPMap.size()]));
        if (toAdd) {
            this.companyPostingModel.setSelectedItem(null);
        } else {
            this.companyPostingList.setSelectedItem(selectedJP);
        }
        this.companyPostingList.setModel(this.companyPostingModel);
    }

    private void setJobTitleDocumentListener() {
        ((JTextComponent) ((JComboBox) entryBoxes.get(0)).getEditor().getEditorComponent()).getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                resetForm();
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
        if (!companyJPMap.keySet().contains(jobTitleText)) {
            enableAndClearAllInput();
        }
    }

    private void enableAndClearAllInput() {
        for (JComponent component : entryBoxes) {
            component.setEnabled(true);
            if (component instanceof JTextComponent) {
                ((JTextComponent) component).setText("");
            }
        }
        enableAllCheckBoxes(requiredDocumentsEntry);
        enableAllCheckBoxes(tagsEntry);
    }

    private void setCompanyPostingListListener() {
        this.companyPostingList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //TODO: this needs testing 1. select an existing posting
                //                         2. select a different posting
                //                         3. input new title
                //                         4. select posting again
                String selectedTitle = (String) companyPostingList.getSelectedItem();
                if (toAdd) {
                    if (companyJPMap.containsKey(selectedTitle)) {
                        selectedJP = companyJPMap.get(selectedTitle);
                        //0.title, 1.field, 2.description, 3.required documents, 4.tags, 5.numOfPos, 6.close date, 7.reference close date
                        //Company default： 0, 1, 2, 3, 4
                        fillDefaultValues();
                        disableDefaultFields();
                    } else {
                        addFieldsAndSubmitButton();
                        enableAndClearAllInput();
                    }
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
        StringBuilder checkedInput = new StringBuilder();
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
        StringBuilder fullTextInput = this.fullOptionalSelectionInput.get(index);
        if (fullTextInput.charAt(0) == ';') {
            fullTextInput.deleteCharAt(0);
        }
        if (!textInput.getText().equals(instructions)) {
            if (fullTextInput.charAt(fullTextInput.length() - 1) != ';') {
                fullTextInput.append(';');
            }
            fullTextInput.append(textInput.getText());
            if (fullTextInput.charAt(fullTextInput.length() - 1) == ';') {
                fullTextInput.deleteCharAt(fullTextInput.length() - 1);
            }
        }
    }

    private void addCheckBoxItemListener(JCheckBox checkBox, StringBuilder checkedInput) {
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String currText = checkedInput.toString();
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!currText.endsWith(";")) {
                        currText += ";";
                    }
                    currText += checkBox.getText() + ";";
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    if (currText.contains(checkBox.getText())) {
                        currText = currText.replaceAll(checkBox.getText() + ";*", "");
                    }
                }
                checkedInput.append(currText);
            }
        });
    }

    private void addSubmitButton(Rectangle rect) {
        JButton submitButton = this.createSubmitButton();
        rect.x = X_CENTER;
        rect.y += STANDARD_Y_INCREMENT;
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
                } else {
                    JOptionPane.showMessageDialog(containerPane, "One or more fields have illegal input.");
                }
            }
        });
        return submit;
    }

    private void addJobPosting(Optional<String[]> defaultFields, Object[] mandatoryFields) {
        if (!defaultFields.isPresent()) {
            hrBackend.implementJobPosting(selectedJP, mandatoryFields);
        } else {
            hrBackend.addJobPosting(mandatoryFields, defaultFields.get());
        }
        JOptionPane.showMessageDialog(containerPane, "Job posting has been added.");
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
                (this.fullOptionalSelectionInput.get(0).toString()),   // documents
                (this.fullOptionalSelectionInput.get(1).toString()),   // tags
        });
    }

    private Object[] getMandatoryFields() {
        return new Object[]{((SpinnerNumberModel) ((JSpinner) entryBoxes.get(5)).getModel()).getNumber(),
                ((Date) ((JDatePickerImpl) entryBoxes.get(6)).getModel().getValue()).toInstant().
                        atZone(ZoneId.systemDefault()).toLocalDate(),
                ((Date) ((JDatePickerImpl) entryBoxes.get(7)).getModel().getValue()).toInstant().
                        atZone(ZoneId.systemDefault()).toLocalDate(),
        };
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

        if (!this.hrBackend.getToday().isBefore(((LocalDate) mandatoryFields[1]))) {
            valid = false;
        } else if (!((LocalDate) mandatoryFields[2]).isAfter(((LocalDate) mandatoryFields[1]))) {
            valid = false;
        }

        return valid;
    }
}

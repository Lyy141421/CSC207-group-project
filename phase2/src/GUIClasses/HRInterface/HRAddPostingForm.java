package GUIClasses.HRInterface;

import CompanyStuff.JobPostings.CompanyJobPosting;
import GUIClasses.CommonUserGUI.GUIElementsCreator;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
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

class HRAddPostingForm extends HRPanel {

    private static final int MAX_NUM_POSITIONS = 1000;

    // === Bound positions ===
    private static final int RECTANGLE_X_COLUMN_1 = 20;
    private static final int RECTANGLE_X_COLUMN_2 = 170;
    private static final int RECTANGLE_X_COLUMN_3 = 330;
    private static final int RECTANGLE_X_COLUMN_3_HALF = 430;
    private static final int RECTANGLE_X_COLUMN_4 = 500;
    private static final int RECTANGLE_X_CENTER = 270;
    private static final int STANDARD_RECTANGLE_WIDTH = 150;
    private static final int MEDIUM_RECTANGLE_WIDTH = 225;
    private static final int LARGER_RECTANGLE_WIDTH = 300;
    private static final int RECTANGLE_START_Y = 10;
    private static final int STANDARD_RECTANGLE_Y_INCREMENT = 40;
    private static final int MEDIUM_RECTANGLE_Y_INCREMENT = 80;
    private static final int LARGER_RECTANGLE_Y_INCREMENT = 110;
    private static final int STANDARD_RECTANGLE_HEIGHT = 30;
    private static final int MEDIUM_RECTANGLE_HEIGHT = 70;
    private static final int LARGER_RECTANGLE_HEIGHT = 100;

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

    private JComboBox<String> companyPostingList;               // The list of company job postings to be displayed
    private DefaultComboBoxModel<String> companyPostingModel;   // The combo box for selecting a company posting

    // === Constructor ===
    HRAddPostingForm(HRBackend hrBackend) {
        super(hrBackend);
        this.setLayout(null);
        this.addFieldsAndSubmitButton();
        this.addText();
        this.setCompanyPostingListListener();
    }

    private void addText() {
        Rectangle rect = new Rectangle(RECTANGLE_X_COLUMN_1, RECTANGLE_START_Y, STANDARD_RECTANGLE_WIDTH, STANDARD_RECTANGLE_HEIGHT);
        ArrayList<String> labels = this.getArrayListOfLabels();

        for (String text : labels) {
            JLabel label = new JLabel(text, SwingConstants.LEFT);
            if (text.equals(FIELD_LABEL) || text.equals(REFERENCE_CLOSE_DATE_LABEL)) {
                label = new JLabel(text, SwingConstants.RIGHT);
                rect.x = RECTANGLE_X_COLUMN_3;
                label.setBounds(rect);
                rect.x = RECTANGLE_X_COLUMN_1;
            } else {
                label.setBounds(rect);
            }
            this.add(label);
            if (text.equals(DESCRIPTION_LABEL)) {
                rect.y += LARGER_RECTANGLE_Y_INCREMENT;
            } else if (text.equals(DOCUMENTS_LABEL) || text.equals(TAGS_LABEL)) {
                rect.y += MEDIUM_RECTANGLE_Y_INCREMENT;
            } else if (!(text.equals(TITLE_LABEL) || text.equals(APPLICANT_CLOSE_DATE_LABEL))) {
                rect.y += STANDARD_RECTANGLE_Y_INCREMENT;
            }
        }
    }

    private ArrayList<String> getArrayListOfLabels() {
        return new ArrayList<>(Arrays.asList(TITLE_LABEL, FIELD_LABEL, DESCRIPTION_LABEL, DOCUMENTS_LABEL,
                TAGS_LABEL, POSITIONS_LABEL, APPLICANT_CLOSE_DATE_LABEL, REFERENCE_CLOSE_DATE_LABEL));
    }

    private void addFieldsAndSubmitButton() {
        Rectangle rect = new Rectangle(RECTANGLE_X_COLUMN_2, RECTANGLE_START_Y, STANDARD_RECTANGLE_WIDTH,
                STANDARD_RECTANGLE_HEIGHT);
        this.addJobTitleAndFieldEntries(rect);
        this.addJobDescriptionEntry(rect);
        this.addRequiredDocumentsEntry(rect);
        this.addTagsEntry(rect);
        this.addNumberOfPositionsEntry(rect);
        this.addCloseDateEntries(rect);
        this.addSubmitButton(rect);
    }

    private void addJobTitleAndFieldEntries(Rectangle rect) {
        this.addJobTitleSelection();
        companyPostingList.setBounds(rect);
        this.add(companyPostingList);
        this.entryBoxes.add(companyPostingList);

        JTextField jobFieldEntry = new JTextField();
        rect.x = RECTANGLE_X_COLUMN_4;
        jobFieldEntry.setBounds(rect);
        this.add(jobFieldEntry);
        this.entryBoxes.add(jobFieldEntry);
    }

    private void addJobDescriptionEntry(Rectangle rect) {
        JScrollPane jobDescriptionEntry = new GUIElementsCreator().createEditableTextAreaWithScrollBar("");
        rect.x = RECTANGLE_X_COLUMN_2;
        rect.y += STANDARD_RECTANGLE_Y_INCREMENT;
        rect.height = LARGER_RECTANGLE_HEIGHT;
        rect.width = LARGER_RECTANGLE_WIDTH;
        jobDescriptionEntry.setBounds(rect);
        this.add(jobDescriptionEntry);
        this.entryBoxes.add((JTextArea) jobDescriptionEntry.getViewport().getView());
    }

    private void addRequiredDocumentsEntry(Rectangle rect) {
        String instructions = "Enter the documents in a semi-colon separated list with no spaces. (e.g: 3 reference letters;Transcript)";
        JScrollPane extraRequiredDocumentsEntry = new GUIElementsCreator().createEditableTextAreaWithScrollBar(instructions);

        requiredDocumentsEntry = this.createSelectionBox(CompanyJobPosting.RECOMMENDED_DOCUMENTS,
                (JTextArea) extraRequiredDocumentsEntry.getViewport().getView(), instructions);
        rect.height = MEDIUM_RECTANGLE_HEIGHT;
        rect.width = MEDIUM_RECTANGLE_WIDTH;
        rect.y += LARGER_RECTANGLE_Y_INCREMENT;
        requiredDocumentsEntry.setBounds(rect);
        this.add(requiredDocumentsEntry);

        rect.x = RECTANGLE_X_COLUMN_3_HALF;
        extraRequiredDocumentsEntry.setBounds(rect);
        this.add(extraRequiredDocumentsEntry);
    }

    private void addTagsEntry(Rectangle rect) {
        String instructions = "Enter the tags in a semi-colon separated list with no spaces. (e.g: University of Toronto;Java)";
        JScrollPane extraTagsEntry = new GUIElementsCreator().createEditableTextAreaWithScrollBar(instructions);

        tagsEntry = this.createSelectionBox(CompanyJobPosting.RECOMMENDED_TAGS,
                (JTextArea) extraTagsEntry.getViewport().getView(), instructions);
        rect.x = RECTANGLE_X_COLUMN_2;
        rect.y += MEDIUM_RECTANGLE_Y_INCREMENT;
        tagsEntry.setBounds(rect);
        this.add(tagsEntry);

        rect.x = RECTANGLE_X_COLUMN_3_HALF;
        extraTagsEntry.setBounds(rect);
        this.add(extraTagsEntry);
    }

    private void addNumberOfPositionsEntry(Rectangle rect) {
        JSpinner numPositionsEntry = this.createNumField();
        rect.x = RECTANGLE_X_COLUMN_2;
        rect.height = STANDARD_RECTANGLE_HEIGHT;
        rect.width = STANDARD_RECTANGLE_WIDTH;
        rect.y += MEDIUM_RECTANGLE_Y_INCREMENT;
        numPositionsEntry.setBounds(rect);
        this.add(numPositionsEntry);
        this.entryBoxes.add(numPositionsEntry);
    }

    private void addCloseDateEntries(Rectangle rect) {
        JDatePickerImpl applicantCloseDateEntry = this.createDatePicker();
        rect.y += STANDARD_RECTANGLE_Y_INCREMENT;
        applicantCloseDateEntry.setBounds(rect);
        this.add(applicantCloseDateEntry);
        this.entryBoxes.add(applicantCloseDateEntry);

        JDatePickerImpl referenceCloseDateEntry = this.createDatePicker();
        rect.x = RECTANGLE_X_COLUMN_4;
        referenceCloseDateEntry.setBounds(rect);
        this.add(referenceCloseDateEntry);
        this.entryBoxes.add(referenceCloseDateEntry);
    }

    private void addJobTitleSelection() {
        this.companyPostingList = new JComboBox<>();
        this.companyPostingList.setEditable(true);
        this.setCompanyJPMap(this.hrBackend.getHR().getBranch().getCompany().getCompanyJobPostings());
        this.companyPostingModel = new DefaultComboBoxModel<>(companyJPMap.keySet().toArray(new String[companyJPMap.size()]));
        this.companyPostingList.setModel(this.companyPostingModel);
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
                if (companyJPMap.containsKey(selectedTitle)) {
                    CompanyJobPosting selectedJP = companyJPMap.get(selectedTitle);
                    //0.title, 1.field, 2.description, 3.required documents, 4.tags, 5.numOfPos, 6.close date, 7.reference close date
                    //Company default： 0, 1, 2, 3, 4
                    fillDefaultValue(selectedJP);
                    disableDefaultFields();
                } else {
                    for (JComponent component : entryBoxes) {
                        component.setEnabled(true);
                    }
                }
            }
        });
    }

    private void fillDefaultValue(CompanyJobPosting companyJobPosting) {
        ((JTextField) this.entryBoxes.get(1)).setText(companyJobPosting.getField());
        ((JTextArea) entryBoxes.get(2)).setText(companyJobPosting.getDescription());
        ((JTextArea) entryBoxes.get(3)).setText(companyJobPosting.getDocsString());
        ((JTextArea) entryBoxes.get(4)).setText(companyJobPosting.getTagsString());
    }

    private void disableDefaultFields() {
        for (int i = 0; i < 5; i++) {
            this.entryBoxes.get(i).setEnabled(false);
            if (this.entryBoxes instanceof JTextComponent) {
                ((JTextComponent) this.entryBoxes.get(i)).setEditable(false);
            }
            // TODO disable documents and tags
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
        int[] todayComponents = hrBackend.getTodayComponents();
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

    private JScrollPane createSelectionBox(String[] recommended, JTextArea textInput, String instructions) {
        JPanel recommendedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String checkedInput = "";
        for (String label : recommended) {
            JCheckBox checkBox = new JCheckBox(label);
            checkedInput = this.addCheckBoxItemListener(checkBox);
            recommendedPanel.add(checkBox);
        }
        JScrollPane labelPane = new JScrollPane(recommendedPanel);
        labelPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        labelPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.setSelectionBoxTextArea(checkedInput, textInput, instructions);
        return labelPane;
    }

    private void setSelectionBoxTextArea(String checkedInput, JTextArea textInput, String instructions) {
        String fullTextInput = checkedInput;
        if (fullTextInput.startsWith(";")) {
            fullTextInput = fullTextInput.substring(1);
        }
        if (!textInput.getText().equals(instructions)) {
            if (!fullTextInput.endsWith(";")) {
                fullTextInput += ";";
            }
            fullTextInput += textInput.getText();
            if (fullTextInput.endsWith(";")) {
                fullTextInput = fullTextInput.substring(0, fullTextInput.length() - 1);
            }
        }
        this.entryBoxes.add(new JTextArea(fullTextInput));
    }

    private String addCheckBoxItemListener(JCheckBox checkBox) {
        StringBuilder checkedInput = new StringBuilder();
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
        return checkedInput.toString();
    }

    private void addSubmitButton(Rectangle rect) {
        JButton submitButton = this.createSubmitButton();
        rect.x = RECTANGLE_X_CENTER;
        rect.y += STANDARD_RECTANGLE_Y_INCREMENT;
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
                    if (!defaultFields.isPresent()) {
                        CompanyJobPosting companyJobPosting = companyJPMap.get(((JTextField) entryBoxes.get(0)).getText());
                        hrBackend.implementJobPosting(companyJobPosting, mandatoryFields);
                    } else {
                        hrBackend.addJobPosting(mandatoryFields, defaultFields.get());
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
        String jobTitleText = ((JTextComponent) ((JComboBox) entryBoxes.get(0)).getEditor().getEditorComponent()).getText();
        System.out.println(jobTitleText);
        if (this.companyJPMap.containsKey(jobTitleText)) {
            return Optional.empty();
        }
        return Optional.of(new String[]{jobTitleText,  // title
                ((JTextField) entryBoxes.get(1)).getText(),  // field
                ((JTextArea) entryBoxes.get(2)).getText(),   // description
                ((JTextArea) entryBoxes.get(3)).getText(),   // documents
                ((JTextArea) entryBoxes.get(4)).getText(),   // tags
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
        } else if (((LocalDate) mandatoryFields[2]).isBefore(((LocalDate) mandatoryFields[1]))) {
            valid = false;
        }

        return valid;
    }
}

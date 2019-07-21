package GUIClasses.HRInterface;

import GUIClasses.MethodsTheGUICallsInHR;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HRAddPosting extends HRPanel {

    JPanel containerPane = this;
    List<JComponent> entryBoxes = new ArrayList();

    HRAddPosting(Container contentPane, MethodsTheGUICallsInHR HRInterface, LocalDate today) {
        super(contentPane, HRInterface, today);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(1, 0, 1, 0);

        this.addAllLabels(c);
        this.addAllFields(c);
        this.addButtons(c);
    }

    private void addLabelToPanel(JComponent component, GridBagConstraints c) {
        c.gridy++;
        this.add(component, c);
    }

    private void addFieldToPanel (JComponent component, GridBagConstraints c) {
        addLabelToPanel(component, c);
        this.entryBoxes.add(component);
    }

    private void addAllLabels (GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = -1;
        JLabel jobTitle = new JLabel("Job title");
        this.addLabelToPanel(jobTitle, c);
        JLabel jobField = new JLabel("Job field");
        this.addLabelToPanel(jobField, c);
        JLabel jobDescription = new JLabel("Job description");
        this.addLabelToPanel(jobDescription, c);
        JLabel requirements = new JLabel("Requirements");
        this.addLabelToPanel(requirements, c);
        JLabel numPositions = new JLabel("Number of positions");
        this.addLabelToPanel(numPositions, c);
        JLabel closeDate = new JLabel("Close date");
        this.addLabelToPanel(closeDate, c);
    }

    private void addAllFields (GridBagConstraints c) {
        c.gridx = 1;
        c.gridy = -1;
        JTextField jobTitleInput = new JTextField(30);
        this.addFieldToPanel(jobTitleInput, c);
        JTextField jobFieldInput = new JTextField(30);
        this.addFieldToPanel(jobFieldInput, c);
        JTextArea jobDescriptionInput = new JTextArea(4, 30);
        this.addFieldToPanel(jobDescriptionInput, c);
        JTextArea requirementsInput = new JTextArea(4, 30);
        this.addFieldToPanel(requirementsInput, c);
        this.addDatePicker(c);
        this.addNumOfPositionField(c);
    }

    private void addDatePicker(GridBagConstraints c) {
        UtilDateModel dateModel = new UtilDateModel();
        dateModel.setDate(today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth());
        dateModel.setSelected(true);
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        JDatePickerImpl closeDateInput = new JDatePickerImpl(datePanel);
        this.addFieldToPanel(closeDateInput, c);
    }

    private void addNumOfPositionField (GridBagConstraints c) {
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        JFormattedTextField numPositionsInput = new JFormattedTextField(formatter);
        numPositionsInput.setValue(1);
        numPositionsInput.setColumns(30);
        this.addFieldToPanel(numPositionsInput, c);
    }

    private JButton createSubmitButton() {
        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] postingFields = new Object[]{((JTextField)entryBoxes.get(0)).getText(),
                        ((JTextField)entryBoxes.get(1)).getText(),
                        ((JTextArea)entryBoxes.get(2)).getText(),
                        ((JTextArea)entryBoxes.get(3)).getText(),
                        ((JFormattedTextField)entryBoxes.get(4)).getValue(),
                        ((Date)((JDatePanelImpl)entryBoxes.get(5)).getModel()
                                .getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                };
                if (isValidInput(postingFields)) {
                    HRInterface.addJobPosting(today, postingFields);
                } else {
                    JOptionPane.showMessageDialog(containerPane, "One or more fields have illegal input.");
                }
            }
        });
        return submit;
    }

    private void addButtons (GridBagConstraints c) {
        JPanel buttons = new JPanel(new FlowLayout());
        JButton submit = this.createSubmitButton();

        buttons.add(submit);
        buttons.add(this.homeButton);

        this.addLabelToPanel(buttons, c);
    }

    private boolean isValidInput(Object[] fields) {
        boolean valid = true;

        int i = 0;
        while (valid && i < 4) {
            if (fields[i].equals("")) {
                valid = false;
            }
            i++;
        }

        if (((LocalDate) fields[5]).isBefore(this.today)) {
            valid = false;
        }

        return valid;
    }
}

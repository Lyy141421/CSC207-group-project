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
    GridBagConstraints c;
    List<JComponent> entryBoxes = new ArrayList();

    HRAddPosting(Container contentPane, MethodsTheGUICallsInHR HRInterface, LocalDate today) {
        super(contentPane, HRInterface, today);

        this.setLayout(new GridBagLayout());
        this.c = new GridBagConstraints();
        this.c.insets = new Insets(1, 0, 1, 0);

        this.addAllLabels();
        this.addAllFields();
        this.addButtons();
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
        JLabel requirements = new JLabel("Requirements");
        this.addLabelToPanel(requirements);
        JLabel numPositions = new JLabel("Number of positions");
        this.addLabelToPanel(numPositions);
        JLabel closeDate = new JLabel("Close date");
        this.addLabelToPanel(closeDate);
    }

    private void addAllFields () {
        this.c.gridx = 1;
        this.c.gridy = -1;
        JTextField jobTitleInput = new JTextField(30);
        this.addFieldToPanel(jobTitleInput);
        JTextField jobFieldInput = new JTextField(30);
        this.addFieldToPanel(jobFieldInput);
        JTextArea jobDescriptionInput = new JTextArea(4, 30);
        this.addFieldToPanel(jobDescriptionInput);
        JTextArea requirementsInput = new JTextArea(4, 30);
        this.addFieldToPanel(requirementsInput);
        this.addDatePicker();
        this.addNumOfPositionField();
    }

    private void addDatePicker() {
        UtilDateModel dateModel = new UtilDateModel();
        dateModel.setDate(today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth());
        dateModel.setSelected(true);
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        JDatePickerImpl closeDateInput = new JDatePickerImpl(datePanel);
        this.addFieldToPanel(closeDateInput);
    }

    private void addNumOfPositionField () {
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        JFormattedTextField numPositionsInput = new JFormattedTextField(formatter);
        numPositionsInput.setValue(1);
        numPositionsInput.setColumns(30);
        this.addFieldToPanel(numPositionsInput);
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
                    JOptionPane.showMessageDialog(containerPane, "Job posting has been added.");
                } else {
                    JOptionPane.showMessageDialog(containerPane, "One or more fields have illegal input.");
                }
            }
        });
        return submit;
    }

    private void addButtons () {
        JPanel buttons = new JPanel(new FlowLayout());
        JButton submit = this.createSubmitButton();

        buttons.add(submit);
        buttons.add(this.homeButton);

        this.addLabelToPanel(buttons);
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

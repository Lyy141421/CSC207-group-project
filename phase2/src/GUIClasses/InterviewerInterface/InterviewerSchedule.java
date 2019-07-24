package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.MethodsTheGUICallsInInterviewer;
import Miscellaneous.InterviewTime;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class InterviewerSchedule extends InterviewerPanel {

    private Container contentPane;

    private ArrayList<JComponent> entryBox = new ArrayList<>();


    InterviewerSchedule(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today){
        super(contentPane, interviewerInterface, today);
        this.interviews = getApplicantToInterviewMap(interviewerInterface.getInterviewsThatNeedScheduling());

        this.setLayout(new BorderLayout());
        this.setInterviewList();
        this.createSetTimePanel();

        this.add(this.interviewList, BorderLayout.WEST);
    }

    void reload() {
        this.interviewList.setListData(interviews.keySet().toArray(new String[interviews.size()]));
    }

    private void setInterviewList() {
        this.interviewList = new JList<>();
        this.interviewList.setListData(interviews.keySet().toArray(new String[interviews.size()]));
        this.interviewList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.interviewList.setLayoutOrientation(JList.VERTICAL);
    }

    private void createSetTimePanel() {
        JPanel setTime = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JComboBox<String> timeSlot = new JComboBox<>(InterviewTime.timeSlots.toArray(new String[InterviewTime.timeSlots.size()]));
        UtilDateModel dateModel = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        JDatePickerImpl interviewDate = new JDatePickerImpl(datePanel);
        this.entryBox.add(timeSlot);
        this.entryBox.add(interviewDate);

        setTime.add(interviewDate);
        setTime.add(timeSlot);
        setTime.add(createScheduleButton());

        this.add(setTime, BorderLayout.CENTER);
    }

    private JButton createScheduleButton() {
        JButton schedule = new JButton("Schedule");
        schedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = ((Date) ((JDatePanelImpl) entryBox.get(1)).getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int selectedIndex = interviewList.getSelectedIndex();
                Interview interview = interviews.get(selectedIndex);
                if (date.isAfter(today)) {
                    boolean canSchedule = interviewerInterface.scheduleInterview(interview, date, ((JComboBox) entryBox.get(0)).getSelectedIndex());
                    if (canSchedule) {
                        interviews.remove(selectedIndex);
                        //TODO: update InterviewerViewComplete
                        scheduleInterviews.remove(selectedIndex);
                        futureInterviews.add(interview);
                        incompleteTitles.addElement(getInterviewTitles(new ArrayList<Interview>(Arrays.asList(interview))).get(0));
                        JOptionPane.showMessageDialog(contentPane, "You have successfully scheduled an interview.");
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Unable to book the interview at the selected date and time.");
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Please choose a date after today.");
                }
            }
        });

        return schedule;
    }
}

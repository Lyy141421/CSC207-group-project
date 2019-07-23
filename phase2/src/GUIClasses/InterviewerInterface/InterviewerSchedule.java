package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.MethodsTheGUICallsInInterviewer;
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

    private JList<String> interviewToScheduleList;

    InterviewerSchedule(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today){
        super(contentPane, interviewerInterface, today);

        this.setLayout(new BorderLayout());
        JPanel setTime = new JPanel(new FlowLayout(FlowLayout.CENTER));

        this.interviewToScheduleList = new JList<>(getIdAndApplicants(interviewsToBeScheduled));
        this.interviewToScheduleList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.interviewToScheduleList.setLayoutOrientation(JList.VERTICAL);
        JComboBox<String> timeSlot = new JComboBox<>(new String[]{"9-10 am", "10-11 am", "1-2 pm", "2-3 pm", "3-4 pm",
                "4-5 pm"});
        UtilDateModel dateModel = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        JDatePickerImpl interviewDate = new JDatePickerImpl(datePanel);
        JButton schedule = new JButton("Confirm");
        schedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = ((Date) interviewDate.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int selectedIndex = interviewList.getSelectedIndex();
                Interview interview = interviewsToBeScheduled.get(selectedIndex);
                if (date.isAfter(today)) {
                    boolean canSchedule = interviewerInterface.scheduleInterview(interview, date, timeSlot.getSelectedIndex());
                    if (canSchedule) {
                        interviewsToBeScheduled.remove(selectedIndex);
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
        JButton home = new JButton("Home");
        //TODO: home action listener

        setTime.add(interviewDate);
        setTime.add(timeSlot);
        setTime.add(schedule);

        this.add(this.interviewToScheduleList, BorderLayout.WEST);
        this.add(setTime, BorderLayout.CENTER);
        this.add(home, BorderLayout.SOUTH);

    }
}

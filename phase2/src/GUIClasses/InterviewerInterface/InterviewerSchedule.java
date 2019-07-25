package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.CommonUserGUI.FrequentlyUsedMethods;
import GUIClasses.MethodsTheGUICallsInInterviewer;
import Miscellaneous.InterviewTime;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class InterviewerSchedule extends InterviewerPanel {

    private static String PROMPT = "PROMPT";
    private static String SET_TIME = "SET_TIME";

    private JSplitPane splitDisplay;
    private JPanel scheduleDateTimeCards;
    private JPanel scheduleDateTimePanel = new JPanel();
    private JDatePanelImpl datePanel;
    private JButton selectDateButton;
    private JComboBox<String> selectTimeBox;

    private LocalDate dateSelected;
    private Interview interviewSelected;


    InterviewerSchedule(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
        this.interviews = getApplicantToInterviewMap(interviewerInterface.getInterviewsThatNeedScheduling());

        this.setLayout(new BorderLayout());
        JPanel title = new FrequentlyUsedMethods().createTitlePanel("Schedule Interviews", 20);
        this.add(title, BorderLayout.PAGE_START);
        splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(250);
        this.setInterviewList(splitDisplay);
        this.setScheduleTimeCards();
        this.setListSelectionListener();
        this.add(splitDisplay, BorderLayout.CENTER);

        this.setScheduleTimePanel();
    }

    void reload() {
        this.interviewList.setListData(interviews.keySet().toArray(new String[interviews.size()]));
    }

    private void setInterviewList(JSplitPane splitDisplay) {
        this.interviewList = new JList<>();
        this.interviewList.setListData(interviews.keySet().toArray(new String[interviews.size()]));
        this.interviewList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.interviewList.setLayoutOrientation(JList.VERTICAL);
        splitDisplay.setLeftComponent(this.interviewList);
    }

    /**
     * Set the list selection listener for choosing the referee.
     */
    private void setListSelectionListener() {
        this.interviewList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (interviewList.getSelectedIndex() != -1) {
                    int selectedIndex = interviewList.getSelectedIndex();
                    interviewSelected = interviews.get(selectedIndex);
                    setScheduleTimePanel();
                    ((CardLayout) scheduleDateTimeCards.getLayout()).show(scheduleDateTimeCards, SET_TIME);
                } else {
                    ((CardLayout) scheduleDateTimeCards.getLayout()).show(scheduleDateTimeCards, PROMPT);
                }
            }
        });
    }

    /**
     * Create the prompt card that is defaulted to show on the right side of the split pane.
     *
     * @return the prompt panel created.
     */
    private JPanel createPromptCard() {
        JPanel promptCard = new JPanel();
        promptCard.setLayout(new BorderLayout());
        JLabel promptText = new JLabel("Select an interview to schedule");
        promptText.setFont(new Font("Century Gothic", Font.BOLD, 15));
        promptText.setHorizontalAlignment(SwingConstants.CENTER);
        promptCard.add(promptText, BorderLayout.CENTER);
        return promptCard;
    }

    private void setScheduleTimeCards() {
        scheduleDateTimeCards = new JPanel(new CardLayout());
        scheduleDateTimeCards.add(this.createPromptCard(), PROMPT);
        scheduleDateTimeCards.add(this.scheduleDateTimePanel, SET_TIME);

    }

    private void setScheduleTimePanel() {
        scheduleDateTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        UtilDateModel dateModel = new UtilDateModel();
        datePanel = new JDatePanelImpl(dateModel);
        JDatePickerImpl interviewDate = new JDatePickerImpl(datePanel);
        int[] tomorrow = this.interviewerInterface.getTomorrow();
        dateModel.setDate(tomorrow[0], tomorrow[1], tomorrow[2]);
        scheduleDateTimePanel.add(interviewDate);
        scheduleDateTimePanel.add(this.createDateButtonPanel());

        splitDisplay.setRightComponent(scheduleDateTimePanel);
    }

    private JPanel createDateButtonPanel() {
        selectDateButton = new JButton("Select date");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectDateButton);
        return buttonPanel;
    }

    private void setSelectTimeBox(LocalDate date) {
        selectTimeBox = new JComboBox<>(this.interviewerInterface.getAvailableTimes(date));
        scheduleDateTimePanel.add(selectTimeBox);
    }

    private void setScheduleDateActionListener() {
        selectDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateSelected = ((Date) ((JDatePanelImpl) datePanel).getModel().getValue()).toInstant().
                        atZone(ZoneId.systemDefault()).toLocalDate();
                if (interviewerInterface.isAfterToday(dateSelected)) {
                    setSelectTimeBox(dateSelected);
                    scheduleDateTimePanel.add(createScheduleButton());
                } else {
                    JOptionPane.showMessageDialog(scheduleDateTimePanel, "Please choose a date after today.");
                    // Check if this works
                    // May need the content pane
                }
            }
        });
    }


    private JButton createScheduleButton() {
        JButton schedule = new JButton("Schedule");
        schedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String timeSlotString = selectTimeBox.getItemAt(selectTimeBox.getSelectedIndex());
                boolean canSchedule = interviewerInterface.scheduleInterview(interviewSelected, dateSelected,
                        timeSlotString);
                if (canSchedule) {
                    JOptionPane.showMessageDialog(scheduleDateTimePanel, "You have successfully scheduled an interview.");
                } else {
                    JOptionPane.showMessageDialog(scheduleDateTimePanel, "You are unavailable at this date and time.");
                    refresh();
                }
            }
        });
        return schedule;
    }

    private void refresh() {

    }
}

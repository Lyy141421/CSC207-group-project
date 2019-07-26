package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.CommonUserGUI.FrequentlyUsedMethods;
import GUIClasses.CommonUserGUI.UserPanel;
import GUIClasses.MethodsTheGUICallsInInterviewer;
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

class InterviewerSchedule extends InterviewerPanel {

    private static String PROMPT = "PROMPT";
    private static String SET_DATETIME = "SET_DATETIME";

    private JSplitPane splitDisplay;
    private JPanel scheduleDateTimeCards;
    private JPanel scheduleDateTimePanel = new JPanel();
    private JDatePickerImpl datePicker;
    private JButton selectDateButton;
    private JComboBox<String> selectTimeBox;

    private LocalDate dateSelected;
    private Interview interviewSelected;


    InterviewerSchedule(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
        this.interviews = getTitleToInterviewMap(interviewerInterface.getInterviewsThatNeedScheduling());

        this.setLayout(new BorderLayout());
        JPanel title = new FrequentlyUsedMethods().createTitlePanel("Schedule Interviews", 20);
        this.add(title, BorderLayout.PAGE_START);
        splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(200);
        this.setInterviewList(splitDisplay);
        this.setInterviewListSelectionListener();
        this.setScheduleDateTimeCards();
        this.add(splitDisplay, BorderLayout.CENTER);
    }

    private void setInterviewList(JSplitPane splitDisplay) {
        super.setInterviewList();
        splitDisplay.setLeftComponent(this.interviewList);
    }

    /**
     * Set the list selection listener for choosing the interview.
     */
    private void setInterviewListSelectionListener() {
        this.interviewList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!interviewList.getValueIsAdjusting() && interviewList.getSelectedIndex() != -1) {
                    String selectedTitle = interviewList.getSelectedValue();
                    interviewSelected = interviews.get(selectedTitle);
                    ((CardLayout) scheduleDateTimeCards.getLayout()).show(scheduleDateTimeCards, SET_DATETIME);
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

    private void setScheduleDateTimeCards() {
        scheduleDateTimeCards = new JPanel(new CardLayout());
        scheduleDateTimeCards.add(this.createPromptCard(), PROMPT);
        scheduleDateTimePanel.add(createSelectDatePanel());
        scheduleDateTimeCards.add(this.scheduleDateTimePanel, SET_DATETIME);
        splitDisplay.setRightComponent(scheduleDateTimeCards);
    }

    private JPanel createSelectDatePanel() {
        JPanel selectDatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // may want to change to border layout
        int[] tomorrow = this.interviewerInterface.getTomorrow();
        UtilDateModel dateModel = new UtilDateModel();
        dateModel.setDate(tomorrow[0], tomorrow[1] - 1, tomorrow[2]);
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        datePicker = new JDatePickerImpl(datePanel);
        selectDatePanel.add(datePicker);
        selectDatePanel.add(this.createDateButtonPanel());
        return selectDatePanel;
    }

    private JPanel createDateButtonPanel() {
        selectDateButton = new JButton("Select date");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectDateButton);
        this.setScheduleDateActionListener();
        return buttonPanel;
    }

    private JPanel createSelectTimePanel(LocalDate date) {
        JPanel selectTimePanel = new JPanel();
        selectTimeBox = new JComboBox<>(this.interviewerInterface.getAvailableTimes(date));
        selectTimePanel.add(selectTimeBox);
        return selectTimePanel;
    }

    private void setScheduleDateActionListener() {
        selectDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int year = datePicker.getModel().getYear();
                int month = datePicker.getModel().getMonth() + 1;
                int day = datePicker.getModel().getDay() + 1;
                dateSelected = LocalDate.of(year, month, day);
                if (interviewerInterface.dateSelectedIsAfterToday(dateSelected)) {
                    if (interviewerInterface.getAvailableTimes(dateSelected).length == 0) {
                        JOptionPane.showMessageDialog(scheduleDateTimePanel, "You have no time slots available on this date.");
                    } else {
                        scheduleDateTimePanel.add(createSelectTimePanel(dateSelected), BorderLayout.CENTER);
                        scheduleDateTimePanel.add(createScheduleButtonPanel(), BorderLayout.AFTER_LAST_LINE);
                        scheduleDateTimePanel.revalidate();
                    }
                } else {
                    JOptionPane.showMessageDialog(scheduleDateTimePanel, "Please choose a date after today.");
                }
            }
        });
    }


    private JPanel createScheduleButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(this.createScheduleButton());
        return buttonPanel;
    }

    private JButton createScheduleButton() {
        JButton scheduleButton = new JButton("Schedule");
        scheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String timeSlotString = selectTimeBox.getItemAt(selectTimeBox.getSelectedIndex());
                boolean canSchedule = interviewerInterface.scheduleInterview(interviewSelected, dateSelected,
                        timeSlotString);
                if (canSchedule) {
                    JOptionPane.showMessageDialog(scheduleDateTimePanel, "You have successfully scheduled an interview.");
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(scheduleDateTimePanel, "You are unavailable at this date and time.");
                }
            }
        });
        return scheduleButton;
    }

    private void refresh() {
        ((UserPanel) this.getParent().getParent()).resetCards();

//        this.interviews.remove(this.interviewList.getSelectedValue());
//        this.interviewList.remove(this.interviewList.getSelectedIndex());
//        this.setInterviewList();
//        ((CardLayout) scheduleDateTimeCards.getLayout()).show(scheduleDateTimeCards, PROMPT);
    }
}

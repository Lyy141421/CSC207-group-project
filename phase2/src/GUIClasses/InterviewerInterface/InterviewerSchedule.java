package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.CommonUserGUI.GUIElementsCreator;
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
    /**
     * Panel for scheduling interviews.
     */

    // === Static variables ===
    // Card layout keys
    private static String PROMPT = "PROMPT";
    private static String SET_DATETIME = "SET_DATETIME";

    // === Instance variables ===
    private JPanel scheduleDateTimeCards;   // Cards that are used for scheduling the interview date and time
    private JPanel scheduleDateTimePanel = new JPanel();    // The full panel being displayed for scheduling
    private GridBagConstraints c = new GridBagConstraints();    // The gird bag constraints for layout purposes
    private JDatePickerImpl datePicker; // The date picker used to select the date
    private JButton selectDateButton;   // The button for confirming a date selection
    private JComboBox<String> selectTimeBox;    // The drop-down box for selecting the time slots

    private LocalDate dateSelected; // The date selected by the interviewer
    private Interview interviewSelected;    // The interview selected by the interviewer

    // === Constructor ===
    InterviewerSchedule(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
        this.interviews = getTitleToInterviewMap(interviewerInterface.getInterviewsThatNeedScheduling());

        this.setLayout(new BorderLayout());
        JPanel title = new GUIElementsCreator().createTitlePanel("Schedule Interviews", 20);
        this.add(title, BorderLayout.PAGE_START);
        splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(200);
        this.setInterviewList();
        this.setInterviewListSelectionListener();
        this.setScheduleDateTimeCards();
        this.add(splitDisplay, BorderLayout.CENTER);
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

    /**
     * Set the scheduling cards.
     */
    private void setScheduleDateTimeCards() {
        scheduleDateTimeCards = new JPanel(new CardLayout());
        scheduleDateTimeCards.add(this.createPromptCard(), PROMPT);
        this.setSelectDatePanel();
        scheduleDateTimeCards.add(this.scheduleDateTimePanel, SET_DATETIME);
        splitDisplay.setRightComponent(scheduleDateTimeCards);
    }

    /**
     * Set the contents of the panel for selecting the date.
     */
    private void setSelectDatePanel() {
        scheduleDateTimePanel.setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        JPanel selectDatePrompt = new GUIElementsCreator().createTitlePanel("Select a date", 15);
        scheduleDateTimePanel.add(selectDatePrompt, c);
        c.gridx++;
        scheduleDateTimePanel.add(createSelectDatePanel(), c);
        c.gridx++;
        scheduleDateTimePanel.add(createDateButtonPanel(), c);
        scheduleDateTimePanel.revalidate();
    }

    /**
     * Create the full panel for selecting the date.
     *
     * @return the panel created.
     */
    private JPanel createSelectDatePanel() {
        JPanel selectDatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        int[] tomorrow = this.interviewerInterface.getTomorrow();
        UtilDateModel dateModel = new UtilDateModel();
        dateModel.setDate(tomorrow[0], tomorrow[1] - 1, tomorrow[2]);
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        datePicker = new JDatePickerImpl(datePanel);
        selectDatePanel.add(datePicker);
        return selectDatePanel;
    }

    /**
     * Create the panel with the select date button.
     * @return the panel created.
     */
    private JPanel createDateButtonPanel() {
        selectDateButton = new JButton("Select date");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectDateButton);
        this.setScheduleDateActionListener();
        return buttonPanel;
    }

    /**
     * Create the full panel for selecting the time slot.
     * @return the panel created.
     */
    private JPanel createSelectTimePanel(LocalDate date) {
        JPanel selectTimePanel = new JPanel(new BorderLayout());
        selectTimeBox = new JComboBox<>(this.interviewerInterface.getAvailableTimes(date));
        selectTimePanel.add(selectTimeBox, BorderLayout.CENTER);
        return selectTimePanel;
    }

    /**
     * Add the panel for selecting the time to the full scheduling panel.
     */
    private void addSelectTimePanel() {
        c.gridx = 0;
        c.gridy++;
        c.fill = GridBagConstraints.CENTER;
        JPanel selectTimePrompt = new GUIElementsCreator().createTitlePanel("Select a time slot", 15);
        scheduleDateTimePanel.add(selectTimePrompt, c);
        c.gridx++;
        scheduleDateTimePanel.add(createSelectTimePanel(dateSelected), c);
        c.gridx++;
        scheduleDateTimePanel.add(createScheduleButtonPanel(), c);
        scheduleDateTimePanel.revalidate();
    }

    /**
     * Set the action listener for selecting the date.
     */
    private void setScheduleDateActionListener() {
        selectDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int year = datePicker.getModel().getYear();
                int month = datePicker.getModel().getMonth() + 1;
                int day = datePicker.getModel().getDay();
                dateSelected = LocalDate.of(year, month, day);
                if (interviewerInterface.dateSelectedIsAfterToday(dateSelected)) {
                    if (interviewerInterface.getAvailableTimes(dateSelected).length == 0) {
                        JOptionPane.showMessageDialog(scheduleDateTimePanel, "You have no time slots available on this date.");
                    } else {
                        selectDateButton.setEnabled(false);
                        addSelectTimePanel();
                    }
                } else {
                    JOptionPane.showMessageDialog(scheduleDateTimePanel, "Please choose a date after today.");
                }
            }
        });
    }


    /**
     * Create a panel with the schedule button.
     * @return the panel created.
     */
    private JPanel createScheduleButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(this.createScheduleButton());
        return buttonPanel;
    }

    /**
     * Create the schedule button.
     * @return the schedule button created.
     */
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

}

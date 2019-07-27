package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.CommonUserGUI.TitleCreator;
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
    private GridBagConstraints c = new GridBagConstraints();
    private JDatePickerImpl datePicker;
    private JButton selectDateButton;
    private JComboBox<String> selectTimeBox;

    private LocalDate dateSelected;
    private Interview interviewSelected;


    InterviewerSchedule(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
        this.interviews = getTitleToInterviewMap(interviewerInterface.getInterviewsThatNeedScheduling());

        this.setLayout(new BorderLayout());
        JPanel title = new TitleCreator().createTitlePanel("Schedule Interviews", 20);
        this.add(title, BorderLayout.PAGE_START);
        splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(200);
        this.setInterviewList(splitDisplay);
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

    private void setScheduleDateTimeCards() {
        scheduleDateTimeCards = new JPanel(new CardLayout());
        scheduleDateTimeCards.add(this.createPromptCard(), PROMPT);
        this.setSelectDatePanel();
        scheduleDateTimeCards.add(this.scheduleDateTimePanel, SET_DATETIME);
        splitDisplay.setRightComponent(scheduleDateTimeCards);
    }

    private void setSelectDatePanel() {
        scheduleDateTimePanel.setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        JPanel selectDatePrompt = new TitleCreator().createTitlePanel("Select a date", 15);
        scheduleDateTimePanel.add(selectDatePrompt, c);
        c.gridx++;
        scheduleDateTimePanel.add(createSelectDatePanel(), c);
        c.gridx++;
        scheduleDateTimePanel.add(createDateButtonPanel(), c);
        scheduleDateTimePanel.revalidate();
    }

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

    private JPanel createDateButtonPanel() {
        selectDateButton = new JButton("Select date");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectDateButton);
        this.setScheduleDateActionListener();
        return buttonPanel;
    }

    private JPanel createSelectTimePanel(LocalDate date) {
        JPanel selectTimePanel = new JPanel(new BorderLayout());
        selectTimeBox = new JComboBox<>(this.interviewerInterface.getAvailableTimes(date));
        selectTimePanel.add(selectTimeBox, BorderLayout.CENTER);
        return selectTimePanel;
    }

    private void addSelectTimePanel() {
        c.gridx = 0;
        c.gridy++;
        c.fill = GridBagConstraints.CENTER;
        JPanel selectTimePrompt = new TitleCreator().createTitlePanel("Select a time slot", 15);
        scheduleDateTimePanel.add(selectTimePrompt, c);
        c.gridx++;
        scheduleDateTimePanel.add(createSelectTimePanel(dateSelected), c);
        c.gridx++;
        scheduleDateTimePanel.add(createScheduleButtonPanel(), c);
        scheduleDateTimePanel.revalidate();
    }

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

}

package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class InterviewerViewComplete extends InterviewerView {

    private JTextArea notes;

    InterviewerViewComplete(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today) {
        super(contentPane, interviewerInterface, today);

        this.createNoteTab();
    }

    @Override
    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getIncompleteInterviews(today));
    }

    //TODO: save notes, advance/fail

    private void createNoteTab() {
        this.notes = new JTextArea("Please enter interview notes");
        this.infoPane.addTab("Notes", new JScrollPane(notes));
    }
/*
        this.pastInterviews = getTitleToInterviewMap(interviewerInterface.getIncompleteInterviews(today));

        this.setLayout(new BorderLayout());
        JPanel select = new JPanel();
        select.setLayout(new BoxLayout(select, BoxLayout.Y_AXIS));
        JPanel passOrFailButtons = new JPanel();
        passOrFailButtons.setLayout(new BoxLayout(passOrFailButtons, BoxLayout.Y_AXIS));
        JPanel buttons = new JPanel(new FlowLayout());

        this.interviewList = new JList();
        JRadioButton incomplete = new JRadioButton("Incomplete", true);
        JRadioButton complete = new JRadioButton("Complete", false);
        ButtonGroup showContent = new ButtonGroup();
        showContent.add(incomplete);
        showContent.add(complete);
        JList<String> viewable = new JList<>(new String[]{"Overview", "Notes", "CV", "Cover letter"});  // TODO replace with document viewer

        select.add(incomplete);
        select.add(complete);
        select.add(new JScrollPane(viewable));

        JTextArea info = new JTextArea("Information");
        info.setEditable(false);
        // Phase 2?
        // JButton reschedule = new JButton("Re-schedule");
        ButtonGroup passOrFail = new ButtonGroup();
        JRadioButton advance = new JRadioButton("Advance");
        advance.setEnabled(false);
        advance.setSelected(true);
        JRadioButton fail = new JRadioButton("Fail");
        fail.setEnabled(false);
        passOrFail.add(advance);
        passOrFail.add(fail);
        passOrFailButtons.add(advance);
        passOrFailButtons.add(fail);
        JButton submit = new JButton("Submit results");
        submit.setEnabled(false);
        JButton home = new JButton("Home");
        //TODO: Home button action listener;
        buttons.add(passOrFailButtons);
        buttons.add(submit);
        buttons.add(home);

        JSplitPane display = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, select, new JScrollPane(info));
        display.setDividerLocation(250);

        viewable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        viewable.setLayoutOrientation(JList.VERTICAL);

        incomplete.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    interviews.setModel(incompleteTitles);
                    currList = futureInterviews;
                    advance.setEnabled(false);
                    fail.setEnabled(false);
                    submit.setEnabled(false);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    interviews.setModel(completeTitles);
                    currList = pastInterviews;
                    advance.setEnabled(true);
                    fail.setEnabled(true);
                    submit.setEnabled(true);
                }
            }
        });

        interviews.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setInfo(currList.get(interviews.getSelectedIndex()), viewable.getSelectedIndex(), info);
                    // Phase2: "you have unsaved changes. Would you like to proceed?"
                    temporaryNotes = "";
                }
            }
        });

        viewable.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (info.isEditable()) {
                    temporaryNotes = info.getText();
                }
                setInfo(currList.get(interviews.getSelectedIndex()), e.getFirstIndex(), info);
            }
        });

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = interviews.getSelectedIndex();
                Interview interview = currList.get(selectedIndex);
                if (viewable.getSelectedIndex() == 1) {
                    interviewerInterface.storeInterviewNotes(interview, interviewerInterface.getInterviewer(), info.getText());
                } else {
                    interviewerInterface.storeInterviewNotes(interview, interviewerInterface.getInterviewer(), temporaryNotes);
                }
                interviewerInterface.setInterviewResult(interview, advance.isSelected());   // TODO job application is a parameter

                // Clear temporarily stored notes
                temporaryNotes = "";
                // Remove interview from list
                currList.remove(interview);
                ((DefaultComboBoxModel<String>) interviews.getModel()).removeElementAt(selectedIndex);
            }
        });

        this.add(this.interviewList, BorderLayout.NORTH);
        this.add(display, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);
    }

    private

    private void setInfo(Interview interview, int attributeIndex, JTextArea info) {
        switch (attributeIndex) {
            case 0:
                info.setText(interview.getOverview());      //TODO rewrite methods?
                break;
            case 1:
                info.setText(interview.getInterviewNotes());    // TODO get this interviewer's notes
                if (interview.isComplete()) {
                    info.setEditable(true);
                } else {
                    info.setEditable(false);
                }
                break;
            case 2:
                // TODO must modify for job application to result hash map
                // TODO must modify for document viewer
                info.setText(interview.getJobApplication().getCV().getContents());
                break;
            case 3:
                // TODO must modify for job application to result hash map
                // TODO must modify for document viewer
                info.setText(interview.getJobApplication().getCoverLetter().getContents());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + attributeIndex);
        }
    }*/
}

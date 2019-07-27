package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;

import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

class InterviewerViewAndWriteNotes extends InterviewerViewOnly {

    // === Instance variables ===
    private JPanel notesPanel = new JPanel();
    private JTextArea notes;

    InterviewerViewAndWriteNotes(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
        this.infoPane.addTab("Write notes", this.notesPanel);
    }

    @Override
    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getIncompleteInterviewsAlreadyOccurredNotCoordinator());
    }

    private void setWriteNotesPanel() {
        if (interviewerInterface.hasAlreadyWrittenNotes(interviewSelected)) {
            JOptionPane.showMessageDialog(notesPanel, "You have already written notes for this interview");
        } else {
            notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));
            this.notes = new JTextArea("Please enter interview notes");
            notesPanel.add(new JScrollPane(notes));
            notesPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            notesPanel.add(this.createSaveNotesButtonPanel());
        }
        notesPanel.revalidate();
    }

    private JPanel createSaveNotesButtonPanel() {
        JPanel saveButtonPanel = new JPanel();
        JButton saveButton = new JButton("Save notes");
        saveButtonPanel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String note = notes.getText();
                interviewerInterface.storeInterviewNotes(interviewSelected, interviewerInterface.getInterviewer(), note);
                JOptionPane.showMessageDialog(notesPanel, "You have successfully written notes for this interview");
                refresh();
            }
        });

        return saveButtonPanel;
    }

    @Override
    void loadTabContents(Interview selectedInterview) {
        super.loadTabContents(selectedInterview);
        notesPanel.removeAll();
        setWriteNotesPanel();
    }
}

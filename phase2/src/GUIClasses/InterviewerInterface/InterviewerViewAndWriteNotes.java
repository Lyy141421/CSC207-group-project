package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;

import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

class InterviewerViewAndWriteNotes extends InterviewerViewOnly {
    /**
     * Panel for viewing and writing notes for an interview.
     */

    // === Instance variables ===
    private JPanel notesPanel = new JPanel();   // The panel for writing notes.
    private JTextArea notes;    // The text area where the notes are written.

    // === Constructor ===
    InterviewerViewAndWriteNotes(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
        this.infoPane.addTab("Write notes", this.notesPanel);
    }

    /**
     * Gets the appropriate interviews to display in the interview list.
     *
     * @return the map of interviews for which this interview can write notes.
     */
    @Override
    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getIncompleteInterviewsAlreadyOccurredNotCoordinator());
    }

    /**
     * Load the contents of the tabs.
     *
     */
    @Override
    void loadTabContents() {
        super.loadTabContents();
        notesPanel.removeAll();
        setWriteNotesPanel();
    }

    /**
     * Set the contents of the panel for writing notes.
     */
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

    /**
     * Create a panel with a save notes button.
     * @return the panel created.
     */
    private JPanel createSaveNotesButtonPanel() {
        JPanel saveButtonPanel = new JPanel();
        JButton saveButton = new JButton("Save notes");
        saveButtonPanel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String note = notes.getText();
                interviewerInterface.storeInterviewNotes(interviewSelected, note);
                JOptionPane.showMessageDialog(notesPanel, "You have successfully written notes for this interview");
                refresh();
            }
        });

        return saveButtonPanel;
    }
}

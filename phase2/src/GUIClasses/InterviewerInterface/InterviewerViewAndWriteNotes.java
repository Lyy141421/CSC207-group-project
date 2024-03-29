package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;

import GUIClasses.CommonUserGUI.GUIElementsCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

class InterviewerViewAndWriteNotes extends InterviewerViewOnly {
    /**
     * Panel for viewing and writing notes for an interview.
     */

    // === Static variables ===
    private static String NOTE_HINT = "Please enter interview notes";

    // === Instance variables ===
    private JPanel notesPanel = new JPanel();   // The panel for writing notes.
    private JScrollPane notes;    // The text area where the notes are written.

    // === Constructor ===
    InterviewerViewAndWriteNotes(InterviewerBackEnd interviewerBackEnd) {
        super(interviewerBackEnd);
        this.infoPane.addTab("Write notes", this.notesPanel);
    }

    /**
     * Gets the appropriate interviews to display in the interview list.
     *
     * @return the map of interviews for which this interview can write notes.
     */
    @Override
    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerBackEnd.getIncompleteInterviewsAlreadyOccurred());
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
        if (interviewerBackEnd.hasAlreadyWrittenNotes(interviewSelected)) {
            notesPanel.setLayout(new BorderLayout());
            JPanel message = new GUIElementsCreator().createLabelPanel("You have already written notes for this interview", 18, false);
            message.setBorder(BorderFactory.createEmptyBorder(100, 20, 20, 20));
            notesPanel.add(message, BorderLayout.CENTER);
        } else {
            notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));
            this.notes = new GUIElementsCreator().createTextAreaWithScrollBar("Please enter interview notes", true);
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
                JTextArea noteArea = (JTextArea) notes.getViewport().getView();
                String note = noteArea.getText();
                if (note==NOTE_HINT) {
                    interviewerBackEnd.storeInterviewNotes(interviewSelected, "");
                } else {
                    interviewerBackEnd.storeInterviewNotes(interviewSelected, note);
                }
                JOptionPane.showMessageDialog(notesPanel, "You have successfully written notes for this interview");
                refresh();
            }
        });

        return saveButtonPanel;
    }
}
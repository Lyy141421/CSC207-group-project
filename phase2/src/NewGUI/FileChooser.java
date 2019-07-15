/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * Adapted from FileChooserDemo.java from https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
 */

package NewGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;


public class FileChooser extends JPanel implements ActionListener {
    static private final String newline = "\n";
    static private final Color LIGHT_RED = new Color(255, 210, 210);
    JButton uploadButton;
    JButton submitButton;
    JPanel removeFileButtonsPanel = new JPanel();
    JFileChooser fc;
    String uploadButtonIconPath = "phase2/images/Open16.gif";
    ArrayList<File> filesToSubmit = new ArrayList<>();

    public FileChooser() {
        super(new BorderLayout());

        //Create a file chooser
        fc = new JFileChooser();

        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        uploadButton = new JButton("Upload",
                new ImageIcon(uploadButtonIconPath));
        uploadButton.addActionListener(this);

        JPanel uploadButtonPanel = new JPanel();
        uploadButtonPanel.add(uploadButton);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitJobApplicationListener(this.filesToSubmit));

        JPanel submitButtonPanel = new JPanel();
        submitButtonPanel.add(submitButton);
        add(submitButtonPanel, BorderLayout.AFTER_LAST_LINE);

        //Add the buttons and the log to this panel.
        add(uploadButtonPanel, BorderLayout.PAGE_START);

        removeFileButtonsPanel.setBackground(Color.WHITE);
        add(removeFileButtonsPanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == uploadButton) {
            int returnVal = fc.showOpenDialog(FileChooser.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                JButton removeFileButton = new JButton("Remove " + file.getName());
                removeFileButton.setBackground(LIGHT_RED);
                removeFileButtonsPanel.add(removeFileButton, BorderLayout.SOUTH);
                removeFileButtonsPanel.validate();
                removeFileButtonsPanel.repaint();
                removeFileButton.addActionListener(
                        new RemoveFileButtonListener(removeFileButtonsPanel, removeFileButton, filesToSubmit, file));
                filesToSubmit.add(file);
            }
        }
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Submit files");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new FileChooser());

        //Display the window.
        //frame.pack();
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}

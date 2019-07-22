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

package NewGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DocumentViewer extends JPanel {

    // === Instance variables ===
    private JList list; // The list of file names that the user can choose from.
    private JButton openButton = new JButton("Open");   // The button which when clicked, will open the selected file
    private File folder;    // The folder in which are files that this user can view.
    private String[] fileNames; // The file names of files that this user can view.
    private File[] files;   // The files that this user can view

    // === Representation Invariant ===
    // The elements in fileNames and files correspond to each other (ie, fileNames[0] is the file name of files[0])

    // === Constructor ===
    public DocumentViewer(File folder) {
        this.fileNames = folder.list();
        this.files = folder.listFiles();

        this.setLayout(new BorderLayout());
        JLabel label = new JLabel("Select a file to open:");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        this.add(label, BorderLayout.NORTH);

        this.createListPanel();
        this.createOpenButtonPanel();
    }

    /**
     * Create the panel with the open button.
     */
    private void createOpenButtonPanel() {
        JPanel openButtonPanel = new JPanel();
        openButton.setEnabled(false);
        openButton.setSize(50, 20);
        openButtonPanel.add(openButton);
        openButtonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(files[list.getSelectedIndex()]);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.add(openButtonPanel, BorderLayout.PAGE_END);
    }

    private void createListPanel() {
        list = new JList(this.fileNames);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(-1);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (list.getSelectedIndex() != -1) {
                    openButton.setEnabled(true);
                }
            }
        });

        JPanel listPanel = new JPanel();
        listPanel.add(new JScrollPane(list));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        this.add(listPanel, BorderLayout.CENTER);
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SplitPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DocumentViewer documentViewer = new DocumentViewer(new File("./uploadedDocuments/companies/Company/Branch/1_title/username"));
        frame.getContentPane().add(documentViewer);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}

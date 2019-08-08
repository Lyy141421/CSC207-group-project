package GUIClasses.CommonUserGUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUIElementsCreator {
    /*
     * A GUI elements creator.
     */

    /**
     * Create a panel with this label
     *
     * @param text     The actual title/message.
     * @param fontSize The size of the title/message.
     * @param isTitle Whether or not this is a panel title.
     * @return the JPanel with this title.
     */
    public JPanel createLabelPanel(String text, int fontSize, boolean isTitle) {
        JLabel title;
        if (isTitle) {
            title = new JLabel(text);
        } else {
            title = new JLabel("<html><p>" + text + "</p></html>");
        }
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(title, BorderLayout.PAGE_START);
        title.setFont(new Font("Century Gothic", Font.BOLD, fontSize));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.revalidate();
        return titlePanel;
    }

    /**
     * Create a panel with a table.
     *
     * @param categoryNames The column names.
     * @param data          The data in the cells.
     * @return the panel created.
     */
    public JPanel createTablePanel(String[] categoryNames, Object[][] data) {
        JPanel tablePanel = new JPanel(new GridLayout(1, 1));

        JTable table = new JTable(data, categoryNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setCellSelectionEnabled(false);
        table.setEnabled(false);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        this.resizeColumnWidth(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(600, 125));
        tablePanel.add(scrollPane);
        return tablePanel;
    }

    /**
     * Resizes the column width for a table.
     *
     * @param table The table to be resized.
     *              Source: https://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths
     */
    private void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > 300)
                width = 300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public JScrollPane createTextAreaWithScrollBar(String text, boolean isEditable) {
        JTextArea textArea = new JTextArea();
        textArea.setText(text);
        if (isEditable) {
            textArea.setForeground(Color.GRAY);
        }
        textArea.setEditable(isEditable);
        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(text) && isEditable) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().equals("") && isEditable) {
                    textArea.setText(text);
                    textArea.setForeground(Color.GRAY);
                }
            }
        });
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(250, 250));
        return areaScrollPane;
    }
}

package GUIClasses.CommonUserGUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class GUIElementsCreator {
    /**
     * A title creator.
     */

    /**
     * Create the title panel.
     *
     * @param text     The actual title.
     * @param fontSize The size of the title.
     * @return the JPanel with this title.
     */
    public JPanel createTitlePanel(String text, int fontSize) {
        JLabel title = new JLabel(text);
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
        scrollPane.setMinimumSize(new Dimension(500, 70));
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
}

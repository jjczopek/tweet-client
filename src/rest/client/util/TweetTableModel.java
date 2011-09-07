package rest.client.util;

import rest.client.beans.Tweet;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for tweets.
 * Provides column names for tweets table.
 */
public class TweetTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Autor", "Treść", "Dodano"};
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public static final double[] PREFERRED_COLUMN_WIDTHS = {.2, .6, .2};

    private List<Tweet> dataList;

    /**
     * Creates new, empty instance of {@code TweetTableModel}
     */
    public TweetTableModel() {
        this(new ArrayList<Tweet>());
    }

    /**
     * Creates new instance of {@code TweetTableModel} with initial data
     *
     * @param dataList initial data for table model
     */
    public TweetTableModel(List<Tweet> dataList) {
        if (dataList == null) throw new NullPointerException("Data list can not be null.");
        this.dataList = dataList;
    }

    /**
     * Sets new data for table model. If {@code dataList} parameter is {@code null}, empty list is set as data.
     * After setting {@code dataList}, listeners are notified about model change.
     *
     * @param dataList data to be set
     */
    public void setDataList(List<Tweet> dataList) {
        if (dataList == null) {
            this.dataList = new ArrayList<Tweet>();
        } else {
            this.dataList = dataList;
        }
        fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public int getRowCount() {
        return this.dataList.size();
    }

    @Override
    public int getColumnCount() {
        return TweetTableModel.COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        /*

        columnIndex     fieldName       fieldType
        =========================================
            0           author          String
            1           content         String
            2           dateCreated     String

         */

        Tweet rowValue = this.dataList.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return rowValue.getAuthor().fullName;
            case 1:
                return rowValue.getContent();
            case 2:
                return TweetTableModel.DATE_FORMAT.format(rowValue.getDateCreated());
        }

        return null;
    }

    @Override
    public String getColumnName(int column) {
        return TweetTableModel.COLUMN_NAMES[column];
    }


}

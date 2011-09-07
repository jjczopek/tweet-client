package rest.client.util;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for Tweeter Client.
 */
public class Util {

    private static Options commandLineOptions = null;

    private Util() {
        throw new AssertionError("Util class can't be instantiated");
    }

    /**
     * Centers given frame on the screen
     *
     * @param frame frame to center
     */
    public static void centerFrame(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = frame.getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        frame.setLocation(x, y);
    }

    /**
     * Centers given dialog on the screen
     *
     * @param dialog dialog to center
     */
    public static void centerDialog(JDialog dialog) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = dialog.getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        dialog.setLocation(x, y);
    }

    /**
     * Provides command line options which can be passed as arguments for the application.
     * This options are used to parse command line arguments upon application startup.
     *
     * @return command line options available for this application
     */
    public static Options getCommandLineParameters() {

        if (Util.commandLineOptions == null) {
            Util.commandLineOptions = new Options();
            Option urlOption = OptionBuilder.withArgName("url").hasArgs().withDescription("Url to Tweeter application").create("url");
            Option aboutOption = new Option("about", "About this application");
            Util.commandLineOptions.addOption(urlOption);
            Util.commandLineOptions.addOption(aboutOption);
        }

        return Util.commandLineOptions;
    }

    /**
     * Resize columns of given table. New column widths are passed in array od doubles, which represent
     * new column width in percentages, where 0.0 is 0% and 1.0 is 100% of total table width.
     *
     * @param table       which table columns resize
     * @param percentages new widths of columns in percentages
     * @throws Error if table column count is different than element count in percentages array
     */
    public static void setPreferredTableColumnWidths(JTable table, double[] percentages) {
        if (table.getColumnModel().getColumnCount() != percentages.length)
            throw new Error("Column count is different than percentages array length");

        Dimension tableDimension = table.getSize();

        double total = 0;
        for (double percent : percentages) {
            total += percent;
        }

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int columnIndex = 0; columnIndex < table.getColumnModel().getColumnCount(); columnIndex++) {
            TableColumn column = table.getColumnModel().getColumn(columnIndex);
            int columnWidth = (int) (tableDimension.width * (percentages[columnIndex] / total));
            column.setPreferredWidth(columnWidth);
            column.setMinWidth(columnWidth);
        }
    }

    /**
     * Utility method for hashing user password using MD5 hashing algorithm
     *
     * @param password password to be hashed
     * @return string representation of MD5 hash from user password; {@code null} if {@code NoSuchAlgorithmException}
     *         or any other error occurred
     */
    public static String hashUserPassword(String password) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
        byte[] data = password.getBytes();
        if (m != null) {
            m.update(data, 0, data.length);
        } else {
            return null;
        }
        BigInteger decodedHash = new BigInteger(1, m.digest());
        return String.format("%1$032X", decodedHash).toLowerCase();
    }

    /**
     * Utility class for holding session Id of logged user
     */
    public static class SessionHolder {
        private static String sessionId;

        /**
         * Return session id of logged user
         *
         * @return stored session id ({@code null} if there is no session stored)
         */
        public static String getSessionId() {
            return sessionId;
        }

        /**
         * Stores session id. {@code Null} value means, that user is not logged in
         *
         * @param sessionId session id to store, null if session is to be cleared
         */
        public static void setSessionId(String sessionId) {
            SessionHolder.sessionId = sessionId;
        }
    }

}

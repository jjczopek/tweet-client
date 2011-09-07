package rest.client.gui;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import rest.client.beans.Tweet;
import rest.client.gui.log.TextAreaAppender;
import rest.client.util.MultilineTableCellRenderer;
import rest.client.util.TweetTableModel;
import rest.client.util.TweeterProxy;
import rest.client.util.Util;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jerzy
 * Date: 18.07.11
 * Time: 19:20
 * To change this template use File | Settings | File Templates.
 */
public class MainFrame {

    private final Logger LOG = Logger.getLogger(MainFrame.class);

    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JButton getFollowedBtn;
    private JButton postBtn;
    private JButton loginBtn;
    private JButton exitBtn;
    private JButton getAllBtn;
    private JPanel logPanel;
    private JTextArea logArea;
    private JPanel contentPanel;
    private JTable tweetTable;
    private TweeterProxy proxy;

    /**
     * Creates MainFrame instance. Url is used to create instance of {@code TweeterProxy}, which is then used to
     * communicate with tweeter application.
     *
     * @param url
     */
    public MainFrame(String url) {
        if (url == null) throw new NullPointerException("URL must be provided.");
        this.proxy = new TweeterProxy(url);

        logArea.setText("");
        TextAreaAppender textAreaAppender = (TextAreaAppender) Logger.getRootLogger().getAppender("TEXTAREA");
        textAreaAppender.setTextArea(logArea);


        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        /*
        Display login dialog
         */
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginDialog loginDlg = new LoginDialog(proxy);
                Util.centerDialog(loginDlg);
                loginDlg.clearDialog();
                loginDlg.setVisible(true);
            }
        });

        tweetTable.setModel(new TweetTableModel());
        getAllBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAllTweets();
            }
        });
        init();
        getFollowedBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFollowed();
            }
        });

        /*
        If logged in, show new post dialog. If not logged in, show appropriate message.
         */
        postBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String sessionId = Util.SessionHolder.getSessionId();

                if (sessionId == null || !isSessionValid(sessionId)) {
                    displayError("Jesteś niezalogowany.");
                } else {
                    NewPostDialog postDialog = new NewPostDialog(proxy);
                    Util.centerDialog(postDialog);
                    postDialog.clearDialog();
                    postDialog.setVisible(true);
                }
            }
        });
    }

    /**
     * Loads tweets of users, that currently logged in user is following. If user is not logged in
     * or his session has expired, apropriate message is displayed.
     */
    private void loadFollowed() {

        String sessionId = Util.SessionHolder.getSessionId();

        if (sessionId == null || !isSessionValid(sessionId)) {
            LOG.error("Jesteś niezalogowany.");
            displayError("Jesteś niezalogowany.");
        } else {
            List<Tweet> followedTweets = proxy.getTweetProxy().followingUsersTweets(sessionId).getTweets();
            TweetTableModel tableModel = (TweetTableModel) tweetTable.getModel();
            tableModel.setDataList(followedTweets);
        }
    }

    /**
     * Checks whether user session is still valid.
     *
     * @param sessionId session to check
     * @return {@code true} if user session is still valid, {@code false} otherwise.
     */
    private boolean isSessionValid(String sessionId) {
        return proxy.getSessionProxy().isSessionValid(sessionId);
    }

    /**
     * Loads all tweets from application. The number of loaded tweets is determined
     * by API on the server side (by default, only 100 are fetched)
     */
    private void loadAllTweets() {
        if (proxy == null) {
            displayError("Brak połączenia z aplikacją");
        } else {
            List<Tweet> tweetList = proxy.getTweetProxy().all(null).getTweets();
            if (tweetList != null) {
                TweetTableModel model = (TweetTableModel) tweetTable.getModel();
                model.setDataList(tweetList);
                Util.setPreferredTableColumnWidths(tweetTable, TweetTableModel.PREFERRED_COLUMN_WIDTHS);
            } else {
                displayError("Nie udało się pobrać wiadomości");
            }
        }
    }

    /**
     * Utility method for displaying error message
     *
     * @param message text of error message to be displayed
     */
    private void displayError(String message) {
        JOptionPane.showMessageDialog(mainPanel, message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

    private void init() {
        // set custom cell renderer for content column
        TableColumn contentColumn = tweetTable.getColumnModel().getColumn(1);
        contentColumn.setCellRenderer(new MultilineTableCellRenderer());
    }


    public static void main(String[] args) {

        CommandLineParser cliParser = new GnuParser();
        String url = null;
        try {
            CommandLine cli = cliParser.parse(Util.getCommandLineParameters(), args);

            if (cli.hasOption("about")) {
                AboutDialog dialog = new AboutDialog();
                dialog.pack();
                Util.centerDialog(dialog);
                dialog.setVisible(true);
                return;
            }

            url = cli.getOptionValue("url", "http://localhost:9000/rest");
            JFrame frame = new JFrame("Tweet client 1.0 [" + url + "]");
            frame.setContentPane(new MainFrame(url).mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            Util.centerFrame(frame);
            frame.setVisible(true);
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


}

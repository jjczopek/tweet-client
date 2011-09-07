package rest.client.gui;

import org.apache.log4j.Logger;
import rest.client.util.TweeterProxy;
import rest.client.util.Util;

import javax.swing.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {

    private final Logger LOG = Logger.getLogger(LoginDialog.class);

    private JPanel contentPane;
    private JButton loginBtn;
    private JButton closeBtn;
    private JTextField loginFld;
    private JPasswordField passwordFld;
    private JLabel loginLbl;
    private JLabel passwordLbl;
    private JLabel statusLbl;
    private final TweeterProxy proxy;

    public LoginDialog(TweeterProxy proxy) {
        this.proxy = proxy;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(loginBtn);

        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        closeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pack();
    }

    /**
     * Tries to log in user. If user credentials are correct, session id is stored in session holder.
     * If there is an error, it is displayed on the status label and is logged.
     */
    private void onOK() {
        // Try to authenticate
        String sessionId;

        sessionId = this.proxy.getSessionProxy().login(getLogin(), Util.hashUserPassword(getPassword()));

        if (!sessionId.equalsIgnoreCase("null")) {
            Util.SessionHolder.setSessionId(sessionId);
            clearDialog();
            LOG.info("Logged in.");
            dispose();
        } else {
            statusLbl.setText("Nie udało się zalogować.");
            LOG.error("Login attempt failed.");
        }
    }


    private void onCancel() {
// add your code here if necessary
        LOG.info("Login cancelled.");
        clearDialog();
        dispose();
    }

    protected String getLogin() {
        return this.loginFld.getText();
    }

    protected String getPassword() {
        return String.valueOf(this.passwordFld.getPassword());
    }

    protected void clearDialog() {
        this.loginFld.setText("");
        this.passwordFld.setText("");
    }


    public static void main(String[] args) {
        LoginDialog dialog = new LoginDialog(new TweeterProxy());
        dialog.pack();
        dialog.setVisible(true);
        dialog.setModal(true);
        System.exit(0);
    }

}

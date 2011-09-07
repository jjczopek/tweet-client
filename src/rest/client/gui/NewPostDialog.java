package rest.client.gui;

import rest.client.util.TweeterProxy;
import rest.client.util.Util;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.*;

public class NewPostDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea postArea;
    private JLabel postLengthLbl;
    private final TweeterProxy proxy;

    public NewPostDialog(TweeterProxy proxy) {
        this.proxy = proxy;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setResizable(false);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
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
        postArea.setDocument(new TweeterPostDocument());
        postArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int lenght = postArea.getText().length();
                postLengthLbl.setText(lenght + "/255");
            }
        });
        pack();
    }


    /**
     * Submits data entered by user. If there is an error, error is displayed. If everything went OK, message is displayed
     * with id of new tweet and postDialog is disposed.
     */
    private void onOK() {
// add your code here

        // add post
        String content = postArea.getText();
        String result = proxy.getTweetProxy().create(Util.SessionHolder.getSessionId(), content);

        if (result.equalsIgnoreCase("CONTENT_TOO_LONG")) {
            JOptionPane.showMessageDialog(this, "Wiadomość może mieć maksymalnie 255 znaków", "Błąd", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        if (result.equalsIgnoreCase("SESSION_INVALID")) {
            JOptionPane.showMessageDialog(this, "Twoja sesja jest nieprawidłowa", "Błąd", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JOptionPane.showMessageDialog(this, "Twoja wiadomość została dodana. Jej identyfikator to: " + result, "Dodano", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        NewPostDialog dialog = new NewPostDialog(new TweeterProxy());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void clearDialog() {
        postArea.setText("");
        postLengthLbl.setText("0/255");
    }
}

/**
 * Document with maximum allowed text lenght.
 */
class TweeterPostDocument extends PlainDocument {
    private final int MAX_LENGHT;

    /**
     * Creates new instance, with maximum length as given.
     *
     * @param MAX_LENGHT maximum text length.
     * @throws AssertionError when max length is negative
     */
    TweeterPostDocument(int MAX_LENGHT) {
        if (MAX_LENGHT < 0) throw new AssertionError("Document length must be positive.");
        this.MAX_LENGHT = MAX_LENGHT;
    }

    /**
     * Creates new instance with maximum of 255 char text length
     */
    TweeterPostDocument() {
        this(255);
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        // check string being inserted does not exceed max length

        if (getLength() + str.length() > MAX_LENGHT) {
            // If it does, then truncate it

            str = str.substring(0, MAX_LENGHT - getLength());
        }
        super.insertString(offs, str, a);
    }

}

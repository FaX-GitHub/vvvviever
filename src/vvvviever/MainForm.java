/*
 *
 *  (c) Copyright 2017 ^-_-^ (http://www.tikotako.tk).
 *
 *  VVVVIEVER is licensed under the GNU General Public License v3.0.
 *
 *   You may obtain a copy of the License at:
 *
 *        http://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package vvvviever;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import static vvvviever.Logger.debug;

/**
 * Created by ^-_-^ on 14/05/2017 @ 00:12.
 **/

public class MainForm
{
    private static JFrame frame;
    private JPanel mainPanel;
    private JButton getButton;
    private JTextField urlField;
    private JTextPane logPanel;
    private JProgressBar progressBar1;
    private JButton clearButton;

    private VVVVID vvvvid;


    private static Action[] textActions = { new DefaultEditorKit.CutAction(), new DefaultEditorKit.CopyAction(), new DefaultEditorKit.PasteAction() };

    private MainForm()
    {
        JPopupMenu menu = new JPopupMenu("Edit");
        for (Action textAction : textActions)
        {
            menu.add(new JMenuItem(textAction));
        }

        logPanel.setComponentPopupMenu(menu);
        urlField.setComponentPopupMenu(menu);

        new Logger(logPanel);

        vvvvid = new VVVVID();

        urlField.setText("https://www.vvvvid.it/#!show/643/sagrada-reset");

        getButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                logPanel.setText("");

                if (debug)
                {
                    new Thread(() ->
                    {
                        vvvvid.getUrls(urlField.getText());
                    }).start();
                    return;
                }

                if (!vvvvid.iHazID())
                {
                    {
                        if (JOptionPane.showConfirmDialog(null, "Can't get the connection ID.", "ERROR - RETRY ?", JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        {
                            vvvvid.getConnID();
                        }
                    }
                } else
                {
                    new Thread(() ->
                    {
                        vvvvid.getUrls(urlField.getText());
                    }).start();
                }
            }
        });
        clearButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String tmpStr = logPanel.getText();
                tmpStr = tmpStr.replace("\t", "");
                String[] hurr = tmpStr.split("\r\n");
                tmpStr = "";
                for (String aHurr : hurr)
                {
                    if ((aHurr.contains("https")) && (aHurr.indexOf("https") == 0))
                    {
                        tmpStr += aHurr + "\r\n";
                    }
                }
                logPanel.setText(tmpStr);
            }
        });
    }

    public static void main(String[] args)
    {
        frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //frame.pack();
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

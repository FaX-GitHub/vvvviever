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

import com.sun.xml.internal.ws.api.addressing.AddressingVersion;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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


    private static Action[] textActions = {new DefaultEditorKit.CutAction(), new DefaultEditorKit.CopyAction(), new DefaultEditorKit.PasteAction()};

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

        urlField.setText("https://www.vvvvid.it/#!show/363/ranpo-kitan-game-of-laplace");

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
                if (vvvvid.episodes != null)
                {
                    /*
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
                    */
                    logPanel.setText("");
                    for (VVVVID.EP gimmy : vvvvid.episodes)
                    {
                        if (gimmy.playable)
                        {
                            logPanel.setText(logPanel.getText() + gimmy.link + "\r\n");
                        }
                    }

                    logPanel.setText(logPanel.getText() + "\r\n---------------------------------------------------------\r\n");

                    // List<VVVVID.EP> leEpisodes = Arrays.asList(vvvvid.episodes); // asList genera una lista non modificabile
                    List<VVVVID.EP> leEpisodes = new LinkedList(Arrays.asList(vvvvid.episodes));

                    JFileChooser fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if(fc.showSaveDialog(null)!= JFileChooser.APPROVE_OPTION)
                    {
                        logPanelAdd("FAIL");
                        return;
                    }

                    String PATH = fc.getSelectedFile().getPath(); // "D:\\VEDERE E CANCELLARE\\ANIME\\tales-of-zestiria-the-x";

                    File[] files = new File(PATH).listFiles();
                    for (File file : files)
                    {
                        if (file.isFile())
                        {
                            for (VVVVID.EP episode : leEpisodes)
                            {
                                if (compare(file.getName(), episode.title))
                                {
                                    logPanelAdd(file.getName() + " == " + episode.title);
                                    File file2 = new File(PATH + "\\[" + episode.number + "] " + file.getName());
                                    if (!file2.exists())
                                    {
                                        if (file.renameTo(file2))
                                        {
                                            logPanelAdd("Renamed");
                                        } else
                                        {
                                            logPanelAdd("FAIL");
                                        }
                                    }
                                    leEpisodes.remove(episode);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private boolean compare(String fileName, String epName)
    {
        if (debug)
        {
            System.out.print(fileName + " ");
        }

        if (fileName.contains(".mp4"))
        {
            int gennaro = 0;
            int gennaro2 = 0;
            fileName = fileName.replace(".mp4", "");
            epName = epName.replaceAll("[\\/:*?\"<>|]", "");
            for (String fronk : fileName.split(" "))
            {
                fronk = fronk.replaceAll("\\(", "\\\\(");
                fronk = fronk.replaceAll("\\)", "\\\\)");

                if (epName.matches("(?i).*" + fronk + ".*"))
                {
                    gennaro++;
                }
                gennaro2++;
            }
            if (gennaro == gennaro2)
            {
                if (debug)
                {
                    System.out.println(epName + " " + gennaro + " -- " + gennaro2 + "\t<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                } else
                {
                    return true;
                }
            } else if (debug)
            {
                System.out.println(epName + " " + gennaro + " -- " + gennaro2);
            }

        }
        return false;
    }

    void logPanelAdd(String text)
    {
        logPanel.setText(logPanel.getText() + text + "\r\n");
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

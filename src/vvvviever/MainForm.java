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

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import static vvvviever.Logger.debug;

/**
 * Created by ^-_-^ on 14/05/2017 @ 00:12.
 **/

public class MainForm
{
    private static JFrame mainFrame;
    private static JFrame logFrame;
    private JPanel mainPanel;
    private JButton getButton;
    private JTextField urlField;
    private JTextPane logPanel;
    private JProgressBar progressBar1;
    private JButton clearButton;
    private JTextPane linkPane;

    private VVVVID vvvvid;


    private static Action[] textActions = {new DefaultEditorKit.CutAction(), new DefaultEditorKit.CopyAction(), new DefaultEditorKit.PasteAction()};

    private MainForm()
    {
        JPopupMenu menu = new JPopupMenu("Edit");

        for (Action textAction : textActions)
        {
            menu.add(new JMenuItem(textAction));
        }

        logPanel.setAutoscrolls(true);
        linkPane.setComponentPopupMenu(menu);
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
                        logPanelAdd("Connecting to vvvvid...");
                        vvvvid.getUrls(urlField.getText());
                        logPanelAdd("Checking list....");
                        if (vvvvid.episodes != null)
                        {
                            logPanelAdd("Listing eps.....");
                            linkPane.setText("");
                            for (Episode gimmy : vvvvid.episodes)
                            {
                                if (gimmy.playable)
                                {
                                    linkPane.setText(linkPane.getText() + gimmy.link + "\r\n");
                                }
                            }
                            logPanelAdd("Generating file......");
                            try
                            {
                                int i = 0;
                                RandomAccessFile randomAccessFile = new RandomAccessFile(vvvvid.episodes[0].serie_id + ".derp", "rw");
                                randomAccessFile.setLength(0);
                                randomAccessFile.writeInt(vvvvid.episodes.length);
                                ByteBuffer classSizes = ByteBuffer.allocate(vvvvid.episodes.length * 4); // int 4byte
                                for (Episode ep : vvvvid.episodes)
                                {
                                    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
                                    objectOutputStream.writeObject(ep);
                                    objectOutputStream.flush();
                                    classSizes.putInt(byteOutputStream.toByteArray().length);
                                    randomAccessFile.write(byteOutputStream.toByteArray());
                                    objectOutputStream.close();
                                    i++;
                                }
                                randomAccessFile.write(classSizes.array());
                                randomAccessFile.close();
                                logPanelAdd("File saved >> " + System.getProperty("user.dir") + "\\" + vvvvid.episodes[0].serie_id + ".derp");
                            } catch (Exception eX)
                            {
                                eX.printStackTrace();
                                logPanelAdd("ERROR SAVING FILE >>> " + eX.getMessage());
                            }
                        }
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
                        logPanelAdd("1");
                        vvvvid.getUrls(urlField.getText());
                        logPanelAdd("2");
                        if (vvvvid.episodes != null)
                        {
                            logPanelAdd("3");
                            for (Episode gimmy : vvvvid.episodes)
                            {
                                if (gimmy.playable)
                                {
                                    // logPanel.setText(logPanel.getText() + gimmy.link + "\r\n");
                                }
                            }
                            logPanelAdd("4");
                            try
                            {
                                FileOutputStream fos = new FileOutputStream(vvvvid.episodes[0].serie_id + ".derp", false);
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                for (Episode ep : vvvvid.episodes)
                                {
                                    oos.writeObject(ep);
                                }
                                oos.close();
                                logPanelAdd("File saved >> " + System.getProperty("user.dir") + vvvvid.episodes[0].serie_id + ".derp");
                            } catch (Exception eX)
                            {
                                eX.printStackTrace();
                                logPanelAdd("ERROR SAVING FILE >>> " + eX.getMessage());
                            }
                        }
                    }).start();
                }
            }
        });

        clearButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    int asd = 637;
                    RandomAccessFile file = new RandomAccessFile(asd + ".derp", "r");
                    long fileSize = file.length();
                    System.out.println("File size: " + fileSize);
                    int tot = file.readInt();
                    System.out.println("Episodes: " + tot);
                    file.seek(file.length() - (tot * 4));
                    int epsDataSize[] = new int[tot];
                    int epsDataSizePos[] = new int[tot];
                    long crap = fileSize - ((4 * tot) + 4);

                    for (int i = 0; i < tot; i++)
                    {
                        epsDataSize[i] = file.readInt();
                    }
                    Episode[] eps = new Episode[tot];

                    file.seek(4);
                    for (int i = 0; i < tot; i++)
                    {
                        byte[] buffer = new byte[epsDataSize[i]];
                        file.read(buffer);
                        ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
                        ObjectInput in = new ObjectInputStream(bis);
                        eps[i] = (Episode) in.readObject();
                        bis.close();
                    }

                    for (Episode ep : eps)
                    {
                        System.out.println("Episode [" + ep.number + "] " + ep.title);
                    }

                    file.close();

                    //InputStream buffer = new BufferedInputStream(file);
                    //ObjectInput input = new ObjectInputStream (buffer);
                    //deserialize the List
                    //Episode ep = (Episode)input.readObject();


                    /*
                    FileInputStream fis = new FileInputStream("637.derp");
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    //List<Episode> eps = new ArrayList<Episode>();
                    //eps = (Episode[])ois.readObject();


                    List<Episode> leEpisodes = (ArrayList<Episode>) ois.readObject();
                    /*
                    Episode [] eps = (Episode[])ois.readObject();

                    List<Episode> leEpisodes = new LinkedList(Arrays.asList(eps));*/
/*
                    for (Episode episode : leEpisodes)
                    {
                        linkPane.setText(linkPane.getText() + episode.title + "\r\n");
                    }

                    ois.close();*/
                } catch (Exception eX)
                {
                    eX.printStackTrace();
                }

                /*
                // List<VVVVID.EP> leEpisodes = Arrays.asList(vvvvid.episodes); // asList genera una lista non modificabile
                List<Episode> leEpisodes = new LinkedList(Arrays.asList());

                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
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
                */
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

        mainFrame = new JFrame("MainForm");
        mainFrame.setContentPane(new MainForm().mainPanel);
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //logFrame.pack();
        mainFrame.setSize(1200, 600);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}

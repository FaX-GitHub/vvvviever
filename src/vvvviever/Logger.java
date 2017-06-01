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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by FaX on 27/05/2017.
 */

public class Logger
{
    public static final boolean debug = true;

    private static JTextPane leLogPanel;

    Logger(JTextPane lepanel)
    {
        leLogPanel = lepanel;
        leLogPanel.setText(("Loggin start @ " + getTime()) + "\r\n");
    }

    private static String getTime()
    {
        return new SimpleDateFormat("[dd/MM/yyyy - HH:mm:ss]").format(new Date());
    }

    public static void log(String wut)
    {
        leLogPanel.setText(leLogPanel.getText() + getTime() + "\t" + wut + "\r\n");
    }
}

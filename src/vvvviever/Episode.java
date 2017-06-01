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

import java.io.Serializable;

/**
 * Created by FaX on 01/06/2017.
 */

public class Episode implements Serializable
{
    int id;
    int number;
    int serie_id;
    String serie_title;
    String title;
    String link;
    Boolean playable;
    String available;
}
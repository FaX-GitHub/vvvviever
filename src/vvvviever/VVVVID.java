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

/**
 * Created by ^-_-^ on 14/05/2017 @ 19:22.
 **/

package vvvviever;

import com.google.gson.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

import static vvvviever.Logger.*;
import static vvvviever.Logger.debug;

class VVVVID
{
    private static String CONNECTIONIDURL = "https://www.vvvvid.it/user/login";
    private static String LOGINJSON = "{\"action\":\"login\",\"email\":\"\",\"password\":\"\",\"facebookParams\":\"\",\"mobile\":false,\"hls\":false,\"flash\":false,\"isIframe\":false}";

    enum Channels
    {
        FILM("film"),
        SERIES("series"),
        ANIME("anime"),
        SHOWS("shows"),
        KIDS("kids"),
        GAMES("games");

        final String value;

        Channels(String wut)
        {
            this.value = wut;
        }

        @Override
        public String toString()
        {
            return value;
        }
    }

    private boolean gotID;
    private String connectionID;

    VVVVID()
    {
        connectionID = "";

        if (!debug)
        {
            getConnID();
        }
    }

    boolean iHazID()
    {
        // from sendPost and getConnID
        return gotID;
    }

    void getConnID()
    {
        log("getConnID()");
        try
        {
            sendPost();
        } catch (Exception e)
        {
            e.printStackTrace();
            log("ERROR >> sendPost() >> " + e.getMessage());
            gotID = false;
        }
        gotID = true;
    }

    void getUrls(String url)
    {
        String fronk;
        StringBuilder list = new StringBuilder();

        if (!debug)
        {
            fronk = "{\"result\":\"frak\",\"message\":\"frell\"}";
            try
            {
                fronk = sendGet(url);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {

            fronk = "{\"result\":\"ok\",\"message\":\"ok\",\"data\":[{\"id\":0,\"show_id\":643,\"season_id\":704,\"show_type\":4,\"number\":1,\"episodes\":[{\"id\":6598,\"season_id\":704,\"video_id\":514196,\"number\":\"01\",\"title\":\"MEMORY in CHILDREN 1/3\",\"thumbnail\":\"https://static.vvvvid.it/img/thumbs/Dynit/SagradaReset/SagradaReset_S01Ep01-t.jpg\",\"description\":\"Sagrada Reset - primo episodio\",\"expired\":false,\"seen\":false,\"playable\":true,\"ondemand_type\":3},{\"id\":6674,\"season_id\":704,\"video_id\":514527,\"number\":\"02\",\"title\":\"MEMORY in CHILDREN 2/3\",\"thumbnail\":\"https://static.vvvvid.it/img/thumbs/Dynit/SagradaReset/SagradaReset_S01Ep02-t.jpg\",\"description\":\"Sagrada Reset - secondo episodio\",\"expired\":false,\"seen\":false,\"playable\":true,\"ondemand_type\":3},{\"id\":6711,\"season_id\":704,\"video_id\":514758,\"number\":\"03\",\"title\":\"CAT, GHOST and REVOLUTION SUNDAY 1/2\",\"thumbnail\":\"https://static.vvvvid.it/img/thumbs/Dynit/SagradaReset/SagradaReset_S01Ep03-t.jpg\",\"description\":\"Sagrada Reset - terzo episodio\",\"expired\":false,\"seen\":false,\"playable\":true,\"ondemand_type\":3},{\"id\":6763,\"season_id\":704,\"video_id\":515061,\"number\":\"04\",\"title\":\"CAT, GHOST and REVOLUTION SUNDAY 2/2\",\"thumbnail\":\"https://static.vvvvid.it/img/thumbs/Dynit/SagradaReset/SagradaReset_S01Ep04-t.jpg\",\"description\":\"Sagrada Reset - quarto episodio\",\"expired\":false,\"seen\":false,\"playable\":true,\"ondemand_type\":3},{\"id\":6809,\"season_id\":704,\"video_id\":515280,\"number\":\"05\",\"title\":\"Il mondo-biglia e il candy-resist\",\"thumbnail\":\"https://static.vvvvid.it/img/thumbs/Dynit/SagradaReset/SagradaReset_S01Ep05-t.jpg\",\"description\":\"Sagrada Reset - quinto episodio\",\"expired\":false,\"seen\":false,\"playable\":true,\"ondemand_type\":3},{\"id\":6827,\"season_id\":704,\"video_id\":515593,\"number\":\"06\",\"title\":\"WITCH, PICTURE and RED EYE GIRL 1/3\",\"thumbnail\":\"https://static.vvvvid.it/img/thumbs/Dynit/SagradaReset/SagradaReset_S01Ep06-t.jpg\",\"description\":\"Sagrada Reset - sesto episodio\",\"expired\":false,\"seen\":false,\"playable\":true,\"ondemand_type\":3},{\"id\":6837,\"season_id\":704,\"video_id\":515885,\"number\":\"07\",\"title\":\"WITCH, PICTURE and RED EYE GIRL 2/3\",\"thumbnail\":\"https://static.vvvvid.it/img/thumbs/Dynit/SagradaReset/SagradaReset_S01Ep07-t.jpg\",\"description\":\"Sagrada Reset - settimo episodio\",\"expired\":false,\"seen\":false,\"playable\":true,\"ondemand_type\":3},{\"id\":6850,\"season_id\":704,\"video_id\":516241,\"number\":\"08\",\"title\":\"WITCH, PICTURE and RED EYE GIRL 3/3\",\"thumbnail\":\"https://static.vvvvid.it/img/thumbs/Dynit/SagradaReset/SagradaReset_S01Ep08-t.jpg\",\"description\":\"Sagrada Reset - ottavo episodio\",\"expired\":false,\"seen\":false,\"playable\":true,\"ondemand_type\":3},{\"id\":6851,\"season_id\":704,\"video_id\":-1,\"number\":\"09\",\"title\":\"Episodio 09\",\"expired\":false,\"seen\":false,\"playable\":false,\"availability_date\":\"01 giu 2017\",\"ondemand_type\":3}],\"name\":\"EPISODI\"}]}";
        }

        try
        {
            JsonElement jelement = new JsonParser().parse(fronk);
            System.out.println(jelement);
            JsonObject jobject = jelement.getAsJsonObject();
            JsonObject jobject2;

            if (jobject.get("result").getAsString().equals("ok") && jobject.get("message").getAsString().equals("ok"))
            {
                jobject = jobject.getAsJsonArray("data").get(0).getAsJsonObject();

                for (int i = 0; i < jobject.getAsJsonArray("episodes").size(); i++)
                {

                    System.out.println("loop : " + i);
                    jobject2 = jobject.getAsJsonArray("episodes").get(i).getAsJsonObject();

                    if (debug)
                    {
                        System.out.println(jobject2.get("title").getAsString());
                        System.out.println(jobject2.get("id").getAsString());
                        System.out.println(jobject2.get("playable").getAsBoolean());
                    }

                    list.append(jobject2.get("title").getAsString())
                            .append(" [ID : ")
                            .append(jobject2.get("id").getAsString())
                            .append(" - Playable : ")
                            .append(jobject2.get("playable").getAsBoolean())
                            .append("]\r\n");
                    if (!jobject2.get("playable").getAsBoolean())
                    {
                        list.append("\tavailability date: ")
                                .append(jobject2.get("availability_date").getAsString())
                                .append("\r\n");
                    } else
                    {
                        list.append("\t" + url)
                                .append("/")
                                .append(jobject2.get("season_id").getAsString())
                                .append("/")
                                .append(jobject2.get("video_id").getAsString())
                                .append("/")
                                .append("\r\n");
                    }
                    log(list.toString());
                    list.delete(0, 9001);
                }
            } else
            {
                log("Error while parsing the JSON.");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            log("Error while parsing the JSON.");
        }
    }

    private String sendGet(String url) throws Exception
    {
        log("sendGet(" + url + ")");
        url = url.replace("https://www.vvvvid.it/#!show/", "");
        url = url.split("/")[0];
        url = "https://www.vvvvid.it/vvvvid/ondemand/" + url + "/seasons/?conn_id=" + connectionID;
        log("sendGet(" + url + ")");
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        log("Response Code: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //log("Response (JSON):\t" + response.toString());


        return response.toString();
    }

    private void sendPost() throws Exception
    {
        log("sendPost(" + CONNECTIONIDURL + ")");

        String url = CONNECTIONIDURL;
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/json");

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(LOGINJSON);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        log("Response Code: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //log("Response (JSON):\t" + response.toString());

        JsonElement jelement = new JsonParser().parse(response.toString());
        JsonObject jobject = jelement.getAsJsonObject();

        if (jobject.get("result").getAsString().equals("loggedout"))
        {
            jobject = jobject.getAsJsonObject("data");
            connectionID = jobject.get("conn_id").getAsString();
            log("Got ID:\t" + connectionID);
            gotID = true;
        } else
        {
            throw new Exception("JSON parsing error");
        }
    }

}


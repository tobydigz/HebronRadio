package com.digzdigital.hebronradio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Digz on 18/03/2016.
 */
public class ParseJSON {
    public static String[] trackUri_server;
    public static String[] trackUri2_server;


    public static final String JSON_ARRAY = "hebron_array";
    public static final String STREAM_LINK_1 = "intranet_link";
    public static final String STREAM_LINK_2 = "internet_link";
    public static String trackUri;
    public static String trackUri2;

    private JSONArray content = null;

    private String json;

    public ParseJSON(String json) {this.json = json;}

    protected void parseJSON(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            content = jsonObject.getJSONArray(JSON_ARRAY);

            trackUri_server = new String[content.length()];
            trackUri2_server = new String[content.length()];

            for (int i=0; i<content.length();i++){
                JSONObject jo = content.getJSONObject(i);
                trackUri_server[i] = jo.getString(STREAM_LINK_1);
                trackUri2_server[i] = jo.getString(STREAM_LINK_2);

                trackUri = Arrays.toString(trackUri_server);
                trackUri2 = Arrays.toString(trackUri2_server);
               trackUri = trackUri.substring(1, trackUri.length() - 1);
               trackUri2 = trackUri2.substring(1, trackUri2.length() - 1);


            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}


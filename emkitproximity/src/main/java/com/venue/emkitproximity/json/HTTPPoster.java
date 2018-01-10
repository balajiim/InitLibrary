package com.venue.emkitproximity.json;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.venue.emkitproximity.utils.Logger;
import com.venue.initv2.EmkitInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HTTPPoster {

    private final String TAG = "HTTPPoster";

    public ResponseHolder doPostasBytes(String url, HashMap<String, Object> hashMap, String eMkitAPIKey) throws
            IOException, JSONException, InvalidResponseCode {


        ResponseHolder holder = new ResponseHolder();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("Content-Type", "application/json;charset=utf-8");

        JSONObject jsonObject = null;

        try {

            jsonObject = new JSONObject();
            Set<String> set = hashMap.keySet();
            Iterator<String> iterator = set.iterator();

            while (iterator.hasNext()) {
                String key = iterator.next();
                Object object = hashMap.get(key);

                if (object instanceof JSONObject) {
                    if (key.trim().length() == 0) {
                        jsonObject = (JSONObject) object;
                    } else {
                        JSONObject valueObject = (JSONObject) object;
                        jsonObject.put(key, valueObject);
                    }
                } else if (object instanceof String) {
                    String value = (String) object;
                    jsonObject.put(key, value);
                } else if (object instanceof Integer) {
                    Integer integer = (Integer) object;
                    jsonObject.put(key, integer);
                } else if (object instanceof byte[]) {

                } else if (object instanceof Boolean) {
                    boolean val = (Boolean) object;
                    jsonObject.put(key, val);
                }

            }

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        //add reuqest header
        con.setRequestMethod("POST");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        //String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(jsonObject.toString());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        Logger.i(TAG, "\nSending 'POST' request to URL : " + url);
        Logger.i(TAG, "Post parameters : " + jsonObject.toString());
        Logger.i(TAG, "Response Code : " + responseCode);

        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            holder.setResponseCode(con.getResponseCode());
            holder.setResponseStr(response.toString());

        } else {
            holder.setResponseStr("");
            holder.setResponseCode(responseCode);
            return holder;
        }
        // Logger.d(TAG, "Response: " + string);

        return holder;
    }

    public ResponseHolder doPost(String url, HashMap<String, String> hashMap) throws IOException, JSONException, InvalidResponseCode {


        ResponseHolder holder = new ResponseHolder();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("Content-Type", "application/json");

        JSONObject jsonObject = null;

        try {

            jsonObject = new JSONObject();
            Set<String> set = hashMap.keySet();
            Iterator<String> iterator = set.iterator();

            while (iterator.hasNext()) {
                String key = iterator.next();
                Object object = hashMap.get(key);

                if (object instanceof JSONObject) {
                    if (key.trim().length() == 0) {
                        jsonObject = (JSONObject) object;
                    } else {
                        JSONObject valueObject = (JSONObject) object;
                        jsonObject.put(key, valueObject);
                    }
                } else if (object instanceof String) {
                    String value = (String) object;
                    jsonObject.put(key, value);
                } else if (object instanceof Integer) {
                    Integer integer = (Integer) object;
                    jsonObject.put(key, integer);
                } else if (object instanceof byte[]) {

                } else if (object instanceof Boolean) {
                    boolean val = (Boolean) object;
                    jsonObject.put(key, val);
                }

            }

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        //add reuqest header
        con.setRequestMethod("POST");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        //String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(jsonObject.toString());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        Logger.i(TAG, "\nSending 'POST' request to URL : " + url);
        Logger.i(TAG, "Post parameters : " + jsonObject.toString());
        Logger.i(TAG, "Response Code dilip: " + responseCode);

        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            holder.setResponseCode(con.getResponseCode());
            holder.setResponseStr(response.toString());
            Map<String, List<String>> map = con.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                    Logger.i(TAG, "Key : " + entry.getKey() +
                            " ,Value : " + entry.getValue());
                    if ((entry.getKey()).indexOf("Set-Cookie") != -1) {
                        String value = "" + entry.getValue();
                        String cookie[] = value.split("\\,");
                        for (int i = 0; i < cookie.length; i++) {
                            Logger.i(TAG, "cookie value " + cookie[i]);
                            if (cookie[i].indexOf(".DOTNETNUKE") != -1) {
                                SharedPreferences pref = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
                                Editor editor = pref.edit();
                                editor.putString("signalr_cookie", "" + cookie[i]);
                                editor.commit();
                            }
                            if (cookie[i].indexOf("AWSELB") != -1) {
                                SharedPreferences pref = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
                                Editor editor = pref.edit();
                                editor.putString("signalr_cookie_extra", "" + cookie[i]);
                                editor.commit();
                            }
                        }

                    }
                }
            }
        } else {
            holder.setResponseStr("");
            holder.setResponseCode(responseCode);
            return holder;
        }
        // Logger.d(TAG, "Response: " + string);

        return holder;

    }

    public ResponseHolder doGet(String url, HashMap<String, String> hashMap, String eMkitApiKey) throws IOException, JSONException, InvalidResponseCode {


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", JSONParser.USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        JSONObject jsonObject = new JSONObject();
        Set<String> set = hashMap.keySet();
        Iterator<String> iterator = set.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = hashMap.get(key);
            con.setRequestProperty(key, value);
            jsonObject.put(key, value);
        }

        //add request header

        int responseCode = con.getResponseCode();
        ResponseHolder holder = new ResponseHolder();
        holder.setResponseCode(responseCode);
        Logger.i(TAG, "\nSending 'GET' request to URL : " + url);
        Logger.i(TAG, "Response Code : " + responseCode);

        if (responseCode != 200)
            return holder;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        holder.setResponseStr(response.toString());
        Map<String, List<String>> map = con.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                Logger.i(TAG, "Key : " + entry.getKey() +
                        " ,Value : " + entry.getValue());
                if ((entry.getKey()).indexOf("Set-Cookie") != -1) {
                    String value = "" + entry.getValue();
                    String cookie[] = value.split("\\,");
                    for (int i = 0; i < cookie.length; i++) {
                        Logger.i(TAG, "cookie value " + cookie[i]);
                        if (cookie[i].indexOf(".DOTNETNUKE") != -1) {
                            SharedPreferences pref = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
                            Editor editor = pref.edit();
                            editor.putString("signalr_cookie", "" + cookie[i]);
                            editor.commit();
                        }
                        if (cookie[i].indexOf("AWSELB") != -1) {
                            SharedPreferences pref = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
                            Editor editor = pref.edit();
                            editor.putString("signalr_cookie_extra", "" + cookie[i]);
                            editor.commit();
                        }
                    }

                }
            }
        }
        return holder;
    }

    public ResponseHolder doGet(String url, HashMap<String, String> hashMap) throws IOException, JSONException, InvalidResponseCode {


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", JSONParser.USER_AGENT);
        //con.setRequestProperty("Content-Type", "application/json");
        //con.setRequestProperty("Accept", "application/json");

        JSONObject jsonObject = new JSONObject();
        Set<String> set = hashMap.keySet();
        Iterator<String> iterator = set.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = hashMap.get(key);
            con.setRequestProperty(key, value);
            jsonObject.put(key, value);
        }

        //add request header

        int responseCode = con.getResponseCode();
        ResponseHolder holder = new ResponseHolder();
        holder.setResponseCode(responseCode);
        Logger.i(TAG, "\nSending 'GET' request to URL : " + url);
        Logger.i(TAG, "Response Code : " + responseCode);

        if (responseCode != 200)
            return holder;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        holder.setResponseStr(response.toString());
        Map<String, List<String>> map = con.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                Logger.i(TAG, "Key : " + entry.getKey() +
                        " ,Value : " + entry.getValue());
                if ((entry.getKey()).indexOf("Set-Cookie") != -1) {
                    String value = "" + entry.getValue();
                    String cookie[] = value.split("\\,");
                    for (int i = 0; i < cookie.length; i++) {
                        Logger.i(TAG, "cookie value " + cookie[i]);
                        if (cookie[i].indexOf(".DOTNETNUKE") != -1) {
                            SharedPreferences pref = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
                            Editor editor = pref.edit();
                            editor.putString("signalr_cookie", "" + cookie[i]);
                            editor.commit();
                        }
                        if (cookie[i].indexOf("AWSELB") != -1) {
                            SharedPreferences pref = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
                            Editor editor = pref.edit();
                            editor.putString("signalr_cookie_extra", "" + cookie[i]);
                            editor.commit();
                        }
                    }

                }
            }
        }
        return holder;
    }

    public ResponseHolder doDelete(String url, HashMap<String, String> hashMap) throws IOException, JSONException, InvalidResponseCode {


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("DELETE");
        con.setRequestProperty("User-Agent", JSONParser.USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        JSONObject jsonObject = new JSONObject();
        Set<String> set = hashMap.keySet();
        Iterator<String> iterator = set.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = hashMap.get(key);
            con.setRequestProperty(key, value);
            jsonObject.put(key, value);
        }

        //add request header

        int responseCode = con.getResponseCode();
        ResponseHolder holder = new ResponseHolder();
        holder.setResponseCode(responseCode);
        Logger.i(TAG, "\nSending 'GET' request to URL : " + url);
        Logger.i(TAG, "Response Code : " + responseCode);

        if (responseCode != 200)
            return holder;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        holder.setResponseStr(response.toString());
        Map<String, List<String>> map = con.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                Logger.i(TAG, "Key : " + entry.getKey() +
                        " ,Value : " + entry.getValue());
                if ((entry.getKey()).indexOf("Set-Cookie") != -1) {
                    String value = "" + entry.getValue();
                    String cookie[] = value.split("\\,");
                    for (int i = 0; i < cookie.length; i++) {
                        Logger.i(TAG, "cookie value " + cookie[i]);
                        if (cookie[i].indexOf(".DOTNETNUKE") != -1) {
                            SharedPreferences pref = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
                            Editor editor = pref.edit();
                            editor.putString("signalr_cookie", "" + cookie[i]);
                            editor.commit();
                        }
                        if (cookie[i].indexOf("AWSELB") != -1) {
                            SharedPreferences pref = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
                            Editor editor = pref.edit();
                            editor.putString("signalr_cookie_extra", "" + cookie[i]);
                            editor.commit();
                        }
                    }

                }
            }
        }
        return holder;
    }

    public ResponseHolder doPut(String url, String jsonString, HashMap<String, String> hashMap)
            throws IOException, JSONException, InvalidResponseCode {


        ResponseHolder holder = new ResponseHolder();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        con.setRequestProperty("Accept", "application/json");
        Set<String> set = hashMap.keySet();
        Iterator<String> iterator = set.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = hashMap.get(key);
            //System.out.println("Manimaran "+key+"----"+value);
            if ("Authorization".equalsIgnoreCase(key)) {
                con.setRequestProperty("Authorization", value);
            } else {
                con.setRequestProperty(key, value);
            }
        }

        //add reuqest header
        con.setRequestMethod("PUT");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        //String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(jsonString);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        Logger.i(TAG, "\nSending 'POST' request to URL : " + url);
        Logger.i(TAG, "Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        holder.setResponseCode(con.getResponseCode());
        holder.setResponseStr(response.toString());

        return holder;
    }


    public ResponseHolder doPost(String url, String jsonString, String formData)
            throws IOException, JSONException, InvalidResponseCode {


        ResponseHolder holder = new ResponseHolder();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json");

        //String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(jsonString);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        holder.setResponseCode(con.getResponseCode());
        Logger.i(TAG, "\nSending 'POST' request to URL : " + url);
        Logger.i(TAG, "Post parameters : " + jsonString);
        Logger.i(TAG, "Response Code : " + responseCode);
        if (responseCode != 200)
            return holder;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        holder.setResponseStr(response.toString());

        return holder;
    }
}
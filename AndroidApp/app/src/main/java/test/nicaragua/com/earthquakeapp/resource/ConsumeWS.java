package test.nicaragua.com.earthquakeapp.resource;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import test.nicaragua.com.earthquakeapp.database.ConstantDB;

/**
 * Created by macbook_prueba on 6/1/18.
 */

public class ConsumeWS {

    static String WEB_SERVICE_URL = ConstantDB.HOST_NAME+"/fdsnws/event/1/";

    public static JSONObject getJsonObject(String method, String parameters, int timeOut) {

        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url;
            if (parameters.equals("")) {
                url = new URL(WEB_SERVICE_URL + method);
            } else {
                url = new URL(WEB_SERVICE_URL + method + "?" + parameters);
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(timeOut);
            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch(Exception e){
            e.printStackTrace();
        } finally{
            urlConnection.disconnect();
        }
        JSONObject result = null;
        try {
            result = new JSONObject(data.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;

    }
}

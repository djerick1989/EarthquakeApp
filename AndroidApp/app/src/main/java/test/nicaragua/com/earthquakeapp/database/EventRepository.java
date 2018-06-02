package test.nicaragua.com.earthquakeapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.nicaragua.com.earthquakeapp.model.Event;
import test.nicaragua.com.earthquakeapp.resource.ConsumeWS;

/**
 * Created by macbook_prueba on 6/1/18.
 */

public class EventRepository extends CommonRepository {

    public EventRepository(Context context) {
        super(context);
    }

    public static long insert(Event event, Context context) {
        EventRepository rep = new EventRepository(context);
        try {
            ContentValues values = new ContentValues();
            values.put(ConstantDB.EVENT.ID, event.getId());
            values.put(ConstantDB.EVENT.LATITUDE, event.getLatitude());
            values.put(ConstantDB.EVENT.LONGITUDE, event.getLongitude());
            values.put(ConstantDB.EVENT.MAGNITUDE, event.getMagnitude());
            values.put(ConstantDB.EVENT.TYPE, event.getType());
            values.put(ConstantDB.EVENT.TIME, event.getTime());
            values.put(ConstantDB.EVENT.DIRECTION, event.getDirection());
            rep.open();
            long resultado = rep.mDb.insert(ConstantDB.TABLE.EVENT, null, values);
            return resultado;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        } finally {
            rep.close();
        }
    }

    public static long delete(Context context) {
        EventRepository rep = new EventRepository(context);
        try {
            ContentValues values = new ContentValues();
            rep.open();
            long resultado = rep.mDb.delete(ConstantDB.TABLE.EVENT, null, null);
            return resultado;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        } finally {
            rep.close();
        }
    }

    public static List<Event> consumeWS(Context context, Date starttime, Date endtime) {
        List<Event> lstEvents = new ArrayList<>();

        String method = "query";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");


        String parameters = "format=geojson&starttime="+df.format(starttime)+"&endtime="+df.format(endtime);

        JSONObject jsonObject = ConsumeWS.getJsonObject(method, parameters, ConstantDB.TIME_OUT_REQUEST);

        try {
            JSONArray features = jsonObject.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                if (i == 0) {
                    delete(context);
                }
                JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                JSONObject properties = features.getJSONObject(i).getJSONObject("properties");

                Event event = new Event();
                event.setId(features.getJSONObject(i).getString("id"));
                event.setType(features.getJSONObject(i).getString("type"));
                event.setLongitude(coordinates.getDouble(0));
                event.setLatitude(coordinates.getDouble(1));
                event.setDirection(properties.getString("place"));
                event.setMagnitude(properties.getDouble("mag"));
                event.setTime(properties.getLong("time"));
                long id = insert(event, context);
                if (id > 0) {
                    lstEvents.add(event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstEvents;
    }

}

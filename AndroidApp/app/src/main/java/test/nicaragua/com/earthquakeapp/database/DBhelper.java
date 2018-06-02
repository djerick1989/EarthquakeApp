package test.nicaragua.com.earthquakeapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by macbook_prueba on 6/1/18.
 *
 * Metodo manejador de las tablas de la base de datos.
 */

public class DBhelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE " + ConstantDB.TABLE.EVENT + " (" +
            ConstantDB.EVENT.ID + " TEXT NOT NULL, " +
            ConstantDB.EVENT.TYPE + " TEXT NOT NULL, " +
            ConstantDB.EVENT.DIRECTION + " TEXT NOT NULL, " +
            ConstantDB.EVENT.LATITUDE + " DOUBLE NOT NULL, " +
            ConstantDB.EVENT.LONGITUDE + " DOUBLE NOT NULL, " +
            ConstantDB.EVENT.TIME + " LONG NOT NULL, " +
            ConstantDB.EVENT.MAGNITUDE + " DOUBLE NOT NULL)";

    public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_EVENTS);
        } catch (SQLiteException ex) {
            Log.v("Create table exception", ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

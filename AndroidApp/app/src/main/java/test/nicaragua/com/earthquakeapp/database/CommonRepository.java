package test.nicaragua.com.earthquakeapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by macbook_prueba on 6/1/18.
 */

public class CommonRepository {

    public SQLiteDatabase mDb;
    private final Context mContext;
    private final DBhelper mDBhelper;

    public CommonRepository(Context context) {
        File sd = Environment.getDataDirectory();
        File dbf = new File(sd, "earthquake");
        mContext = context;
        mDBhelper = new DBhelper(mContext, "earthquake", null, 1);
    }

    public void open() throws SQLiteException {
        try {
            mDb = mDBhelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            Log.d("Error:", ex.getMessage());
            mDb = mDBhelper.getReadableDatabase();
        }
    }

    public void close() {
        try {
            mDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

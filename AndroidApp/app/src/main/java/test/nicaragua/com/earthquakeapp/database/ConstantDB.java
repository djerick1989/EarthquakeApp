package test.nicaragua.com.earthquakeapp.database;

/**
 * Created by macbook_prueba on 6/1/18.
 */

public class ConstantDB {

    public static final String HOST_NAME = "https://earthquake.usgs.gov";
    public static final int TIME_OUT_REQUEST = 2000;

    public class TABLE {
        public static final String EVENT = "event";
    }

    public class EVENT {
        public static final String ID = "id";
        public static final String TYPE = "type";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String MAGNITUDE = "magnitude";
        public static final String DIRECTION = "direction";
        public static final String TIME = "time";
    }
}

package abassawo.c4q.nyc.flickrfeed.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by c4q-Abass on 2/12/16.
 */
public class DatabaseHelper  extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String FAVORITE_IMAGES_TABLE = DBSchema.NAME;
    private static final String DATABASE_NAME = "FLICKRFEED_DATABASE";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(initTable(FAVORITE_IMAGES_TABLE));

    }

    public String initTable(String tableName) {
        String initSQL = "create table " + tableName + "(" +
                "_id integer primary key autoincrement, " +
                DBSchema.Cols._ID + ", " +
                DBSchema.Cols.URL + ", " +
                DBSchema.Cols.CAPTION + ", " +
                DBSchema.Cols.OWNER + ", " +
                DBSchema.Cols.LAT + ", " +
                DBSchema.Cols.LON + ")";
        return initSQL;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_IMAGES_TABLE);
        db.execSQL(initTable(FAVORITE_IMAGES_TABLE));

    }

}

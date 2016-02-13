package abassawo.c4q.nyc.flickrfeed.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by c4q-Abass on 2/12/16.
 */
public class Serializer {
    private static Context mContext;
    private  static SQLiteDatabase mDatabase;



    public Serializer(Context context, SQLiteDatabase db){
        mContext = context;
        this.mDatabase = db;
    }

    private static ContentValues getContentValuesfromFaves(GalleryItem item){
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.Cols._ID, item.getId());
        cv.put(DBSchema.Cols.URL, item.getUrl());
        return cv;
    }


    public List<GalleryItem> getFaves(){
        List<GalleryItem> items = new ArrayList<>();
        ImageCursorWrapper cursor= queryImages(DBSchema.NAME, null, null);
        if(cursor.getCount() <= 0) return items;
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                items.add(cursor.getFaveItem());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }
        return items;
    }


    public void addFavorite(GalleryItem item){
        ContentValues cv = getContentValuesfromFaves(item);
        mDatabase.insert(DBSchema.NAME, null, cv);
    }



    public boolean hasFaves(){
        return getFaves().size() > 0;
    }



    public ImageCursorWrapper queryImages(String tableName, String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                tableName,
                null,   //select all columns
                whereClause,
                whereArgs,
                null, //group by
                null,
                null);
        return new ImageCursorWrapper(cursor);
    }

    public static class ImageCursorWrapper extends CursorWrapper {
        public ImageCursorWrapper(Cursor cursor){
            super(cursor);
        }

        public GalleryItem getFaveItem(){
            int id = getInt(getColumnIndex(DBSchema.Cols._ID));
            String url = getString(getColumnIndex(DBSchema.Cols.URL));
            GalleryItem  item = new GalleryItem();
            item.setId("" + id);
            item.setUrl(url);
            return item;
        }

    }

}

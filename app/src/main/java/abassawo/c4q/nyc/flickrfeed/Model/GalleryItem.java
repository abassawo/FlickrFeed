package abassawo.c4q.nyc.flickrfeed.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by c4q-Abass on 10/26/15.
 */
public class GalleryItem implements Serializable {
    private String mCaption;
    private String mOwner;
    private String mId;
    private String mUrl;
    private double mLat;
    private double mLon;


    public double getLat(){
        return mLat;
    }
    public void setLat(double lat){
        mLat = lat;
    }

    public double getLon(){
        return mLon;
    }

    public void setLon(double lon){
        this.mLon = lon;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {

        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getOwner(){
        return mOwner;
    }

    public void setOwner(String owner){
        mOwner = owner;
    }

    public Uri getPhotoPageuri(){
        return Uri.parse("http://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(mOwner)
                .appendPath(mId).build();
    }


    public String toString(){
        return mCaption;
    }

}

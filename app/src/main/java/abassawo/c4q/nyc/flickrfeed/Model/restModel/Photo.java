package abassawo.c4q.nyc.flickrfeed.model.restModel;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

/**
 * Created by c4q-Abass on 2/12/16.
 */
public class Photo {

    @SerializedName("id")
    public String mId;

    @SerializedName("secret")
    public String mSecret;

    @SerializedName("title")
    public String mTitle;

    @SerializedName("server")
    public int mServer;

    @SerializedName("url_s")
    public String mUrl;

    public String toString() {
        return "Photo{" +
                "mId=" + mId +
                ", mSecret='" + mSecret + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mServer=" + mServer +
                ", mUrl=" + mUrl + "}";
    }

    public String getUrl() {

        return mUrl;
    }


    private String mCaption;
    private String mOwner;


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



}

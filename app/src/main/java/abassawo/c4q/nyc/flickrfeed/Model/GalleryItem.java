package abassawo.c4q.nyc.flickrfeed.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

/**
 * Created by c4q-Abass on 10/26/15.
 */
public class GalleryItem {

    @SerializedName("caption")
    private String mCaption;

    @SerializedName("owner")
    private String mOwner;

    @SerializedName("id")
    private String mId;

    @SerializedName("url")
    private String mUrl;

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

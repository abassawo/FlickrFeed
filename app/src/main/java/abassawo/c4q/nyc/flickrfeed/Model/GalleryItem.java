package abassawo.c4q.nyc.flickrfeed.Model;

import android.net.Uri;

/**
 * Created by c4q-Abass on 10/26/15.
 */
public class GalleryItem {

    private String mCaption;
    private String mOwner;
    private String mId;
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

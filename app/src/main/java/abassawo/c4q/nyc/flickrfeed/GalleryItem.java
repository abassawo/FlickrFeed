package abassawo.c4q.nyc.flickrfeed;

/**
 * Created by c4q-Abass on 10/26/15.
 */
public class GalleryItem {

    private String mCaption;

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

    private String mId;
    private String mUrl;

    public String toString(){
        return mCaption;
    }
}

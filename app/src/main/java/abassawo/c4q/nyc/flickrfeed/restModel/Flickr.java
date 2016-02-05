package abassawo.c4q.nyc.flickrfeed.restModel;


import abassawo.c4q.nyc.flickrfeed.restModel.FlickrService;
import retrofit2.Retrofit;

/**
 * Created by c4q-Abass on 1/27/16.
 */
public class Flickr {

    public static final String API_URL = "http://www.flickr.photos.search";
    private static final Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).build();
    private static final FlickrService SERVICE = retrofit.create(FlickrService.class);

    public static FlickrService getService(){
        return SERVICE;
    }

}

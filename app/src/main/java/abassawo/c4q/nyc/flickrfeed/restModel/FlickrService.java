package abassawo.c4q.nyc.flickrfeed.restModel;



import java.util.List;

import abassawo.c4q.nyc.flickrfeed.model.Config;
import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by c4q-Abass on 12/18/15.
 */
public interface FlickrService {

    @GET("/photos?api_key= " + Config.API_KEY)
    public Response getGalleryItems(Callback<List<GalleryItem>> cb );

    @GET("/photos?api_key= " + Config.API_KEY)
    public Response search(@Query("per_page") int perPage,String date, Callback<List<GalleryItem>> cb );
}

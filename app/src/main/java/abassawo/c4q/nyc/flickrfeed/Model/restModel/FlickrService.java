package abassawo.c4q.nyc.flickrfeed.model.restModel;

import java.util.List;

import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by c4q-Abass on 2/12/16.
 */
public interface FlickrService {

    @GET("/services/rest/?method=flickr.photos.getRecent")
    void getRecentPhotos(@Query("per_page") int perPage,  @Query("page") int page, Callback<RestResponse> cb);


}

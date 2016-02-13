package abassawo.c4q.nyc.flickrfeed.model.restModel;

import com.squareup.okhttp.OkHttpClient;

import abassawo.c4q.nyc.flickrfeed.model.Config;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;

/**
 * Created by c4q-Abass on 2/12/16.
 */
public class Flickr {

    private static final FlickrService SERVICE = getRestAdapter().create(FlickrService.class);
    private static final String TAG = "sFlickr";


    public static FlickrService getService() {
        return SERVICE;
    }

    private static RestAdapter getRestAdapter(){
        OkHttpClient client = new OkHttpClient();
        OkClient retrofitClient = new OkClient(client);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.baseUrl)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("api_key", Config.API_KEY);
                        request.addQueryParam("format", "json");
                        request.addQueryParam("nojsoncallback", "1"); //disable the JSONP callba
                        request.addQueryParam("extras", "url_s, geo");

                    }
                }).setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog(TAG))
                .setClient(retrofitClient).build();
        return restAdapter;
    }
}

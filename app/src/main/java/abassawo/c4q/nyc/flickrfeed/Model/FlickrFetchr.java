package abassawo.c4q.nyc.flickrfeed.model;

import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;


/**
 * Created by c4q-Abass on 10/26/15.
 */
public class FlickrFetchr {

    private static final String API_KEY = "e1eceea6273135d361fa463be7fc8aa0";
    private static final String TAG = "FlickrFetchr.class";
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";



    private  static Uri ENDPOINT = Uri.parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("method", "flickr.photos.getRecent")
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s").build();

    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() + " : with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = inputStream.read(buffer)) > 0){
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }finally{
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private String buildUrl(String method, String query){
        Uri.Builder uriBuilder = ENDPOINT.buildUpon().appendQueryParameter("method", method);
        if(method.equals(SEARCH_METHOD)){
            uriBuilder.appendQueryParameter("text", query);
        }
        return uriBuilder.build().toString();
    }
    public  List<GalleryItem> fetchRecentPhotos(){
    String url = buildUrl(FETCH_RECENTS_METHOD, null);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> searchPhotos(String query){
        String url = buildUrl(SEARCH_METHOD, query);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem>downloadGalleryItems(String url){
        List<GalleryItem>items = new ArrayList<>();
        try {
            //Use a Uri builder to build complete url for flickr api request.
            Log.d(TAG + " URL",  url);
            String jsonString = getUrlString(url);
            Log.i("JSON parsed", "Received JSON: " + jsonString);

            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException e) {
            Log.e("Failure", "error parsing json", e);
        } catch(JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return items;
    }



    private void parseItems(List<GalleryItem> items,JSONObject jsonBody) throws IOException, JSONException{
        JSONObject photosJSONObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJSONObject.getJSONArray("photo");

        for (int i = 0; i < photoJsonArray.length() ; i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));
            if(!photoJsonObject.has("url_s")){
                continue;
            }
            item.setUrl(photoJsonObject.getString("url_s"));
            item.setOwner(photoJsonObject.getString("owner"));
            items.add(item);
        }

    }


}

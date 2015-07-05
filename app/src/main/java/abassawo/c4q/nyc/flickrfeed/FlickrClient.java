package abassawo.c4q.nyc.flickrfeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by c4q-Abass on 6/20/15.
 */
public class FlickrClient {

    public static final String TAG = "FlickrGetter";

    public static final String FLICKR_JSON_API = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";

    // TODO : Step 1 - complete this method,return the entire json string.
    public String getJsonString() throws IOException {
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(FLICKR_JSON_API);
            connection = (HttpsURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null)  {
                builder.append(line + "\n");
            }
            return builder.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    // TODO : Step 2 - by using Step 1's result, get 20 images' url addresses and save into ArrayList in String.
    public List<String> getBitmapList() throws JSONException, IOException {
        String jsonString = getJsonString();
        JSONObject object = new JSONObject(jsonString);
        JSONArray items = object.getJSONArray("items");
        List<String> imageUrlList = new ArrayList<String>();

        for (int i = 0; i < items.length(); i++){
            JSONObject item = (JSONObject) items.get(i);
            JSONObject media = (JSONObject) item.get("media");
            String imageUrl = media.getString("m");
            imageUrlList.add(imageUrl);
        }

        return imageUrlList;
    }
}
package abassawo.c4q.nyc.flickrfeed.model.restModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by c4q-Abass on 2/12/16.
 */
public class RestResponse {
    @SerializedName("photos")
    public PhotosInfo mPhotosInfo;

    @SerializedName("stat")
    public String mStatus;

    @Override
    public String toString() {
        return "GetRecentPhotosResponse{" +
                "mPhotosInfo=" + mPhotosInfo +
                ", mStatus='" + mStatus + '\'' +
                '}';
    }
}

package abassawo.c4q.nyc.flickrfeed.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by c4q-Abass on 10/27/15.
 */
public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mRequestHandler;
    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();

    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T>mTThumbnailDownloadListener;

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }
    //Use interface to deegate duty of handling loaded img to another class(gallery fragment)
    public interface ThumbnailDownloadListener<T>{
        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener){
        mTThumbnailDownloadListener = listener;
    }

    public void queueThumbnail(T target, String url){
        Log.i(TAG, "Got a URL:" + url);
        if(url == null){
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();
        }
    }

    public void clearQueue(){
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);

    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == MESSAGE_DOWNLOAD){
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got request for URL: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    public void handleRequest(final T target){
        try {
            final String url = mRequestMap.get(target);
            if (url == null) {
                return;
            }

            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmaps created");

            //because mResponsehandler is assoc. w/ the main thread's looper,
            // all of the run code will be run on main thread.
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mRequestMap.get(target) != url){ //verify requestmap. by the time downloader finished downloading,
                        // rv may have recycled the holder and requestd a different url
                        //check to make sure each holder gets the correct image, even if another request has been made.
                        return;
                    }
                    mRequestMap.remove(target); //remove the photoholder-url mapping from the requestmap and set the bitmap on target.
                    mTThumbnailDownloadListener.onThumbnailDownloaded(target, bitmap);
                }
            });

        }catch(IOException ioe){
            Log.e(TAG, "Error downloading image", ioe);
        }
    }

    private void notificationRequest(final T target){

    }
}

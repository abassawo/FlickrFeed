package abassawo.c4q.nyc.flickrfeed.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import abassawo.c4q.nyc.flickrfeed.Activities.WebViewActivity;
import abassawo.c4q.nyc.flickrfeed.Model.FlickrFetchr;
import abassawo.c4q.nyc.flickrfeed.Model.GalleryItem;
import abassawo.c4q.nyc.flickrfeed.Services.PollService;
import abassawo.c4q.nyc.flickrfeed.Model.QueryPrefs;
import abassawo.c4q.nyc.flickrfeed.R;
import abassawo.c4q.nyc.flickrfeed.Model.ThumbnailDownloader;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends VisibleFragment {
    private String TAG = "GalleryFragment";
    RecyclerView mRecyclerView;
    private static List<GalleryItem> mItems = new ArrayList<>();
    private PhotoAdapter mAdapter;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initThumbnailDL();
        updateItems();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycer_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        setupAdapter();
        return v;
    }



    public void initThumbnailDL(){
        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        //Cal
        mThumbnailDownloader.setThumbnailDownloadListener(new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
            @Override
            public void onThumbnailDownloaded(PhotoHolder holder, Bitmap thumbnail) {
                Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                holder.bindDrawable(drawable);
            }
        });
        Log.i(TAG, "Background thread started");
    }

    private void updateItems(){
        String query = QueryPrefs.getStoredQuery(getActivity());
        new FetchItemsTask(query).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if(PollService.isServiceAlarmOn(getActivity())){
            toggleItem.setTitle(R.string.stop_polling);
        } else {
            toggleItem.setTitle(R.string.start_polling);
        }


        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = QueryPrefs.getStoredQuery(getActivity());
                searchView.setQuery(query, false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "Query text submitted: " + query);
                QueryPrefs.setStoredQuery(getActivity(), query);
                updateItems();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "Query text submitted: " + newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_iteam_clear:
                QueryPrefs.setStoredQuery(getActivity(), null);
                updateItems();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean startAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), startAlarm);
                getActivity().invalidateOptionsMenu();
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }


    private void setupAdapter() {
        //Checking if is added so thaat getactivity() will not be null
        //As we are using an asynctask, cannot assume the frag is attached to an activity
        if (isAdded()) {
            mAdapter = new PhotoAdapter(mItems);
            mRecyclerView.setAdapter(mAdapter);
        }
    }


    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mItemImageView;
        private GalleryItem mGalleryItem;

        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindDrawable(Drawable drawable){
            mItemImageView.setImageDrawable(drawable);
        }

        public void bindGalleryItem(GalleryItem item){
            mGalleryItem = item;
        }

        @Override
        public void onClick(View v) {
            Intent i = WebViewActivity.newIntent(getActivity().getApplicationContext(), mGalleryItem.getPhotoPageuri());
            startActivity(i);
        }
    }

    public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> items) {
            mGalleryItems = items;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem item = mGalleryItems.get(position);
            holder.bindGalleryItem(item);
            Drawable placeholder = getResources().getDrawable(R.drawable.default_placeholder);
            holder.bindDrawable(placeholder);
            mThumbnailDownloader.queueThumbnail(holder, item.getUrl());
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }
    //Incorporate an asynctaskloader subclass to this project.
    public class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        private String mQuery;
        public FetchItemsTask(String query){
            mQuery = query;
        }
        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            if(mQuery == null) {
                return new FlickrFetchr().fetchRecentPhotos();
            } else {
                return new FlickrFetchr().searchPhotos(mQuery);
            }
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }
    }

}

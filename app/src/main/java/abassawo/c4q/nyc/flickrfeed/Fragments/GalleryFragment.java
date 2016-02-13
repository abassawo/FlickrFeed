package abassawo.c4q.nyc.flickrfeed.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import abassawo.c4q.nyc.flickrfeed.activities.WebViewActivity;
import abassawo.c4q.nyc.flickrfeed.model.FlickrFetchr;
import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;
import abassawo.c4q.nyc.flickrfeed.services.PollService;
import abassawo.c4q.nyc.flickrfeed.model.QueryPrefs;
import abassawo.c4q.nyc.flickrfeed.R;
import abassawo.c4q.nyc.flickrfeed.model.ThumbnailDownloader;
import butterknife.Bind;
import butterknife.ButterKnife;


public class GalleryFragment extends VisibleFragment {
    private static final String QUERY_KEY_ = "query";
    public static String TAG = "GalleryFragment";
    private static List<GalleryItem> mItems = new ArrayList<>();
    private PhotoAdapter mAdapter;
    @Bind(R.id.fragment_photo_gallery_recycer_view)
    RecyclerView mRecyclerView;
    private String mQuery;
   // private OnFragmentinteractionListener mCallback;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;


    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            mCallback = (OnFragmentinteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentinteractionListener");
//        }
//        super.onAttach(activity);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        ButterKnife.bind(this, v);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        setupAdapter();
        return v;
    }

    public void updateItems(String query) {
        if (isConnectedToInternet()) {
            //mCallback.setSearchMethod(query);
            new FetchItemsTask(query).execute();
        } else {
            final View view = getActivity().findViewById(R.id.main_container);
            Snackbar.make(view, "Internet is not Connected", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Connect", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    }).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateItems(mQuery);
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifiInfo.isConnected() || dataInfo.isConnected();
    }


    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem randomItem = menu.findItem(R.id.menu_item_random);
        randomItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getActivity(), "Fetching the latest...", Toast.LENGTH_SHORT).show();
                mQuery = null;
                updateItems(mQuery);
                return false;
            }
        });
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
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
                search(query);
                searchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "Query text submitted: " + newText);
                return false;
            }
        });
    }

    public void search(String query) {
        Log.d(TAG, "Query text submitted: " + query);
        Toast.makeText(getActivity(), "Searching " + query, Toast.LENGTH_SHORT).show();
        QueryPrefs.setStoredQuery(getActivity(), query);
        mQuery = query;
        //mCallback.setSearchMethod(mQuery);
        new FetchItemsTask(mQuery).execute();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Background thread destroyed");
    }


    private void setupAdapter() {
        //Checking if is added so thaat getactivity() will not be null
        //As we are using an asynctask, cannot assume the frag is attached to an activity
        if (isAdded()) {
            mAdapter = new PhotoAdapter(getActivity(), mItems);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
//
//    public interface OnFragmentinteractionListener {
//         void setSearchMethod(String query);
//    }


    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView mItemImageView;
        private GalleryItem mGalleryItem;

        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindGalleryItem(Context context, GalleryItem item) {
            mGalleryItem = item;
            Picasso.with(context).load(item.getUrl()).into(mItemImageView);

        }

//        public void bindDrawable(Drawable drawable){
//            mItemImageView.setImageDrawable(drawable);
//        }

        @Override
        public void onClick(View v) {
            Intent i = WebViewActivity.newIntent(getActivity().getApplicationContext(), mGalleryItem.getPhotoPageuri());
            i.putExtra(WebViewActivity.INTENT_EXTRA_GALLERY_ITEM, mGalleryItem);
            startActivity(i);
        }

        @Override
        public boolean onLongClick(View v) {
            PreviewFragment preview = PreviewFragment.newInstance(mGalleryItem);
            preview.show(getFragmentManager(), PreviewFragment.TAG);
            return false;
        }


    }


    public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> mGalleryItems;
        private Context mContext;

        public PhotoAdapter(Context context, List<GalleryItem> items) {
            mContext = context;
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
            holder.bindGalleryItem(mContext, item);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }

    }

    public class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        private String mQuery;


        public FetchItemsTask(String query) {
            mQuery = query;
        }

        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            if (mQuery == null)
                return new FlickrFetchr().fetchRecentPhotos();
            else
                return new FlickrFetchr().searchPhotos(mQuery);

        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }
    }


}

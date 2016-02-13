package abassawo.c4q.nyc.flickrfeed.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import abassawo.c4q.nyc.flickrfeed.R;
import abassawo.c4q.nyc.flickrfeed.model.DatabaseHelper;
import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;
import abassawo.c4q.nyc.flickrfeed.model.Serializer;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by c4q-Abass on 2/12/16.
 */
public class FavesFragment extends GalleryFragment {
    private GalleryFragment.PhotoAdapter mAdapter;
    @Bind(R.id.fragment_photo_gallery_recycer_view)
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        ButterKnife.bind(this, v);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return v;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
    }


    @Override
    public void updateItems(String query) {
        if (isAdded()) {
            SQLiteDatabase db = new DatabaseHelper(getActivity()).getWritableDatabase();
            Serializer serializer = new Serializer(getActivity(), db);
            List<GalleryItem> mItems = serializer.getFaves();
            mAdapter = new GalleryFragment.PhotoAdapter(getActivity(), mItems);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}

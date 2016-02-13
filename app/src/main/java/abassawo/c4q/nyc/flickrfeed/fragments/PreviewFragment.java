package abassawo.c4q.nyc.flickrfeed.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import abassawo.c4q.nyc.flickrfeed.R;
import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;

/**
 * Created by c4q-Abass on 2/12/16.
 */
public class PreviewFragment extends DialogFragment {
    public static String TAG = "PreviewFragment";
    private static String BUNDLE_EXTRA = "extra";

    public static PreviewFragment newInstance(GalleryItem galleryItem){
        PreviewFragment preview = new PreviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_EXTRA, galleryItem);
        preview.setArguments(args);
        return preview;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preview_layout, container, false);
    }
}

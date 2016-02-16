package abassawo.c4q.nyc.flickrfeed.fragments;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import abassawo.c4q.nyc.flickrfeed.R;
import abassawo.c4q.nyc.flickrfeed.model.DatabaseHelper;
import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;
import abassawo.c4q.nyc.flickrfeed.model.Serializer;
import abassawo.c4q.nyc.flickrfeed.model.TargetPhoneGallery;
import butterknife.Bind;
import butterknife.ButterKnife;
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;


import static abassawo.c4q.nyc.flickrfeed.fragments.PhotoDetailFragment.*;

/**
 * Created by c4q-Abass on 2/12/16.
 */

public class PreviewFragment extends SupportBlurDialogFragment implements View.OnClickListener{
    @Bind(R.id.preview_imageview) ImageView preview;
    @Bind(R.id.preview_download_btn)
    FloatingActionButton fabDL;
    @Bind(R.id.preview_fave_btn) FloatingActionButton faveFab;
    private GalleryItem mGalleryItem;

    public static String TAG = "PreviewFragment";
    private static String BUNDLE_EXTRA = "extra";

    public static PreviewFragment newInstance(GalleryItem galleryItem){
        PreviewFragment preview = new PreviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_EXTRA, galleryItem);
        preview.setArguments(args);
        return preview;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                dismiss();
            }
        };
        handler.postDelayed(r, 2000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGalleryItem = (GalleryItem) getArguments().getSerializable(BUNDLE_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_layout, container, false);
        ButterKnife.bind(this, view);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        fabDL.setOnClickListener(this);
        faveFab.setOnClickListener(this);
        GalleryItem item = (GalleryItem) getArguments().getSerializable(BUNDLE_EXTRA);
        Log.d(TAG, "Long clicked item's url is " + item.getUrl());
        Picasso.with(getActivity()).load(item.getUrl()).into(preview);
        return view;
    }

    @Override
    protected float getDownScaleFactor() {
        // Allow to customize the down scale factor.
        return 2;
    }

    @Override
    protected int getBlurRadius() {
        // Allow to customize the blur radius factor.
        return 10;
    }

    @Override
    protected boolean isActionBarBlurred() {
        // Enable or disable the blur effect on the action bar.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isDimmingEnable() {
        // Enable or disable the dimming effect.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isRenderScriptEnable() {
        // Enable or disable the use of RenderScript for blurring effect
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isDebugEnable() {
        // Enable or disable debug mode.
        // False by default.
        return true;
    }

    public void download(GalleryItem item){
        Picasso.with(getActivity()).load(item.getUrl()).into(new TargetPhoneGallery(getActivity().getContentResolver(), "image name", "image desc"));
        Toast.makeText(getActivity(), "Image saved to Gallery", Toast.LENGTH_SHORT).show();
    }

    public void addToFavorites(GalleryItem item){
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Serializer serializer = new Serializer(getActivity(), db);
        serializer.addFavorite(item);
        Toast.makeText(getActivity(), "Added to Favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.preview_download_btn:download(mGalleryItem);
                break;
            case R.id.preview_fave_btn:  addToFavorites(mGalleryItem);
                break;
        }
    }
}

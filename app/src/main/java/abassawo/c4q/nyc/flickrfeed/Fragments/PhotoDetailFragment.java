package abassawo.c4q.nyc.flickrfeed.fragments;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import abassawo.c4q.nyc.flickrfeed.R;
import abassawo.c4q.nyc.flickrfeed.activities.WebViewActivity;
import abassawo.c4q.nyc.flickrfeed.model.DatabaseHelper;
import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;
import abassawo.c4q.nyc.flickrfeed.model.Serializer;
import abassawo.c4q.nyc.flickrfeed.model.TargetPhoneGallery;


public class PhotoDetailFragment extends VisibleFragment  {
    private OnWebviewLoaded mCallback;


    @Override
    public void onStart() {
        super.onStart();
    }

    private static final String ARG_URI = "photos_page_url";

    private Uri mUri;
    public static WebView mWebView;
    private ProgressBar mProgressBar;
    private Intent shareIntent;

    //new Instance constructor using the uri argument
    public static PhotoDetailFragment newInstance(Uri uri){
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnWebviewLoaded) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnWebViewLoadedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  //also Inherits set retain instance and hasoptions
        setHasOptionsMenu(true);
        mUri = getArguments().getParcelable(ARG_URI);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem actionItem = (MenuItem) menu.findItem(R.id.share_image);
        setupShareMenuItem(actionItem);
    }


    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void setupShareMenuItem(MenuItem actionItem ){
        ShareActionProvider actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(actionItem);
        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        ImageView backdrop = (ImageView) getActivity().findViewById(R.id.backdrop);
        Uri bmpUri = getLocalBitmapUri(backdrop);
        shareIntent = new Intent(Intent.ACTION_SEND);
        //final GalleryItem galleryItem = (GalleryItem) getActivity().getIntent().getSerializableExtra(WebViewActivity.INTENT_EXTRA_GALLERY_ITEM);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Made with FlickrFeed");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check it out!");
        actionItem.setIcon(android.R.drawable.ic_menu_share);
        actionProvider.setShareIntent(shareIntent);
        actionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
            @Override
            public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                String packageName = intent.getComponent().getPackageName();
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this pic");
                intent.putExtra(Intent.EXTRA_STREAM, getActivity().getIntent().getData());
                intent.setPackage(packageName);
                return false;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView= inflater.inflate(R.layout.fragment_photo_detail, container, false);
        mWebView = (WebView) mView.findViewById(R.id.fragment_photo_detail_webview);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.fragment_photo_detail_progress_bar);
        setupViews(mWebView, mProgressBar);
        return mView;
    }

    public void setupViews(WebView webView, ProgressBar progressBar){
        progressBar.setMax(100); //WebChromeclient report range is from 0 to 100

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView1, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                    mCallback.onWebViewLoaded();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                return false;
            }
        });
        webView.loadUrl(mUri.toString());
    }

    public interface OnWebviewLoaded {
        void onWebViewLoaded();
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: getActivity().onBackPressed();
                break;
            case R.id.downl_this_image: showDownloadDialog();
                break;
            case R.id.star_this_image:
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Serializer serializer = new Serializer(getActivity(), db);
                serializer.addFavorite((GalleryItem) getActivity().getIntent().getSerializableExtra(WebViewActivity.INTENT_EXTRA_GALLERY_ITEM));
//                Snackbar.make(view, book.getTitle() + " has been favorited", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Toast.makeText(getActivity(), "Added to Favorites", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);

    }

    private void showDownloadDialog() {
        final GalleryItem galleryItem = (GalleryItem) getActivity().getIntent().getSerializableExtra(WebViewActivity.INTENT_EXTRA_GALLERY_ITEM);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Download image?");
        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Picasso.with(getActivity()).load(galleryItem.getUrl()).into(new TargetPhoneGallery(getActivity().getContentResolver(), "image name", "image desc"));
            Toast.makeText(getActivity(), "Image saved to Gallery", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // No action - Dismiss.e
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }







}
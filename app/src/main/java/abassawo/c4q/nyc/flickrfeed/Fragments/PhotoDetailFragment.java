package abassawo.c4q.nyc.flickrfeed.fragments;



import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import abassawo.c4q.nyc.flickrfeed.R;


public class PhotoDetailFragment extends VisibleFragment {
    @Override
    public void onStart() {
        super.onStart();
    }

    private static final String ARG_URI = "photos_page_url";

    private Uri mUri;
    public static WebView mWebView;
    private ProgressBar mProgressBar;

    //new Instance constructor using the uri argument
    public static PhotoDetailFragment newInstance(Uri uri){
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);

        PhotoDetailFragment fragment = new PhotoDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  //also Inherits set retain instance and hasoptions
        mUri = getArguments().getParcelable(ARG_URI);
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







}
package abassawo.c4q.nyc.flickrfeed.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import abassawo.c4q.nyc.flickrfeed.R;
import abassawo.c4q.nyc.flickrfeed.model.FlickrFetchr;
import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;


public class LocatrFragment extends SupportMapFragment {
    private static final String TAG = "LocatrFragment";
    private GoogleApiClient mClient;
    static LocatrFragment fragment;
    private GoogleMap mMap;
    private GalleryItem mGalleryItem;
    private Bitmap mMapImage;
    private GalleryItem mMapItem;
    private Location mCurrentLocation;


    public static LocatrFragment getInstance() {
        if (fragment == null) {
            fragment = new LocatrFragment();
        }
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    private void findImage() {
        if (isConnectedtoGPS()) {
            Toast.makeText(getActivity(), "Retrieving latest image in vicinity", Toast.LENGTH_SHORT).show();
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setNumUpdates(1);
            request.setInterval(0);
            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.i(TAG, "Got a location: " + location);
                    new AsyncSearch().execute(location);
                }

            });
        } else {
            final View view = getActivity().findViewById(R.id.main_container);
            Snackbar.make(view, "GPS is not Connected", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Connect", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Toast.makeText(getActivity(), "GPS not connected", Toast.LENGTH_LONG).show();
                    }
                })
                .build();
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (mMap == null || mMapImage == null) {
            return;
        }
        LatLng itemPoint = new LatLng(mMapItem.getLat(), mMapItem.getLon());
        LatLng myPoint = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(itemPoint)
                .include(myPoint)
                .build();
        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mMap.animateCamera(update);

        BitmapDescriptor itemBitmap = BitmapDescriptorFactory.fromBitmap(mMapImage);
        MarkerOptions itemMarker = new MarkerOptions()
                .position(itemPoint)
                .icon(itemBitmap);
        MarkerOptions myMarker = new MarkerOptions().position(myPoint);
        mMap.clear();
        mMap.addMarker(itemMarker);
        mMap.addMarker(myMarker);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_locatr, menu);
        MenuItem searchItem = menu.findItem(R.id.action_locate);
        searchItem.setEnabled(mClient.isConnected());
        if (searchItem.isEnabled()) {
            searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    findImage();
                    return true;
                }
            });
        }
    }

    public boolean isConnectedtoGPS() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo wifiInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo dataInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        return wifiInfo.isConnected() || dataInfo.isConnected();

    }


    private class AsyncSearch extends AsyncTask<Location, Void, Void> {
        private Bitmap mBitmap;
        private GalleryItem mGalleryItem;
        private Location mLocation;

        @Override
        protected Void doInBackground(Location... params) {
            mLocation = params[0];
            FlickrFetchr fetchr = new FlickrFetchr();
            List<GalleryItem> items = fetchr.searchPhotos(params[0]);
            if (items.size() == 0) {
                return null;
            }
            mGalleryItem = items.get(0);
            try {
                byte[] bytes = fetchr.getUrlBytes(mGalleryItem.getUrl());
                mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (IOException ioe) {
                Toast.makeText(getActivity(), "Connection failed", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Unable to download bitmap", ioe);
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mMapImage = mBitmap;
            mMapItem = mGalleryItem;
            mCurrentLocation = mLocation;
            updateUI();
        }
    }

}

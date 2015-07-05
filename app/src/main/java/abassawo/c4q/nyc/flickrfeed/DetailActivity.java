package abassawo.c4q.nyc.flickrfeed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import abassawo.c4q.nyc.flickrfeed.R;

public class DetailActivity extends ActionBarActivity {
    private String imageUrl = null;
    private ImageView detailImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        RelativeLayout detailLayout = (RelativeLayout) findViewById(R.id.detailContainer);
        //detailLayout.setBackgroundColor(Color.CYAN);

        detailImage= (ImageView) findViewById(R.id.detailImage);
        imageUrl = getIntent().getStringExtra("imageLink");
        Picasso.with(getApplicationContext()).load(imageUrl).resize(800, 800).centerCrop().into(detailImage);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setIcon(R.mipmap.ic_mustache);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onSaveClick (View v) {
        imageUrl = getIntent().getStringExtra("imageLink");
        Picasso.with(this)
                .load(imageUrl)
                .into(target);
        Toast.makeText(DetailActivity.this, "Downloading", Toast.LENGTH_SHORT).show();

    }



    public void onShareClick(View v) {
        imageUrl = getIntent().getStringExtra("imageLink");
        Toast.makeText(getApplicationContext(), "Preparing to share :" + imageUrl.toString(), Toast.LENGTH_LONG).show();
        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUrl);
        List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(shareIntent, 0);


        boolean intentSafe = resInfos.size() > 0;
        if (intentSafe) {
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                Log.i("Package Name", packageName);
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, imageUrl);

                intent.setPackage(packageName);
                targetShareIntents.add(intent);
            }
            Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
            startActivity(chooserIntent);


        } else {
            return;
        }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String appDirectoryName = getString(R.string.app_name);
                    File storageDir = new File (Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), appDirectoryName);

                    File file = new File(storageDir, "FlickrFile.jpg");

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    try
                    {
                        storageDir.createNewFile();
                        Bitmap bitmap = Picasso.with(DetailActivity.this)
                                .load(imageUrl).get();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(Environment.getExternalStoragePublicDirectory(
//                                Environment.DIRECTORY_PICTURES)));
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                        ostream.write(bytes.toByteArray());
                        ostream.close();
                        MediaStore.Images.Media.insertImage(getContentResolver(), "New", "Flickr", "New Image");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }



                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }



        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            if (placeHolderDrawable != null) {
            }
        }


    };
}

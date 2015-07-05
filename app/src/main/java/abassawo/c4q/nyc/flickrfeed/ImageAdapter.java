package abassawo.c4q.nyc.flickrfeed;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by c4q-Abass on 6/12/15.
 */
public class ImageAdapter extends BaseAdapter {
    Context mContext;
    List<String> imageUrlList;

    public ImageAdapter(Context mContext, List<String> imageUrlList) {
        this.mContext = mContext;
        this.imageUrlList = imageUrlList;
    }


    @Override
    public int getCount() {
        if (imageUrlList != null) {
            return this.imageUrlList.size();
        } else return 0;
    }

    @Override
    public String getItem(int position) {
        return this.imageUrlList.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        if (imageUrlList != null) {
            return (long) this.imageUrlList.get(position).hashCode();
        } else return 0;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = (ImageView) new ImageView(mContext);
        }
        String imageUrl = getItem(position);
        Picasso.with(mContext).load(imageUrl).resize(200, 200).centerCrop().into((ImageView) convertView);
        return convertView;
    }


}
package net.vokacom.drive.SlideViewPager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import net.vokacom.drive.R;

import static com.android.volley.VolleyLog.TAG;

class ViewPagerItem extends PagerAdapter {

    private Activity activity;
    private String[] images;
    private LayoutInflater inflater;

    public ViewPagerItem(Activity activity, String[] images){
        this.activity = activity;
        this.images = images;

    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.slide_pager_item,container,false);

        ImageView image;
        image = itemView.findViewById(R.id.imageViewItem);

        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);

        int height = dis.heightPixels;
        int width = dis.widthPixels;

        image.setMaxHeight(height);
        image.setMaxWidth(width);

        try {
            Glide.with(activity.getApplicationContext())
                    .load(images[position])
                    .into(image)
            
            ;
        }
        catch (Exception ex){
            Log.i(TAG, "instantiateItem: Error on glide" +ex);

        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

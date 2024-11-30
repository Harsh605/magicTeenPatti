package com.casinocards.appmagicTeenpatti.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ss.com.bannerslider.ImageLoadingService;

public class PicassoImageLoadingService implements ImageLoadingService {
    public Context context;
  public PicassoImageLoadingService(Context context2) {
        this.context = context2;
    }
    public void loadImage(String url, ImageView imageView) {
        Picasso.get().load(url).fit().into(imageView);
    }
    public void loadImage(int resource, ImageView imageView) {
        Picasso.get().load(resource).fit().into(imageView);
        //  Picasso.load(resource).placeholder(R.drawable.looooder).error(R.drawable.looooder).into(imageView);
    }

    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        Picasso.get().load(url).placeholder(placeHolder).error(errorDrawable).into(imageView);
    }
}

package com.casinocards.appmagicTeenpatti.Utils;



import com.bumptech.glide.Glide;
import com.casinocards.appmagicTeenpatti.ApiClasses.Const;

import java.util.ArrayList;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {
  //  List<BannerArrayBean> bannerArrayBeans = null;
    ArrayList<com.casinocards.appmagicTeenpatti.model.BannerModel> bannerArrayBeans;
    private ArrayList<Integer> images;

   /* public MainSliderAdapter(List<BannerArrayBean> bannerArray, ArrayList<Integer> images) {
        this.bannerArrayBeans = bannerArray;
        this.images = images;
    }*/
    public MainSliderAdapter(ArrayList<com.casinocards.appmagicTeenpatti.model.BannerModel> bannerArray) {
        this.bannerArrayBeans = bannerArray;
    }

    public int getItemCount() {
        return this.bannerArrayBeans.size();
    }

    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {

        if(bannerArrayBeans!=null) {
         //   Glide.with(getApplicationContext()).load(Const.banner_img + bannerModelArrayList.get(position).getImg()).into(imageView);
         //   view.addView(imageLayout, 0);
            StringBuilder sb = new StringBuilder();
          //  sb.append(AppController.domainMain);
            sb.append(Const.banner_img +bannerArrayBeans.get(position).getImg());
            viewHolder.bindImageSlide(sb.toString());
        }
      /*  if(position>1){
            if(bannerArrayBeans!=null) {
                StringBuilder sb = new StringBuilder();
                sb.append(AppController.domainMain);
                sb.append(this.bannerArrayBeans.get(position).getBannerImage());
                viewHolder.bindImageSlide(sb.toString());
            }
        }else {
            viewHolder.bindImageSlide(bannerArrayBeans.get(position).getId());

        }*/

    }
}

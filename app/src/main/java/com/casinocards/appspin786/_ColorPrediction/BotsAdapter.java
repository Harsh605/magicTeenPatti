package com.casinocards.appmagicTeenpatti._ColorPrediction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.casinocards.appmagicTeenpatti.ApiClasses.Const;
import com.casinocards.appmagicTeenpatti.R;

import java.util.ArrayList;

public class BotsAdapter extends RecyclerView.Adapter<BotsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Bots_list> list;
    private int timeLeft = 20000; // Start with 20 seconds (in milliseconds)
    private boolean subtractTime = true; // Start with subtracting time

    public BotsAdapter(Context context, ArrayList<Bots_list> list) {
        this.context = context;
        this.list = list;
        startUpdatingCoins();
    }

    private void startUpdatingCoins() {
        updateCoins();
    }

    private void updateCoins() {
        // Check if time is greater than 0
        if (timeLeft > 0) {
            // Subtract time if subtractTime is true, else add time
            if (subtractTime) {
                timeLeft -= 10000; // Subtract 10 seconds (in milliseconds)
            } else {
                timeLeft -= 5000; // Subtract 5 seconds (in milliseconds) after the first 10 seconds
            }

            // Toggle subtractTime for next iteration
            subtractTime = !subtractTime;
        } else {
            // Reset time to 20 seconds
            timeLeft = 20000;
        }

        // Iterate through each bot and update the coin value
        for (Bots_list bot : list) {
            // Generate random coin value between 1000 and 5000
            int randomCoin = (int) (Math.random() * (5000 - 1000 + 1) + 1000);
            bot.setCoin(String.valueOf(randomCoin));
        }

        // Notify adapter that dataset has changed
        notifyDataSetChanged();

        // Schedule next update
        new android.os.Handler().postDelayed(this::updateCoins, subtractTime ? 8000 : 5000); // Call updateCoins() after 10 seconds initially, then 5 seconds
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem = layoutInflater.inflate(R.layout.item_bots, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Bots_list bots_list = list.get(position);

        holder.tv_name.setText(bots_list.getName());
        holder.txt_wallet.setText("₹ " + bots_list.getCoin());

        Glide.with(context).load(Const.IMGAE_PATH + bots_list.getAvatar()).into(holder.img_profile);
        // holder.img_profile.animate().rotationYBy(180);
    }

    @Override
    public int getItemCount() {
        if (list.size() > 3) {
            return 3;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, txt_wallet;
        ImageView img_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.txt_name);
            txt_wallet = itemView.findViewById(R.id.txt_wallet);
            img_profile = itemView.findViewById(R.id.img_profile);
        }
    }
}

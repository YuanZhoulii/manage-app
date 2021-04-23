package com.example.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.modules.ClubInfoActivity;
import com.example.app.pojo.ClubData;
import com.example.app.utils.HttpUtils;

public class Page1RvAdapter extends RecyclerView.Adapter<Page1RvAdapter.H> {

    private static final String TAG = "Page1RvAdapter";
    private ClubData data;
    private Context context;
    private Activity activity;

    public Page1RvAdapter(ClubData data) {
        this.data = data;
    }

    public Page1RvAdapter(ClubData data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.page1_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        ClubData.DataDTO d = data.getData().get(position);
        String url = HttpUtils.HOST + "app/upload/" + d.getCImg();
       // Log.e(TAG, "onBindViewHolder: " + url);
        try {
            Glide.with(context).load(url).into(holder.iv);
        } catch (Exception e) {
            e.printStackTrace();
            Glide.with(context).load(R.mipmap.noimage).into(holder.iv);
        }
        holder.tv.setText(d.getCName());
        holder.iv.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClubInfoActivity.class);
            intent.putExtra("data", d);
            activity.startActivityForResult(intent, 2);
        });
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.getData() != null ? data.getData().size() : 0;
    }

    class H extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv;

        public H(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}

package com.example.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.pojo.Page3Data;
import com.example.app.utils.AppConfig;

import java.util.List;

public class Page3RvAdapter extends RecyclerView.Adapter<Page3RvAdapter.H> {
    private List<Page3Data> data;
    private Context context;
    public Page3RvAdapter(List<Page3Data> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.page3_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        Page3Data d = data.get(position);
        Glide.with(context).load(d.getImage()).apply(AppConfig.options).into(holder.iv);
        holder.tvTitle.setText(d.getTitle());
        holder.tvSingTime.setText(d.getSingTime());
        holder.tvSingDate.setText("报名时间: " + d.getDate());
        holder.tvSingUP.setBackgroundResource(d.isClose() ? R.drawable.page3_item_open : R.drawable.page3_item_close);
        holder.tvSingUP.setText(d.isClose()?"点击报名":"已结束");
        holder.tvSingUP.setOnClickListener(v -> {
            if (d.isClose()) {
                Toast.makeText(context, "报名", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "活动已结束", Toast.LENGTH_SHORT).show();
            }
        });
        holder.ivPeople.setText(d.getPeople() + "人");
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class H extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tvTitle;

        private TextView tvSingTime;
        private TextView tvSingDate;
        private TextView tvSingUP;
        private TextView ivPeople;


        public H(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSingTime = itemView.findViewById(R.id.tv_singTime);
            tvSingDate = itemView.findViewById(R.id.tv_singDate);
            tvSingUP = itemView.findViewById(R.id.tv_singUP);
            ivPeople = itemView.findViewById(R.id.iv_people);

        }
    }
}

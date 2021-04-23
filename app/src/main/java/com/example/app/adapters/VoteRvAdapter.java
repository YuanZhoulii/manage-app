package com.example.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.pojo.Vote;
import com.example.app.utils.AppConfig;

import java.util.List;

public class VoteRvAdapter extends RecyclerView.Adapter<VoteRvAdapter.H> {
    private List<Vote.DataDTO> data;
    private Context context;
    private OnClick onClick;

    public VoteRvAdapter(List<Vote.DataDTO> data) {
        this.data = data;
    }

    public VoteRvAdapter(List<Vote.DataDTO> data, OnClick onClick) {
        this.data = data;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        Vote.DataDTO d = data.get(position);
        holder.tvName.setText("投票内容: \n" + d.getValue());
        refreshTv(holder, d);
        holder.pb1.setProgress(d.getYes());
        holder.pb2.setProgress(d.getNo());
        int anInt = AppConfig.spf.getInt(AppConfig.spf.getString("userId", "") + ":" + d.getId() + ":vote", 0);
        if (anInt == 0) {
            holder.pb1.setOnClickListener(v -> {
                holder.iv1.setVisibility(View.VISIBLE);
                holder.iv2.setVisibility(View.INVISIBLE);
                d.setYes(d.getYes() + 1);
                holder.pb1.setProgress(d.getYes());
                refreshTv(holder, d);
                onClick.onClick(true, d);
                holder.pb1.setClickable(false);
                holder.pb2.setClickable(false);
            });
            holder.pb2.setOnClickListener(v -> {
                holder.iv2.setVisibility(View.VISIBLE);
                holder.iv1.setVisibility(View.INVISIBLE);
                d.setNo(d.getNo() + 1);
                holder.pb2.setProgress(d.getNo());
                refreshTv(holder, d);
                onClick.onClick(false, d);
                holder.pb1.setClickable(false);
                holder.pb2.setClickable(false);
            });
        } else {
            holder.iv1.setVisibility(anInt == 1 ? View.VISIBLE : View.INVISIBLE);
            holder.iv2.setVisibility(anInt == 2 ? View.VISIBLE : View.INVISIBLE);
        }

    }

    private void refreshTv(H holder, Vote.DataDTO d) {
        int sum = d.getYes() + d.getNo();
        holder.pb1.setMax(sum == 0 ? 10 : sum);
        holder.pb2.setMax(sum == 0 ? 10 : sum);
        holder.tv1.setText(d.getYes() + " 票");
        holder.tv2.setText(d.getNo() + " 票");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class H extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ProgressBar pb1;
        private ImageView iv1;
        private TextView tv1;
        private ProgressBar pb2;
        private ImageView iv2;
        private TextView tv2;


        public H(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            pb1 = itemView.findViewById(R.id.pb1);
            iv1 = itemView.findViewById(R.id.iv1);
            tv1 = itemView.findViewById(R.id.tv1);
            pb2 = itemView.findViewById(R.id.pb2);
            iv2 = itemView.findViewById(R.id.iv2);
            tv2 = itemView.findViewById(R.id.tv2);
        }
    }

    public interface OnClick {
        void onClick(boolean flag, Vote.DataDTO d);
    }
}

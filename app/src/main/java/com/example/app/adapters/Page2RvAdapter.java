package com.example.app.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.MainActivity;
import com.example.app.R;
import com.example.app.modules.Page2InfoActivity;
import com.example.app.pojo.ClubData;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;

import java.util.List;

public class Page2RvAdapter extends RecyclerView.Adapter<Page2RvAdapter.H> {
    private List<ClubData.DataDTO> data;
    private MainActivity context;
    public Page2RvAdapter(List<ClubData.DataDTO> data, MainActivity context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.page2_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        ClubData.DataDTO d = data.get(position);
        Glide.with(context).load(HttpUtils.HOST + "app/upload/" + d.getCImg()).apply(AppConfig.options).into(holder.iv);
        holder.tvTitle.setText(d.getCName());
        holder.tvInfo.setText(d.getDeclaration());
        holder.ll.setOnClickListener(v -> {
            Intent intent = new Intent(context, Page2InfoActivity.class);
            intent.putExtra("data",d);
            context.startActivityForResult(intent,2);
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class H extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tvTitle;
        private TextView tvInfo;
        private LinearLayout ll;


        public H(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvInfo = itemView.findViewById(R.id.tv_info);
            ll = itemView.findViewById(R.id.ll);
        }
    }
}

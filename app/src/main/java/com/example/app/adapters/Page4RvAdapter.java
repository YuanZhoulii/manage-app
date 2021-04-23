package com.example.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.pojo.JoinData;

import java.util.List;

public class Page4RvAdapter extends RecyclerView.Adapter<Page4RvAdapter.H> {

    private Context context;
    List<JoinData.DataDTO> dto;
    onClick click;

    public Page4RvAdapter(List<JoinData.DataDTO> dto) {
        this.dto = dto;
    }

    public Page4RvAdapter(List<JoinData.DataDTO> dto, onClick click) {
        this.dto = dto;
        this.click = click;
    }

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.page4_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        JoinData.DataDTO d = dto.get(position);
        String s = "用户 " + d.getUserid() + "  申请加入  " + d.getClubName();
        holder.tvName.setText(s);
        holder.yes.setOnClickListener(v -> click.yes(this,d,position));
        holder.no.setOnClickListener(v -> click.no(d.getId()));
    }

    @Override
    public int getItemCount() {
        return dto.size();
    }

    public class H extends RecyclerView.ViewHolder {
        private LinearLayout ll;
        private TextView tvName;
        private ImageView yes;
        private ImageView no;


        public H(@NonNull View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.ll);
            tvName = itemView.findViewById(R.id.tv_name);
            yes = itemView.findViewById(R.id.yes);
            no = itemView.findViewById(R.id.no);

        }
    }

    public interface onClick {
        void yes(Page4RvAdapter p,JoinData.DataDTO d,int pos);

        void no(int id);
    }
}

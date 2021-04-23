package com.example.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.pojo.JoinActivityData;

import java.util.List;

public class JoinActivityRvAdapter extends RecyclerView.Adapter<JoinActivityRvAdapter.H> {

    List<JoinActivityData.DataDTO> dto;
    onClick click;

    public JoinActivityRvAdapter(List<JoinActivityData.DataDTO> dto) {
        this.dto = dto;
    }

    public JoinActivityRvAdapter(List<JoinActivityData.DataDTO> dto, onClick click) {
        this.dto = dto;
        this.click = click;
    }

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.page4_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        JoinActivityData.DataDTO d = dto.get(position);
        String s = "用户 " + d.getUserid() + " 申请参加 "+d.getTitle();
        holder.tvName.setText(s);
        holder.yes.setOnClickListener(v -> {
            click.click(true,this,d,position);
        });
        holder.no.setOnClickListener(v -> {
            click.click(false,this,d,position);
        });
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
        void click(boolean b,JoinActivityRvAdapter j,JoinActivityData.DataDTO d, int pos);
    }
}

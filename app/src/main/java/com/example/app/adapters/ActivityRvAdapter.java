package com.example.app.adapters;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.pojo.Activity;

public class ActivityRvAdapter extends RecyclerView.Adapter<ActivityRvAdapter.H> {
    private Activity activity;
    private OnClick onClick;


    public ActivityRvAdapter(Activity activity, OnClick onClick) {
        this.activity = activity;
        this.onClick = onClick;
    }


    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        Activity.DataDTO d = activity.getData().get(position);
        holder.tvName.setText(d.getTitle());
        holder.tvDate.setText("活动时间: " + d.getStartDate() + " ~ " + d.getEndDate());
        holder.tvContent.setText(d.getContent());
        holder.tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        holder.tvContent.setOnTouchListener((v, event) -> {
            //按下
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });
        if (d.getFlag() != null && d.getFlag().equals("Y")) {
            holder.btn.setText("已参加");
            holder.btn.setEnabled(false);
        }
        if (d.getFlag() == null) {
            holder.btn.setText("已申请");
            holder.btn.setEnabled(false);
        }
        holder.btn.setOnClickListener(v -> {
            onClick.onClick(holder.btn, d);
        });
    }


    @Override
    public int getItemCount() {
        return activity != null ? activity.getData().size() : 0;
    }

    public class H extends RecyclerView.ViewHolder {
        private LinearLayout ll;
        private TextView tvName;
        private TextView tvDate;
        private TextView tvContent;
        private Button btn;

        public H(@NonNull View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.ll);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvContent = itemView.findViewById(R.id.tv_content);
            btn = itemView.findViewById(R.id.btn);
        }
    }

    public interface OnClick {
        void onClick(Button button, Activity.DataDTO data);
    }
}

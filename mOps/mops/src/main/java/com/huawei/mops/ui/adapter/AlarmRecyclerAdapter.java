package com.huawei.mops.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.WarningInfoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tWX366549 on 2016/10/12.
 */
public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.AlarmViewHolder> {
    private List<WarningInfoEntity> datas;
    private Context context;
    private LayoutInflater inflater;

    private String Y;
    private String N;
    private OnItemClickListener onItemClickListener;

    public AlarmRecyclerAdapter(List<WarningInfoEntity> datas, Context context) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        N = context.getResources().getString(R.string.alarm_no_clear);
        Y = context.getResources().getString(R.string.alarm_already_clear);
        this.datas = datas;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 为每个item inflate一个view
     *
     * @param parent
     * @param viewType
     * @return 该方法返回一个ViewHolder, 所以我们需要定义一个RecyclerView.ViewHolder
     */
    @Override
    public AlarmRecyclerAdapter.AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.alarm_item, parent, false);
        return new AlarmViewHolder(view);
    }

    /**
     * 渲染数据到view中
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(AlarmRecyclerAdapter.AlarmViewHolder holder, final int position) {
        final WarningInfoEntity data = datas.get(position);
        holder.alarm_item_last_date.setText(data.getLastTime());
        holder.alarm_item_title.setText(data.getWarningName());
        holder.alarm_item_desc.setText(data.getWarningDesc());
        boolean isCleared = data.getStatus().equals("Y");
        if (isCleared) {
            holder.alarm_item_is_clear.setTextColor(Color.parseColor("#FF3DDA19"));
        } else {
            holder.alarm_item_is_clear.setTextColor(Color.parseColor("#FFFF6160"));
        }
        holder.alarm_item_is_clear.setText(isCleared ? Y : N);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(v, data, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, WarningInfoEntity entity, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.alarm_item_title)
        TextView alarm_item_title;
        @Bind(R.id.alarm_item_desc)
        TextView alarm_item_desc;
        @Bind(R.id.alarm_item_is_clear)
        TextView alarm_item_is_clear;
        @Bind(R.id.alarm_item_last_date)
        TextView alarm_item_last_date;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

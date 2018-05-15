package com.huawei.mops.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huawei.mops.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tWX366549 on 2016/11/7.
 */
public class BreakdownAdapter extends RecyclerView.Adapter<BreakdownAdapter.BreakdownViewHolder> {

    private Context context;

    @Override
    public BreakdownAdapter.BreakdownViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announce_list_item, null);
        return new BreakdownViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BreakdownAdapter.BreakdownViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class BreakdownViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.announce_item_title)
        TextView announce_item_title;
        @Bind(R.id.announce_item_desc)
        TextView announce_item_desc;
        @Bind(R.id.announce_item_date)
        TextView announce_item_date;

        public BreakdownViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

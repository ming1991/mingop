package com.huawei.mops.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.RegionInfo;
import com.huawei.mops.bean.SolutionTopicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tWX366549 on 2016/10/19.
 */
public class ShowCaseFilterAdapter extends BaseAdapter {

    public static final int TYPE_REGION = 0;
    public static final int TYPE_CSIC = 1;
    public static final int TYPE_SOLUTION_TOPIC = 2;

    private int type;

    private Context mContext;
    private List<CSICInfo> csicInfos;
    private List<SolutionTopicInfo> solutionTopicInfos;
    private List<RegionInfo> regionInfos;
    private LayoutInflater inflater;

    public ShowCaseFilterAdapter(Context context) {
        if (csicInfos == null) csicInfos = new ArrayList<>();
        if (regionInfos == null) regionInfos = new ArrayList<>();
        if (solutionTopicInfos == null) solutionTopicInfos = new ArrayList<>();
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getType() {
        return type;
    }

    public void setRegionInfos(List<RegionInfo> newDatas, int type) {
        if (regionInfos != null) {
//            regionInfos.clear();
        }
        if (newDatas == null) {
            regionInfos = new ArrayList<>();
        }
        this.type = type;
        regionInfos = newDatas;
    }

    public void setCsicInfos(List<CSICInfo> newDatas, int type) {
        if (csicInfos != null) {
//            regionInfos.clear();
        }
        if (newDatas == null) {
            csicInfos = new ArrayList<>();
        }
        this.type = type;
        csicInfos = newDatas;
    }

    public void setSolutionTopicInfos(List<SolutionTopicInfo> newDatas, int type) {
        if (solutionTopicInfos != null) {
//            regionInfos.clear();
        }
        if (newDatas == null) {
            solutionTopicInfos = new ArrayList<>();
        }
        this.type = type;
        solutionTopicInfos = newDatas;
    }


    @Override
    public int getCount() {
        int count = 0;
        switch (type) {
            case TYPE_REGION:
                count = regionInfos.size();
                break;
            case TYPE_CSIC:
                count = csicInfos.size();
                break;
            case TYPE_SOLUTION_TOPIC:
                count = solutionTopicInfos.size();
                break;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object obj = null;
        switch (type) {
            case TYPE_REGION:
                obj = regionInfos.get(position);
                break;
            case TYPE_CSIC:
                obj = csicInfos.get(position);
                break;
            case TYPE_SOLUTION_TOPIC:
                obj = solutionTopicInfos.get(position);
                break;
        }
        return obj;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.filter_layout_item, null);
            holder.content = (TextView) convertView.findViewById(R.id.filter_item_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case TYPE_REGION:
                if (regionInfos.get(position).getId() == 0) {
                    holder.content.setTextColor(mContext.getResources().getColor(R.color.filter_text_unchecked));
                } else {
                    holder.content.setTextColor(Color.BLACK);
                }
                holder.content.setText(regionInfos.get(position).getRegionName());
                break;
            case TYPE_CSIC:
                if (csicInfos.get(position).getId() == 0) {
                    holder.content.setTextColor(mContext.getResources().getColor(R.color.filter_text_unchecked));
                } else {
                    holder.content.setTextColor(Color.BLACK);
                }
                holder.content.setText(csicInfos.get(position).getChName());
                break;
            case TYPE_SOLUTION_TOPIC:
                if (solutionTopicInfos.get(position).getId() == 0) {
                    holder.content.setTextColor(mContext.getResources().getColor(R.color.filter_text_unchecked));
                } else {
                    holder.content.setTextColor(Color.BLACK);
                }
                holder.content.setText(solutionTopicInfos.get(position).getTopicName());
                break;
        }


        return convertView;
    }

    class ViewHolder {
        TextView content;
    }
}

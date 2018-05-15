package com.huawei.mops.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.AlarmFieldInfo;
import com.huawei.mops.bean.AlarmLevelInfo;
import com.huawei.mops.bean.AlarmStatusInfo;
import com.huawei.mops.bean.CSICInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tWX366549 on 2016/10/19.
 */
public class AlarmFilterAdapter extends BaseAdapter {

    public static final int TYPE_CSIC = 0;
    public static final int TYPE_FIELD = 1;
    public static final int TYPE_LEVEL = 2;
    public static final int TYPE_STATUS = 3;

    private int type;

    private Context mContext;
    private List<CSICInfo> csicInfos;
    private List<AlarmLevelInfo> levelInfos;
    private List<AlarmStatusInfo> statusInfos;
    private List<AlarmFieldInfo> fieldInfos;
    private LayoutInflater inflater;

    public AlarmFilterAdapter(Context context) {
        mContext = context;
        if (csicInfos == null) csicInfos = new ArrayList<>();
        if (levelInfos == null) levelInfos = new ArrayList<>();
        if (statusInfos == null) statusInfos = new ArrayList<>();
        if (fieldInfos == null) fieldInfos = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getType() {
        return type;
    }

    public void setWarningStatus(List<AlarmStatusInfo> newDatas, int type) {
        if (newDatas == null) {
            statusInfos = new ArrayList<>();
        }
        this.type = type;
        statusInfos = newDatas;
    }

    public void setCsicInfos(List<CSICInfo> newDatas, int type) {
        if (newDatas == null) {
            csicInfos = new ArrayList<>();
        }
        this.type = type;
        csicInfos = newDatas;
    }

    public void setWarningLevel(List<AlarmLevelInfo> newDatas, int type) {
        if (newDatas == null) {
            levelInfos = new ArrayList<>();
        }
        this.type = type;
        levelInfos = newDatas;
    }

    public void setWarningField(List<AlarmFieldInfo> newDatas, int type) {
        if (newDatas == null) {
            fieldInfos = new ArrayList<>();
        }
        this.type = type;
        fieldInfos = newDatas;
    }


    @Override
    public int getCount() {
        int count = 0;
        switch (type) {
            case TYPE_FIELD:
                count = fieldInfos.size();
                break;
            case TYPE_CSIC:
                count = csicInfos.size();
                break;
            case TYPE_LEVEL:
                count = levelInfos.size();
                break;
            case TYPE_STATUS:
                count = statusInfos.size();
                break;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object obj = null;
        switch (type) {
            case TYPE_FIELD:
                obj = fieldInfos.get(position);
                break;
            case TYPE_CSIC:
                obj = csicInfos.get(position);
                break;
            case TYPE_LEVEL:
                obj = levelInfos.get(position);
                break;
            case TYPE_STATUS:
                obj = statusInfos.get(position);
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
            case TYPE_FIELD:
                if (fieldInfos.get(position).getId() == 0) {
                    holder.content.setTextColor(mContext.getResources().getColor(R.color.filter_text_unchecked));
                } else {
                    holder.content.setTextColor(Color.BLACK);
                }
                holder.content.setText(fieldInfos.get(position).getTitle());
                break;
            case TYPE_CSIC:
                if (csicInfos.get(position).getId() == 0) {
                    holder.content.setTextColor(mContext.getResources().getColor(R.color.filter_text_unchecked));
                } else {
                    holder.content.setTextColor(Color.BLACK);
                }
                holder.content.setText(csicInfos.get(position).getChName());
                break;
            case TYPE_LEVEL:
                if (levelInfos.get(position).getId() == 0) {
                    holder.content.setTextColor(mContext.getResources().getColor(R.color.filter_text_unchecked));
                } else {
                    holder.content.setTextColor(Color.BLACK);
                }
                holder.content.setText(levelInfos.get(position).getTitle());
                break;
            case TYPE_STATUS:
                if (statusInfos.get(position).getId() == 0) {
                    holder.content.setTextColor(mContext.getResources().getColor(R.color.filter_text_unchecked));
                } else {
                    holder.content.setTextColor(Color.BLACK);
                }
                holder.content.setText(statusInfos.get(position).getTitle());
                break;
        }
        return convertView;
    }

    class ViewHolder {
        TextView content;
    }
}

package com.huawei.mops.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.ShowCaseEntity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tWX366549 on 2016/10/12.
 */
public class ShowCaseRecyclerAdapter extends RecyclerView.Adapter<ShowCaseRecyclerAdapter.InspactionViewHolder> {

    public static final int NORMAL = 1000;
    public static final int SLIDE = 2000;
    private final TranslateAnimation openAnim;
    private final TranslateAnimation closeAnim;
    private int mState = NORMAL;

    private List<ShowCaseEntity> datas;
    private Context context;
    private LayoutInflater inflater;
    private List<Integer> checkPositionlist;

    private List<ShowCaseEntity> checkedDatas = new ArrayList<>();

    private List<InspactionViewHolder> inspactionViewHolders = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    private OnItemSelectedChangedListener onItemSelectedChangedListener;

    private static boolean isBoxOpened = false;

    public ShowCaseRecyclerAdapter(List<ShowCaseEntity> datas, Context context) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        this.datas = datas;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkPositionlist = new ArrayList<>();

        openAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        openAnim.setDuration(200);

        closeAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        closeAnim.setDuration(200);


    }

    public void openBox() {
        mState = SLIDE;
        for (InspactionViewHolder holder : inspactionViewHolders) {
            holder.open();
        }
        Logger.d("open box");
        notifyDataSetChanged();
        isBoxOpened = true;
    }

    public void closeBox() {
        mState = NORMAL;
        for (InspactionViewHolder holder : inspactionViewHolders) {
            holder.close();
        }
        notifyDataSetChanged();
        Logger.d("close box");
        isBoxOpened = false;
    }

    public boolean isBoxOpened() {
        return isBoxOpened;
    }

    @Override
    public ShowCaseRecyclerAdapter.InspactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.inspaction_item, parent, false);
        InspactionViewHolder holder = new InspactionViewHolder(view);
//        inspactionViewHolders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ShowCaseRecyclerAdapter.InspactionViewHolder holder, final int position) {
//        holder.bind(datas.get(position), position);
        Integer tag = new Integer(position);
        holder.inspaction_cb.setTag(tag);//设置tag 否则划回来时选中消失
        //checkbox  复用问题
        if (checkPositionlist != null) {
            holder.inspaction_cb.setChecked((checkPositionlist.contains(tag) ? true : false));
        } else {
            holder.inspaction_cb.setChecked(false);
        }

        onchecked(holder, position);
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ShowCaseEntity entity, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemSelectedChangedListener {
        void onAllItemSelected(int count);

        void onItemSelected(int position, ShowCaseEntity itemBean);

        void onItemUnSelected(int position);
    }

    public void setOnItemSelectedChangedListener(OnItemSelectedChangedListener listener) {
        this.onItemSelectedChangedListener = listener;
    }

    public void selectAll() {
        checkedDatas.clear();
        checkPositionlist.clear();
//        for (ItemBean bean : datas) {
//            bean.setChecked(true);
//        }
        for (int i = 0; i < datas.size(); i++) {
            checkPositionlist.add(new Integer(i));
            checkedDatas.add(datas.get(i));
        }
        notifyDataSetChanged();
    }

    public void selectNone() {
        checkPositionlist.clear();
        checkedDatas.clear();
        notifyDataSetChanged();
    }

    public List<ShowCaseEntity> getCheckedDatas() {
        return checkedDatas;
    }

    public int getHolderSize() {
        return inspactionViewHolders.size();
    }

    class InspactionViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.inspaction_swip_layout)
        RelativeLayout contentView;
        @Bind(R.id.inspaction_checkbox_parent)
        RelativeLayout checkBoxParent;
        @Bind(R.id.inspaction_item_title)
        TextView inspaction_item_title;
        @Bind(R.id.inspaction_item_desc)
        TextView inspaction_item_desc;
        @Bind(R.id.inspaction_item_is_clear)
        TextView inspaction_item_is_clear;
        @Bind(R.id.inspaction_item_last_date)
        TextView inspaction_item_last_date;
        @Bind(R.id.inspaction_checkbox)
        CheckBox inspaction_cb;

        public void open() {
//            inspaction_cb.startAnimation(openAnim);
            checkBoxParent.setVisibility(View.VISIBLE);
        }

        public void close() {
//            inspaction_cb.startAnimation(closeAnim);
            checkBoxParent.setVisibility(View.GONE);
        }

        public InspactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


//        public void bind(ItemBean itemBean, final int position) {
//            mItemBean = itemBean;
//            switch (mState) {
//                case NORMAL:
//                    close();
//                    mItemBean.setChecked(false);
//                    break;
//                case SLIDE:
//                    open();
//                    break;
//            }
//            inspaction_cb.setChecked(itemBean.isChecked());
//            inspaction_item_last_date.setText(itemBean.getContent());
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mState == NORMAL) {
//                        if (onItemClickListener != null)
//                            onItemClickListener.onItemClick(v, position);
//                    } else {
//                        setCheckBox();
//                    }
//                }
//            });
//
//            inspaction_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    inspaction_cb.setChecked(isChecked);
//                    mItemBean.setChecked(isChecked);
//                    if (isChecked) {
//                        checkedDatas.add(mItemBean);
//                        checkPositionlist.add(new Integer(position));
//
//                        if (onItemSelectedChangedListener != null) {
//                            onItemSelectedChangedListener.onItemSelected(position, mItemBean);
//                            if (checkedDatas.size() == datas.size()) {
//                                onItemSelectedChangedListener.onAllItemSelected(datas.size());
//                            }
//                        }
//                    } else {
//                        checkedDatas.remove(mItemBean);
//                        if (onItemSelectedChangedListener != null) {
//                            onItemSelectedChangedListener.onItemUnSelected(position);
//                        }
//                        checkPositionlist.remove(new Integer(position));
//                    }
//                }
//            });
//        }

//        public void setCheckBox() {
//            inspaction_cb.setChecked(!inspaction_cb.isChecked());
//        }
    }

    private void onchecked(final InspactionViewHolder viewHolder, final int position) {
        final ShowCaseEntity entity = datas.get(position);
        viewHolder.inspaction_item_title.setText(entity.getNameCh());
        viewHolder.inspaction_item_last_date.setText(entity.getLastCheckTime());
        boolean status = entity.getStatus().equals(context.getResources().getString(R.string.normal));

        if (status) {
            viewHolder.inspaction_item_is_clear.setTextColor(Color.parseColor("#FF3DDA19"));
        } else {
            viewHolder.inspaction_item_is_clear.setTextColor(Color.parseColor("#FFFF6160"));
        }

        viewHolder.inspaction_item_is_clear.setText(entity.getStatus());
        viewHolder.inspaction_item_desc.setText(entity.getDescriptionCh());
        switch (mState) {
            case NORMAL:
                viewHolder.close();
                break;
            case SLIDE:
                viewHolder.open();
                break;
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState == NORMAL) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, entity, position);
                } else {
                    viewHolder.inspaction_cb.setChecked(!viewHolder.inspaction_cb.isChecked());
                }
            }
        });

        viewHolder.inspaction_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ShowCaseEntity itemBean = datas.get(position);
                if (isChecked) {
                    if (!checkPositionlist.contains(viewHolder.inspaction_cb.getTag())) {
                        checkedDatas.add(itemBean);
                        checkPositionlist.add(new Integer(position));
                        if (onItemSelectedChangedListener != null) {
                            onItemSelectedChangedListener.onItemSelected(position, itemBean);
                            if (checkedDatas.size() == datas.size()) {
                                onItemSelectedChangedListener.onAllItemSelected(datas.size());
                            }
                        }
                    }
                } else {
                    if (checkPositionlist.contains(viewHolder.inspaction_cb.getTag())) {
                        checkedDatas.remove(itemBean);
                        checkPositionlist.remove(new Integer(position));
                        if (onItemSelectedChangedListener != null) {
                            onItemSelectedChangedListener.onItemUnSelected(position);
                        }
                    }
                }
            }
        });
    }
}

package com.huawei.mops.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.bean.ShowCaseEntity;
import com.huawei.mops.ui.announce.AnnounceListActivity;
import com.huawei.mops.ui.announce.AnnounceManageActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.http.POST;

/**
 * Created by tWX366549 on 2016/10/12.
 */
public class AnnounceRecyclerAdapter extends RecyclerView.Adapter<AnnounceRecyclerAdapter.AnnounceViewHolder> {

    public static final int UPDATE_MENU_ID = 1000;
    public static final int PUBLISH_MENU_ID = 1001;
    public static final int DELETE_MENU_ID = 1002;
    public static final int COPY_MENU_ID = 1003;

    public static final int NORMAL = 1000;
    public static final int SLIDE = 2000;
    private TranslateAnimation openAnim;
    private TranslateAnimation closeAnim;
    private int mState = NORMAL;

    private List<NoticeBreakdownEntity> breakdownEntities;
    private List<NoticeWarningEntity> warningEntities;
    private List<NoticeCsicUpdateEntity> csicUpdateEntities;
    private List<NoticePlatformUpdateEntity> platformUpdateEntities;

    private List<NoticeEmailEntity> emailEntities;

    private Context context;
    private LayoutInflater inflater;
    private List<Integer> checkPositionlist;

    private ArrayList<Object> checkedDatas = new ArrayList<>();

    private List<AnnounceViewHolder> announceViewHolders = new ArrayList<>();

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener longClickListener;
    private OnItemSelectedChangedListener onItemSelectedChangedListener;

    private static boolean isBoxOpened = false;

    private int flag;
    private BaseNoticeEntity entity;

    private String[] menuTitle;

    private void initAnim(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkPositionlist = new ArrayList<>();
        this.context = context;
        openAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        openAnim.setDuration(200);

        closeAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        closeAnim.setDuration(200);
        menuTitle = context.getResources().getStringArray(R.array.announce_menu);
    }

    public AnnounceRecyclerAdapter(Context context) {
        initAnim(context);
    }

    public void setBreakdownEntities(List<NoticeBreakdownEntity> breakdownEntities, int flag) {
        this.breakdownEntities = breakdownEntities;
        this.flag = flag;
    }

    public void setWarningEntities(List<NoticeWarningEntity> warningEntities, int flag) {
        this.warningEntities = warningEntities;
        this.flag = flag;
    }

    public void setCsicUpdateEntities(List<NoticeCsicUpdateEntity> csicUpdateEntities, int flag) {
        this.csicUpdateEntities = csicUpdateEntities;
        this.flag = flag;
    }

    public void setPlatformUpdateEntities(List<NoticePlatformUpdateEntity> platformUpdateEntities, int flag) {
        this.platformUpdateEntities = platformUpdateEntities;
        this.flag = flag;
    }

    public void seteEmailEntities(List<NoticeEmailEntity> emailEntities, int flag) {
        this.emailEntities = emailEntities;
        this.flag = flag;
    }

    public void openBox() {
        mState = SLIDE;
        for (AnnounceViewHolder holder : announceViewHolders) {
            holder.open();
        }
        Logger.d("open box");
        notifyDataSetChanged();
        isBoxOpened = true;
    }

    public void closeBox() {
        mState = NORMAL;
        for (AnnounceViewHolder holder : announceViewHolders) {
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
    public AnnounceRecyclerAdapter.AnnounceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.announce_list_item, parent, false);
        AnnounceViewHolder holder = new AnnounceViewHolder(view);
//        inspactionViewHolders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AnnounceRecyclerAdapter.AnnounceViewHolder holder, final int position) {
//        holder.bind(datas.get(position), position);
        Integer tag = new Integer(position);
        holder.announce_checkbox.setTag(tag);//设置tag 否则划回来时选中消失
        //checkbox  复用问题
        if (checkPositionlist != null) {
            holder.announce_checkbox.setChecked((checkPositionlist.contains(tag) ? true : false));
        } else {
            holder.announce_checkbox.setChecked(false);
        }

        onchecked(holder, position);
    }


    @Override
    public int getItemCount() {
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                return csicUpdateEntities.size();
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                return breakdownEntities.size();
            case AnnounceManageActivity.WARNING_FLAG:
                return warningEntities.size();
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                return platformUpdateEntities.size();
            case AnnounceManageActivity.EMAIL_FLAG:
                return emailEntities.size();
        }
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public interface OnItemSelectedChangedListener {
        void onAllItemSelected(int count);

        void onItemSelected(int position);

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

        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                for (int i = 0; i < csicUpdateEntities.size(); i++) {
                    checkPositionlist.add(new Integer(i));
                    checkedDatas.add(csicUpdateEntities.get(i));
                }
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                for (int i = 0; i < breakdownEntities.size(); i++) {
                    checkPositionlist.add(new Integer(i));
                    checkedDatas.add(breakdownEntities.get(i));
                }
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                for (int i = 0; i < warningEntities.size(); i++) {
                    checkPositionlist.add(new Integer(i));
                    checkedDatas.add(warningEntities.get(i));
                }
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                for (int i = 0; i < platformUpdateEntities.size(); i++) {
                    checkPositionlist.add(new Integer(i));
                    checkedDatas.add(platformUpdateEntities.get(i));
                }
                break;
            case AnnounceManageActivity.EMAIL_FLAG:
                for (int i = 0; i < emailEntities.size(); i++) {
                    checkPositionlist.add(new Integer(i));
                    checkedDatas.add(emailEntities.get(i));
                }
                break;
        }

        notifyDataSetChanged();
    }

    public void selectNone() {
        checkPositionlist.clear();
        checkedDatas.clear();
        notifyDataSetChanged();
    }

    public int getSelectedDataSize() {
        return checkedDatas.size();
    }

    public ArrayList<Object> getSelectedData() {
        return checkedDatas;
    }

    public int getHolderSize() {
        return announceViewHolders.size();
    }

    class AnnounceViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        @Bind(R.id.announce_item_right)
        RelativeLayout checkBoxParent;
        @Bind(R.id.announce_item_title)
        TextView announce_item_title;
        @Bind(R.id.announce_item_desc)
        TextView announce_item_desc;
        @Bind(R.id.announce_item_date)
        TextView announce_item_date;
        @Bind(R.id.announce_checkbox)
        CheckBox announce_checkbox;

        public void open() {
//            inspaction_cb.startAnimation(openAnim);
            checkBoxParent.setVisibility(View.VISIBLE);
        }

        public void close() {
//            inspaction_cb.startAnimation(closeAnim);
            checkBoxParent.setVisibility(View.GONE);
        }

        public AnnounceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Log.e("huawei", "create menu");
            menu.add(Menu.NONE, UPDATE_MENU_ID, Menu.NONE, context.getResources().getString(R.string.update));

            menu.add(Menu.NONE, DELETE_MENU_ID, Menu.NONE, context.getResources().getString(R.string.delete));

            if (flag != AnnounceManageActivity.EMAIL_FLAG) {
                menu.add(Menu.NONE, COPY_MENU_ID, Menu.NONE, context.getResources().getString(R.string.copy));
                menu.add(Menu.NONE, PUBLISH_MENU_ID, Menu.NONE, context.getResources().getString(R.string.publish));

            }

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

    private void onchecked(final AnnounceViewHolder viewHolder, final int position) {
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                entity = csicUpdateEntities.get(position);
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                entity = breakdownEntities.get(position);
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                entity = warningEntities.get(position);
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                entity = platformUpdateEntities.get(position);
                break;
        }

        if (flag != AnnounceManageActivity.EMAIL_FLAG) {
            viewHolder.announce_item_title.setText(entity.getNoticeTitle());
            viewHolder.announce_item_desc.setText(entity.getNoticeContent());
            if (entity.getIsSend().equals("N")) {
                viewHolder.announce_item_date.setTextColor(Color.parseColor("#FFFF6160"));
                viewHolder.announce_item_date.setText("未发布");
            } else {
                viewHolder.announce_item_date.setTextColor(Color.parseColor("#FF909090"));
                viewHolder.announce_item_date.setText(entity.getCreateTime());
            }
        } else {
            NoticeEmailEntity noticeEmailEntity = emailEntities.get(position);
            viewHolder.announce_item_title.setText(noticeEmailEntity.getGroupName());
            viewHolder.announce_item_desc.setText(noticeEmailEntity.getSendToPerson());
        }

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
                        onItemClickListener.onItemClick(v, position);
                } else {
                    viewHolder.announce_checkbox.setChecked(!viewHolder.announce_checkbox.isChecked());
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                pressedData = entity;

                setPosition(position);
//                if (longClickListener != null)
//                    longClickListener.onItemLongClick(v, position);
                return false;
            }
        });

        viewHolder.announce_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BaseNoticeEntity itemBean = null;
                switch (flag) {
                    case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                        itemBean = csicUpdateEntities.get(position);
                        break;
                    case AnnounceManageActivity.BREAKDOWN_FLAG:
                        itemBean = breakdownEntities.get(position);
                        break;
                    case AnnounceManageActivity.WARNING_FLAG:
                        itemBean = warningEntities.get(position);
                        break;
                    case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                        itemBean = platformUpdateEntities.get(position);
                        break;
                    case AnnounceManageActivity.EMAIL_FLAG:
                        itemBean = emailEntities.get(position);
                }
                if (isChecked) {
                    if (!checkPositionlist.contains(viewHolder.announce_checkbox.getTag())) {
                        checkedDatas.add(itemBean);
                        checkPositionlist.add(new Integer(position));
                        if (onItemSelectedChangedListener != null) {
                            onItemSelectedChangedListener.onItemSelected(position);
                            int size = 0;
                            switch (flag) {
                                case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                                    size = csicUpdateEntities.size();
                                    break;
                                case AnnounceManageActivity.BREAKDOWN_FLAG:
                                    size = breakdownEntities.size();
                                    break;
                                case AnnounceManageActivity.WARNING_FLAG:
                                    size = warningEntities.size();
                                    break;
                                case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                                    size = platformUpdateEntities.size();
                                    break;
                                case AnnounceManageActivity.EMAIL_FLAG:
                                    size = emailEntities.size();
                                    break;
                            }
                            if (checkedDatas.size() == size) {
                                onItemSelectedChangedListener.onAllItemSelected(size);
                            }
                        }
                    }
                } else {
                    if (checkPositionlist.contains(viewHolder.announce_checkbox.getTag())) {
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

    private int position;

    private BaseNoticeEntity pressedData;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public BaseNoticeEntity getLongPressedData() {
        return pressedData;
    }
}

package com.huawei.mops.ui.announce.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.ui.announce.AnnounceManageActivity;
import com.huawei.mops.util.Constants;
import com.huawei.mops.util.DisplayUtil;
import com.huawei.mops.util.StringUtil;
import com.huawei.mops.weight.ablistview.CommonAdapter;
import com.huawei.mops.weight.ablistview.ViewHolder;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import rx.functions.Action1;

public class PlatformUpdatePublishFragment extends BaseAnnounceFragment {


    @Bind(R.id.module_checkbox)
    CheckBox module_checkbox;
    @Bind(R.id.module_checkbox_layout)
    LinearLayout module_checkbox_layout;
    //    @Bind(R.id.region_checkbox)
//    CheckBox region_checkbox;
//    @Bind(R.id.csic_checkbox)
//    CheckBox csic_checkbox;
//    @Bind(R.id.csic_checkbox_layout)
//    LinearLayout csic_checkbox_layout;
    @Bind(R.id.email_receiver_checkbox)
    CheckBox email_receiver_checkbox;
    @Bind(R.id.email_receiver_checkbox_layout)
    LinearLayout email_receiver_checkbox_layout;
    @Bind(R.id.email_cc_checkbox)
    CheckBox email_cc_checkbox;
    @Bind(R.id.email_cc_checkbox_layout)
    LinearLayout email_cc_checkbox_layout;

    @Bind(R.id.platform_update_pl_title)
    EditText platform_update_pl_title;
    @Bind(R.id.platform_update_pl_desc_layout)
    LinearLayout platform_update_pl_desc_layout;
    @Bind(R.id.platform_update_pl_content)
    EditText platform_update_pl_content;
    @Bind(R.id.platform_update_pl_time_layout)
    LinearLayout platform_update_pl_reason_layout;
    @Bind(R.id.platform_update_pl_time)
    EditText platform_update_pl_time;
    @Bind(R.id.platform_update_pl_detail)
    EditText platform_update_pl_detail;
    @Bind(R.id.platform_update_pl_influence)
    EditText platform_update_pl_influence;
    @Bind(R.id.platform_update_pl_influence_range_layout)
    LinearLayout platform_update_pl_influence_range_layout;
    @Bind(R.id.platform_update_pl_influence_range)
    EditText platform_update_pl_influence_range;
    @Bind(R.id.platform_update_pl_charge_person)
    EditText platform_update_pl_charge_person;
    @Bind(R.id.platform_update_pl_email_receiver_layout)
    LinearLayout platform_update_pl_email_receiver_layout;
    @Bind(R.id.platform_update_pl_email_receiver)
    EditText platform_update_pl_email_receiver;
    @Bind(R.id.platform_update_pl_email_ccer_layout)
    LinearLayout platform_update_pl_email_ccer_layout;
    @Bind(R.id.platform_update_pl_email_ccer)
    EditText platform_update_pl_email_ccer;

    @Bind(R.id.platform_update_title_$)
    TextView title;
    @Bind(R.id.platform_update_receiver_$)
    TextView receiver;

    private ArrayList<NoticePlatformUpdateEntity> platformUpdateEntities;
    private HashMap<String, Object> emailAndCsic;
    private NoticePlatformUpdateEntity platformUpdateEntity;

    private ArrayList<NoticeEmailEntity> recEmails;
    private ArrayList<NoticeEmailEntity> ccEmails;

    private int selectedRegionId = 0;
    private NoticeEmailEntity selectedRecEmailGroup;
    private NoticeEmailEntity selectedCcEmailGroup;

    private CheckBox box;
    private Context mContext;
    private int currentPopup;
    private ListView popupListView;
    private PopupWindow popupWindow;

    private List<CheckBox> checkBoxes = new ArrayList<>();

    public PlatformUpdatePublishFragment() {
    }

    public static PlatformUpdatePublishFragment newInstance(ArrayList<NoticePlatformUpdateEntity> param1, HashMap<String, Object> emailAndCsic) {
        PlatformUpdatePublishFragment fragment = new PlatformUpdatePublishFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, emailAndCsic);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        if (getArguments() != null) {
            platformUpdateEntities = (ArrayList<NoticePlatformUpdateEntity>) getArguments().getSerializable(ARG_PARAM1);
            emailAndCsic = (HashMap<String, Object>) getArguments().getSerializable(ARG_PARAM2);
            recEmails = (ArrayList<NoticeEmailEntity>) emailAndCsic.get("email");
            ccEmails = recEmails;
            setDefaultInfo();
        }
    }

    private void setDefaultInfo() {
        NoticePlatformUpdateEntity noticePlatformUpdateEntity = new NoticePlatformUpdateEntity();
        noticePlatformUpdateEntity.setNoticeTitle(mContext.getResources().getString(R.string.no_data));
        platformUpdateEntities.add(0, noticePlatformUpdateEntity);
        NoticeEmailEntity defaultRecEmail = new NoticeEmailEntity();
        defaultRecEmail.setGroupName(mContext.getResources().getString(R.string.no_data));
        recEmails.add(0, defaultRecEmail);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_platform_update_publish, container, false);
    }

    @Override
    public void initUIAndListener(View view) {

        initView();

        RxCompoundButton.checkedChanges(module_checkbox).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean && currentPopup != Constants.POPUP_MODULE) {
                    box = module_checkbox;
                    currentPopup = Constants.POPUP_MODULE;
                    popupListView.setAdapter(new CommonAdapter<NoticePlatformUpdateEntity>(getContext(), R.layout.popup_list_item_layout, platformUpdateEntities) {
                        @Override
                        protected void convert(ViewHolder viewHolder, NoticePlatformUpdateEntity item, int position) {
                            viewHolder.setText(R.id.popup_list_item_tv, item.getNoticeTitle());
                        }
                    });
                }
                showOrDismissWindow(module_checkbox_layout, aBoolean);
                if (!checkBoxes.contains(module_checkbox)) {
                    checkBoxes.add(module_checkbox);
                }
            }
        });


        RxCompoundButton.checkedChanges(email_receiver_checkbox).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean && currentPopup != Constants.POPUP_EMAIL_RECEIVER) {
                    box = email_receiver_checkbox;
                    currentPopup = Constants.POPUP_EMAIL_RECEIVER;
                    popupListView.setAdapter(new CommonAdapter<NoticeEmailEntity>(getContext(), R.layout.popup_list_item_layout, recEmails) {
                        @Override
                        protected void convert(ViewHolder viewHolder, NoticeEmailEntity item, int position) {
                            viewHolder.setText(R.id.popup_list_item_tv, item.getGroupName());
                        }
                    });
                }
                showOrDismissWindow(email_receiver_checkbox_layout, aBoolean);
                if (!checkBoxes.contains(email_receiver_checkbox)) {
                    checkBoxes.add(email_receiver_checkbox);
                }
            }
        });

        RxCompoundButton.checkedChanges(email_cc_checkbox).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean && currentPopup != Constants.POPUP_EMAIL_CC) {
                    box = email_cc_checkbox;
                    currentPopup = Constants.POPUP_EMAIL_CC;
                    popupListView.setAdapter(new CommonAdapter<NoticeEmailEntity>(getContext(), R.layout.popup_list_item_layout, ccEmails) {
                        @Override
                        protected void convert(ViewHolder viewHolder, NoticeEmailEntity item, int position) {
                            viewHolder.setText(R.id.popup_list_item_tv, item.getGroupName());
                        }
                    });
                }
                showOrDismissWindow(email_cc_checkbox_layout, aBoolean);
                if (!checkBoxes.contains(email_cc_checkbox)) {
                    checkBoxes.add(email_cc_checkbox);
                }
            }
        });

    }

    private void initView() {
        title.setText(StringUtil.getStyleText(getContext(), R.string.platform_update_title_$, "*"));
        receiver.setText(StringUtil.getStyleText(getContext(), R.string.email_receiver, "*"));

        int windowHeight = DisplayUtil.getWindowHeight(getActivity());
        createPopupWindow(windowHeight);

        if (position != -1) {
            setDefaultContent(platformUpdateEntities.get(position + 1));
        }
    }


    private void showOrDismissWindow(View targetView, Boolean aBoolean) {
        if (aBoolean) {
            showPopupWindow(targetView);
        } else {
            dismissPopupWindow();
        }
    }

    @SuppressWarnings("deprecation")
    private void createPopupWindow(int height) {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.announce_popup_layout, null);

        popupListView = (ListView) contentView.findViewById(R.id.announce_popup_list);

        popupWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.WRAP_CONTENT, height / 4, true);
        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                restore();
            }
        });

        popupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (currentPopup) {
                    case Constants.POPUP_MODULE:
                        platformUpdateEntity = platformUpdateEntities.get(position);
                        box.setText(platformUpdateEntity.getNoticeTitle());
                        setDefaultContent(platformUpdateEntity);
                        break;
                    case Constants.POPUP_EMAIL_RECEIVER:
                        selectedRecEmailGroup = recEmails.get(position);
                        String recGroup = selectedRecEmailGroup.getGroupName();
//                        box.setText((recGroup != null && recGroup.equals(mContext.getResources().getString(R.string.no_data))) ? "" : recGroup);
                        box.setText(recGroup);
                        platform_update_pl_email_receiver.setText(selectedRecEmailGroup.getSendToPerson());
                        break;
                    case Constants.POPUP_EMAIL_CC:
                        selectedCcEmailGroup = ccEmails.get(position);
                        String ccGroup = selectedCcEmailGroup.getGroupName();
//                        box.setText((group != null && group.equals(mContext.getResources().getString(R.string.no_data))) ? "" : group);
                        box.setText(ccGroup);
                        platform_update_pl_email_ccer.setText(selectedCcEmailGroup.getSendToPerson());
                        break;
                }
                dismissPopupWindow();
            }
        });

        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.notice_publish_title_bg));
        popupWindow.setOutsideTouchable(true);
    }

    @Override
    public BaseNoticeEntity getData() throws Exception {
        if (platformUpdateEntity == null) {
            platformUpdateEntity = new NoticePlatformUpdateEntity();
        }
        String title = platform_update_pl_title.getText().toString().trim();
        String receiver = platform_update_pl_email_receiver.getText().toString().trim();
        if (title.equals(""))
            throw new Exception(mContext.getResources().getString(R.string.error_breakdown_title));
        if (receiver.equals(""))
            throw new Exception(mContext.getResources().getString(R.string.error_breakdown_receiver));
        platformUpdateEntity.setCategoryId(AnnounceManageActivity.PLATFORM_UPGRADE_FLAG);
        platformUpdateEntity.setNoticeTitle(title);
        platformUpdateEntity.setUpdateTime(platform_update_pl_time.getText().toString().trim());
        platformUpdateEntity.setNoticeContent(platform_update_pl_content.getText().toString().trim());
        platformUpdateEntity.setDescription(platform_update_pl_detail.getText().toString().trim());
        platformUpdateEntity.setChangeInfluence(platform_update_pl_influence.getText().toString().trim());
        platformUpdateEntity.setInfluenceScope(platform_update_pl_influence_range.getText().toString().trim());
        platformUpdateEntity.setChargePerson(platform_update_pl_charge_person.getText().toString().trim());
        platformUpdateEntity.setSendToPerson(platform_update_pl_email_receiver.getText().toString().trim());
        platformUpdateEntity.setCopyToPerson(platform_update_pl_email_ccer.getText().toString().trim());
        Logger.d(platformUpdateEntity.toString());
        return platformUpdateEntity;
    }

    private void setDefaultContent(NoticePlatformUpdateEntity platformUpdateEntity) {
        this.platformUpdateEntity = platformUpdateEntity;
        if (!isUpdate) {
            platformUpdateEntity.setId(-1);
        }
        platform_update_pl_title.setText(platformUpdateEntity.getNoticeTitle() != null && !platformUpdateEntity.getNoticeTitle().equals(mContext.getResources().getString(R.string.no_data)) ? platformUpdateEntity.getNoticeTitle() : "");
        platform_update_pl_content.setText(platformUpdateEntity.getNoticeContent() != null ? platformUpdateEntity.getNoticeContent() : "");
        platform_update_pl_time.setText(platformUpdateEntity.getUpdateTime() != null ? platformUpdateEntity.getUpdateTime() : "");
        platform_update_pl_detail.setText(platformUpdateEntity.getDescription() != null ? platformUpdateEntity.getDescription() : "");
        platform_update_pl_influence.setText(platformUpdateEntity.getChangeInfluence() != null ? platformUpdateEntity.getChangeInfluence() : "");
        platform_update_pl_influence_range.setText(platformUpdateEntity.getInfluenceScope() != null ? platformUpdateEntity.getInfluenceScope() : "");
        platform_update_pl_charge_person.setText(platformUpdateEntity.getChargePerson() != null ? platformUpdateEntity.getChargePerson() : "");
        platform_update_pl_email_receiver.setText(platformUpdateEntity.getSendToPerson() != null ? platformUpdateEntity.getSendToPerson() : "");
        email_cc_checkbox.setText(mContext.getResources().getString(R.string.no_data));
        platform_update_pl_email_ccer.setText(platformUpdateEntity.getCopyToPerson() != null ? platformUpdateEntity.getCopyToPerson() : "");
        email_receiver_checkbox.setText(mContext.getResources().getString(R.string.no_data));
    }

    private void restore() {
        for (CheckBox box : checkBoxes) {
            if (box.isChecked())
                box.setChecked(false);
        }
    }

    private void showPopupWindow(View targetView) {
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAsDropDown(targetView);
        }
    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void initData() {

    }

}

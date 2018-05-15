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
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.RegionInfo;
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

public class CsicUpdatePublishFragment extends BaseAnnounceFragment {

    @Bind(R.id.module_checkbox)
    CheckBox module_checkbox;
    @Bind(R.id.module_checkbox_layout)
    LinearLayout module_checkbox_layout;
    @Bind(R.id.region_checkbox)
    CheckBox region_checkbox;
    @Bind(R.id.csic_checkbox)
    CheckBox csic_checkbox;
    @Bind(R.id.csic_checkbox_layout)
    LinearLayout csic_checkbox_layout;
    @Bind(R.id.email_receiver_checkbox)
    CheckBox email_receiver_checkbox;
    @Bind(R.id.email_receiver_checkbox_layout)
    LinearLayout email_receiver_checkbox_layout;
    @Bind(R.id.email_cc_checkbox)
    CheckBox email_cc_checkbox;
    @Bind(R.id.email_cc_checkbox_layout)
    LinearLayout email_cc_checkbox_layout;
    @Bind(R.id.csic_update_pl_title)
    EditText csic_update_pl_title;
    @Bind(R.id.csic_update_pl_desc_layout)
    LinearLayout csic_update_pl_desc_layout;
    @Bind(R.id.csic_update_pl_content)
    EditText csic_update_pl_content;
    @Bind(R.id.csic_update_pl_reason_layout)
    LinearLayout csic_update_pl_reason_layout;
    @Bind(R.id.csic_update_pl_purpose)
    EditText csic_update_pl_purpose;
    @Bind(R.id.csic_update_pl_method)
    EditText csic_update_pl_method;
    @Bind(R.id.csic_update_pl_time)
    EditText csic_update_pl_time;
    @Bind(R.id.csic_update_pl_influence_range_layout)
    LinearLayout csic_update_pl_influence_range_layout;
    @Bind(R.id.csic_update_pl_influence_range)
    EditText csic_update_pl_influence_range;
    @Bind(R.id.csic_update_pl_charge_person)
    EditText csic_update_pl_charge_person;
    @Bind(R.id.csic_update_pl_email_receiver_layout)
    LinearLayout csic_update_pl_email_receiver_layout;
    @Bind(R.id.csic_update_pl_email_receiver)
    EditText csic_update_pl_email_receiver;
    @Bind(R.id.csic_update_pl_email_ccer_layout)
    LinearLayout csic_update_pl_email_ccer_layout;
    @Bind(R.id.csic_update_pl_email_ccer)
    EditText csic_update_pl_email_ccer;

    @Bind(R.id.csic_update_csic_$)
    TextView csic;
    @Bind(R.id.csic_update_title_$)
    TextView title;
    @Bind(R.id.csic_update_content_$)
    TextView content;
    @Bind(R.id.csic_update_purpose_$)
    TextView purpose;
    @Bind(R.id.email_receiver)
    TextView receiver;

    private ArrayList<NoticeCsicUpdateEntity> csicUpdateEntities;
    private NoticeCsicUpdateEntity csicUpdateEntity;
    private HashMap<String, Object> emailAndCsic;

    private ArrayList<NoticeEmailEntity> recEmails;
    private ArrayList<NoticeEmailEntity> ccEmails;


    private ArrayList<CSICInfo> csicInfos;
    private ArrayList<RegionInfo> regionInfos;
    private ArrayList<CSICInfo> csicInfosForRegion;

    private int selectedRegionId = 0;
    private CSICInfo selectedCsic;
    private RegionInfo selectedRegion;
    private NoticeEmailEntity selectedRecEmailGroup;
    private NoticeEmailEntity selectedCcEmailGroup;

    private CheckBox box;
    private Context mContext;
    private int currentPopup;
    private ListView popupListView;
    private PopupWindow popupWindow;

    private List<CheckBox> checkBoxes = new ArrayList<>();


    public CsicUpdatePublishFragment() {
    }

    public static CsicUpdatePublishFragment newInstance(ArrayList<NoticeCsicUpdateEntity> param1, HashMap<String, Object> params2) {
        CsicUpdatePublishFragment fragment = new CsicUpdatePublishFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, params2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        if (getArguments() != null) {
            csicUpdateEntities = (ArrayList<NoticeCsicUpdateEntity>) getArguments().getSerializable(ARG_PARAM1);
            emailAndCsic = (HashMap<String, Object>) getArguments().getSerializable(ARG_PARAM2);
            recEmails = (ArrayList<NoticeEmailEntity>) emailAndCsic.get("email");
            ccEmails = recEmails;
            regionInfos = (ArrayList<RegionInfo>) emailAndCsic.get("region");
            csicInfos = (ArrayList<CSICInfo>) emailAndCsic.get("csic");
            setDefaultInfo();
        }
    }

    private void setDefaultInfo() {
        NoticeCsicUpdateEntity defaultBreakdownEntity = new NoticeCsicUpdateEntity();
        defaultBreakdownEntity.setNoticeTitle(mContext.getResources().getString(R.string.no_data));
        csicUpdateEntities.add(0, defaultBreakdownEntity);
        NoticeEmailEntity defaultRecEmail = new NoticeEmailEntity();
        defaultRecEmail.setGroupName(mContext.getResources().getString(R.string.no_data));
        recEmails.add(0, defaultRecEmail);
        RegionInfo defaultRegion = new RegionInfo();
        defaultRegion.setRegionName(mContext.getResources().getString(R.string.no_data));
        defaultRegion.setId(0);
        regionInfos.add(0, defaultRegion);
//        ccEmails.add(0, defaultRecEmail);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_csic_update_publish, container, false);
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
                    popupListView.setAdapter(new CommonAdapter<NoticeCsicUpdateEntity>(getContext(), R.layout.popup_list_item_layout, csicUpdateEntities) {
                        @Override
                        protected void convert(ViewHolder viewHolder, NoticeCsicUpdateEntity item, int position) {
                            viewHolder.setText(R.id.popup_list_item_tv, item.getNoticeTitle());
                        }
                    });
                }
                if (!checkBoxes.contains(module_checkbox)) {
                    checkBoxes.add(module_checkbox);
                }
                showOrDismissWindow(module_checkbox_layout, aBoolean);
            }
        });

        RxCompoundButton.checkedChanges(region_checkbox).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean && currentPopup != Constants.POPUP_REGION) {
                    box = region_checkbox;
                    currentPopup = Constants.POPUP_REGION;
                    popupListView.setAdapter(new CommonAdapter<RegionInfo>(getContext(), R.layout.popup_list_item_layout, regionInfos) {
                        @Override
                        protected void convert(ViewHolder viewHolder, RegionInfo item, int position) {
                            viewHolder.setText(R.id.popup_list_item_tv, item.getRegionName());
                        }
                    });
                }
                showOrDismissWindow(csic_checkbox_layout, aBoolean);
                if (!checkBoxes.contains(region_checkbox)) {
                    checkBoxes.add(region_checkbox);
                }
            }
        });

        RxCompoundButton.checkedChanges(csic_checkbox).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean && currentPopup != Constants.POPUP_CSIC) {
                    box = csic_checkbox;
                    currentPopup = Constants.POPUP_CSIC;
                    csicInfosForRegion = selectCsicInfoByRegionId(selectedRegionId);
                    popupListView.setAdapter(new CommonAdapter<CSICInfo>(getContext(), R.layout.popup_list_item_layout, csicInfosForRegion) {
                        @Override
                        protected void convert(ViewHolder viewHolder, CSICInfo item, int position) {
                            viewHolder.setText(R.id.popup_list_item_tv, item.getChName());
                        }
                    });
                }
                showOrDismissWindow(csic_checkbox_layout, aBoolean);
                if (!checkBoxes.contains(csic_checkbox)) {
                    checkBoxes.add(csic_checkbox);
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
        csic.setText(StringUtil.getStyleText(getContext(), R.string.csic_update_csic_$, "*"));
        content.setText(StringUtil.getStyleText(getContext(), R.string.csic_update_content_$, "*"));
        purpose.setText(StringUtil.getStyleText(getContext(), R.string.csic_update_purpose_$, "*"));
        title.setText(StringUtil.getStyleText(getContext(), R.string.csic_update_title_$, "*"));
        receiver.setText(StringUtil.getStyleText(getContext(), R.string.email_receiver, "*"));
        int windowHeight = DisplayUtil.getWindowHeight(getActivity());
        createPopupWindow(windowHeight);
        if (position != -1) {
            setDefaultContent(csicUpdateEntities.get(position + 1));
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
                        csicUpdateEntity = csicUpdateEntities.get(position);
                        box.setText(csicUpdateEntity.getNoticeTitle());
                        setDefaultContent(csicUpdateEntity);
                        break;
                    case Constants.POPUP_REGION:
                        selectedRegion = regionInfos.get(position);
                        box.setText(selectedRegion.getRegionName());
                        selectedRegionId = selectedRegion.getId();
                        csicInfosForRegion = selectCsicInfoByRegionId(selectedRegionId);
                        if (csicInfosForRegion.size() > 0) {
                            selectedCsic = csicInfosForRegion.get(0);
                            csic_checkbox.setText(selectedCsic.getChName());
                        } else {
                            csic_checkbox.setText(mContext.getResources().getString(R.string.no_data));
                            selectedCsic = null;
                        }
                        break;
                    case Constants.POPUP_CSIC:
                        selectedCsic = csicInfosForRegion.get(position);
                        box.setText(selectedCsic.getChName());
                        break;
                    case Constants.POPUP_EMAIL_RECEIVER:
                        selectedRecEmailGroup = recEmails.get(position);
                        String recGroup = selectedRecEmailGroup.getGroupName();
//                        box.setText((recGroup != null && recGroup.equals(mContext.getResources().getString(R.string.no_data))) ? "" : recGroup);
                        box.setText(recGroup);
                        csic_update_pl_email_receiver.setText(selectedRecEmailGroup.getSendToPerson());
                        break;
                    case Constants.POPUP_EMAIL_CC:
                        selectedCcEmailGroup = ccEmails.get(position);
                        String ccGroup = selectedCcEmailGroup.getGroupName();
//                        box.setText((group != null && group.equals(mContext.getResources().getString(R.string.no_data))) ? "" : group);
                        box.setText(ccGroup);
                        csic_update_pl_email_ccer.setText(selectedCcEmailGroup.getSendToPerson());
                        break;
                }
                dismissPopupWindow();
            }
        });

        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.notice_publish_title_bg));
        popupWindow.setOutsideTouchable(true);
    }

    private void setDefaultContent(NoticeCsicUpdateEntity csicUpdateEntity) {
        this.csicUpdateEntity = csicUpdateEntity;
        if (!isUpdate) {
            csicUpdateEntity.setId(-1);
        }
        selectedRegionId = csicUpdateEntity.getRegionId();
        RegionInfo regionInfo = getRegionById(selectedRegionId);
        if (regionInfo == null) {
            region_checkbox.setText(mContext.getResources().getString(R.string.no_data));
        } else {
            region_checkbox.setText(regionInfo.getRegionName());
        }

        if (!csicUpdateEntity.getNoticeTitle().equals(mContext.getResources().getString(R.string.no_data))) {
            CSICInfo csicInfo = new CSICInfo();
            csicInfo.setId(csicUpdateEntity.getCsicId());
            csicInfo.setChName(csicUpdateEntity.getCsicName());
            csicInfo.setEnName(csicUpdateEntity.getCsicNameEn());
            csicInfo.setRegionId(selectedRegionId);
            selectedCsic = csicInfo;
        } else {
            selectedCsic = null;
        }

        csic_checkbox.setText(csicUpdateEntity.getCsicName() != null ? csicUpdateEntity.getCsicName() : mContext.getResources().getString(R.string.no_data));
        csic_update_pl_title.setText(csicUpdateEntity.getNoticeTitle() != null && !csicUpdateEntity.getNoticeTitle().equals(mContext.getResources().getString(R.string.no_data)) ? csicUpdateEntity.getNoticeTitle() : "");
        csic_update_pl_content.setText(csicUpdateEntity.getNoticeContent() != null ? csicUpdateEntity.getNoticeContent() : "");
        csic_update_pl_purpose.setText(csicUpdateEntity.getPurpose() != null ? csicUpdateEntity.getPurpose() : "");
        csic_update_pl_time.setText(csicUpdateEntity.getUpdateTime() != null ? csicUpdateEntity.getUpdateTime() : "");
        csic_update_pl_method.setText(csicUpdateEntity.getReformOperate() != null ? csicUpdateEntity.getReformOperate() : "");
        csic_update_pl_influence_range.setText(csicUpdateEntity.getChangeInfluence() != null ? csicUpdateEntity.getChangeInfluence() : "");
        csic_update_pl_charge_person.setText(csicUpdateEntity.getChargePerson() != null ? csicUpdateEntity.getChargePerson() : "");
        csic_update_pl_email_receiver.setText(csicUpdateEntity.getSendToPerson() != null ? csicUpdateEntity.getSendToPerson() : "");
        email_cc_checkbox.setText(mContext.getResources().getString(R.string.no_data));
        csic_update_pl_email_ccer.setText(csicUpdateEntity.getCopyToPerson() != null ? csicUpdateEntity.getCopyToPerson() : "");
        email_receiver_checkbox.setText(mContext.getResources().getString(R.string.no_data));
    }

    @Override
    public BaseNoticeEntity getData() throws Exception {
        if (csicUpdateEntity == null) {
            csicUpdateEntity = new NoticeCsicUpdateEntity();
        }
        String title = csic_update_pl_title.getText().toString().trim();
        String content = csic_update_pl_content.getText().toString().trim();
        String purpose = csic_update_pl_purpose.getText().toString().trim();
        String receiver = csic_update_pl_email_receiver.getText().toString().trim();
        if (selectedCsic == null)
            throw new Exception(mContext.getResources().getString(R.string.error_csic_update_csic));
        if (title.equals(""))
            throw new Exception(mContext.getResources().getString(R.string.error_csic_update_title));
        if (content.equals(""))
            throw new Exception(mContext.getResources().getString(R.string.error_csic_update_content));
        if (purpose.equals(""))
            throw new Exception(mContext.getResources().getString(R.string.error_csic_update_purpose));
        if (receiver.equals(""))
            throw new Exception(mContext.getResources().getString(R.string.error_csic_update_receiver));
        csicUpdateEntity.setCategoryId(AnnounceManageActivity.CSIC_UPGRADE_FLAG);
        csicUpdateEntity.setCsicId(selectedCsic.getId());
        csicUpdateEntity.setRegionId(selectedRegion == null ? 0 : selectedRegion.getId());
        csicUpdateEntity.setNoticeTitle(csic_update_pl_title.getText().toString().trim());
        csicUpdateEntity.setPurpose(purpose);
        csicUpdateEntity.setReformOperate(csic_update_pl_method.getText().toString().trim());
        csicUpdateEntity.setUpdateTime(csic_update_pl_time.getText().toString().trim());
        csicUpdateEntity.setNoticeContent(csic_update_pl_content.getText().toString().trim());
        csicUpdateEntity.setChangeInfluence(csic_update_pl_influence_range.getText().toString().trim());
        csicUpdateEntity.setChargePerson(csic_update_pl_charge_person.getText().toString().trim());
        csicUpdateEntity.setSendToPerson(csic_update_pl_email_receiver.getText().toString().trim());
        csicUpdateEntity.setCopyToPerson(csic_update_pl_email_ccer.getText().toString().trim());
        Logger.d(csicUpdateEntity.toString());
        return csicUpdateEntity;
    }

    private void restore() {
        for (CheckBox box : checkBoxes) {
            if (box.isChecked())
                box.setChecked(false);
        }
    }

    private void showOrDismissWindow(View targetView, Boolean aBoolean) {
        if (aBoolean) {
            showPopupWindow(targetView);
        } else {
            dismissPopupWindow();
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


    private RegionInfo getRegionById(int regionId) {
        for (RegionInfo regionInfo1 : regionInfos) {
            if (regionId == regionInfo1.getId()) {
                return regionInfo1;
            }
        }
        return regionInfos.get(0);
    }

    private ArrayList<CSICInfo> selectCsicInfoByRegionId(int regionId) {
        if (regionId == 0) return csicInfos;
        ArrayList<CSICInfo> selectedCsicInfo = new ArrayList<>();
        for (CSICInfo csicInfo : csicInfos) {
            if (csicInfo.getRegionId() == regionId) {
                selectedCsicInfo.add(csicInfo);
            }
        }
//        selectedCsicInfo.add(0, csicInfos.get(0));
//        if (selectedCsicInfo.size() == 0) {
//            CSICInfo csicInfo = new CSICInfo();
//            csicInfo.setChName(mContext.getResources().getString(R.string.no_data));
//            selectedCsicInfo.add(csicInfo);
//        }
        return selectedCsicInfo;
    }

    @Override
    public void initData() {

    }


}

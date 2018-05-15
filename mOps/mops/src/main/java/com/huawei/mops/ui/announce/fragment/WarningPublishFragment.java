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
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
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

public class WarningPublishFragment extends BaseAnnounceFragment {

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

    @Bind(R.id.warning_pl_title)
    EditText warning_pl_title;
    @Bind(R.id.warning_pl_operate_type_layout)
    LinearLayout warning_pl_desc_layout;
    @Bind(R.id.warning_pl_operate_type)
    EditText warning_pl_operate_type;
    @Bind(R.id.warning_pl_contain_version_layout)
    LinearLayout warning_pl_reason_layout;
    @Bind(R.id.warning_pl_contain_version)
    EditText warning_pl_contain_version;
    @Bind(R.id.warning_pl_contain_devices)
    EditText warning_pl_contain_devices;
    @Bind(R.id.warning_pl_work_load)
    EditText warning_pl_work_load;
    @Bind(R.id.warning_pl_csicPerson_layout)
    LinearLayout warning_pl_influence_range_layout;
    @Bind(R.id.warning_pl_csicPerson)
    EditText warning_pl_csicPerson;
    @Bind(R.id.warning_pl_itPerson)
    EditText warning_pl_itPerson;
    @Bind(R.id.warning_pl_operatePerson)
    EditText warning_pl_operatePerson;
    @Bind(R.id.warning_pl_contain_scope)
    EditText warning_pl_contain_scope;
    @Bind(R.id.warning_pl_operate_require)
    EditText warning_pl_operate_require;
    @Bind(R.id.warning_pl_problem_desc)
    EditText warning_pl_problem_desc;
    @Bind(R.id.warning_pl_reform_operate)
    EditText warning_pl_reform_operate;
    @Bind(R.id.warning_pl_notice_content)
    EditText warning_pl_notice_content;

    @Bind(R.id.warning_pl_email_receiver_layout)
    LinearLayout warning_pl_email_receiver_layout;
    @Bind(R.id.warning_pl_email_receiver)
    EditText warning_pl_email_receiver;
    @Bind(R.id.warning_pl_email_ccer_layout)
    LinearLayout warning_pl_email_ccer_layout;
    @Bind(R.id.warning_pl_email_ccer)
    EditText warning_pl_email_ccer;

    @Bind(R.id.warning_csic_$)
    TextView csic;
    @Bind(R.id.warning_title_$)
    TextView title;
    @Bind(R.id.email_receiver)
    TextView receiver;

    private ArrayList<NoticeWarningEntity> warningEntities;
    private NoticeWarningEntity warningEntity;
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

    public WarningPublishFragment() {
    }

    public static WarningPublishFragment newInstance(ArrayList<NoticeWarningEntity> warningInfoEntities, HashMap<String, Object> emailAndCsic) {
        WarningPublishFragment fragment = new WarningPublishFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, warningInfoEntities);
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
            warningEntities = (ArrayList<NoticeWarningEntity>) getArguments().getSerializable(ARG_PARAM1);
            emailAndCsic = (HashMap<String, Object>) getArguments().getSerializable(ARG_PARAM2);
            recEmails = (ArrayList<NoticeEmailEntity>) emailAndCsic.get("email");
            ccEmails = recEmails;
            regionInfos = (ArrayList<RegionInfo>) emailAndCsic.get("region");
            csicInfos = (ArrayList<CSICInfo>) emailAndCsic.get("csic");
            setDefaultInfo();
        }
    }

    private void setDefaultInfo() {
        NoticeWarningEntity defaultWarningEntity = new NoticeWarningEntity();
        defaultWarningEntity.setNoticeTitle(mContext.getResources().getString(R.string.no_data));
        warningEntities.add(0, defaultWarningEntity);
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
        return inflater.inflate(R.layout.fragment_warning_publish, container, false);
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
                    popupListView.setAdapter(new CommonAdapter<NoticeWarningEntity>(getContext(), R.layout.popup_list_item_layout, warningEntities) {
                        @Override
                        protected void convert(ViewHolder viewHolder, NoticeWarningEntity item, int position) {
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

        csic.setText(StringUtil.getStyleText(getContext(), R.string.warning_csic_$, "*"));
        title.setText(StringUtil.getStyleText(getContext(), R.string.warning_title_$, "*"));
        receiver.setText(StringUtil.getStyleText(getContext(), R.string.email_receiver, "*"));

        int windowHeight = DisplayUtil.getWindowHeight(getActivity());
        createPopupWindow(windowHeight);

        if (position > 0) {
            setDefaultContent(warningEntities.get(position + 1));
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
                        warningEntity = warningEntities.get(position);
                        box.setText(warningEntity.getNoticeTitle());
                        setDefaultContent(warningEntity);
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
                            selectedCsic=null;
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
                        warning_pl_email_receiver.setText(selectedRecEmailGroup.getSendToPerson());
                        break;
                    case Constants.POPUP_EMAIL_CC:
                        selectedCcEmailGroup = ccEmails.get(position);
                        String ccGroup = selectedCcEmailGroup.getGroupName();
//                        box.setText((group != null && group.equals(mContext.getResources().getString(R.string.no_data))) ? "" : group);
                        box.setText(ccGroup);
                        warning_pl_email_ccer.setText(selectedCcEmailGroup.getSendToPerson());
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
        if (warningEntity == null) {
            warningEntity = new NoticeWarningEntity();
        }
        String title = warning_pl_title.getText().toString().trim();
        String receiver = warning_pl_email_receiver.getText().toString().trim();
        if (selectedCsic == null)
            throw new Exception(mContext.getResources().getString(R.string.error_warning_csic));
        if (title.equals(""))
            throw new Exception(mContext.getResources().getString(R.string.error_breakdown_title));
        if (receiver.equals(""))
            throw new Exception(mContext.getResources().getString(R.string.error_breakdown_receiver));
        warningEntity.setCategoryId(AnnounceManageActivity.WARNING_FLAG);
        warningEntity.setCsicId(selectedCsic.getId());
        warningEntity.setRegionId(selectedRegion == null ? 0 : selectedRegion.getId());
        warningEntity.setNoticeTitle(title);
        warningEntity.setOperateType(warning_pl_operate_type.getText().toString().trim());
        warningEntity.setContainVersion(warning_pl_contain_version.getText().toString().trim());
        warningEntity.setContainBusiness(warning_pl_contain_devices.getText().toString().trim());
        warningEntity.setWorkload(warning_pl_work_load.getText().toString().trim());
        warningEntity.setCsicPerson(warning_pl_csicPerson.getText().toString().trim());
        warningEntity.setItPerson(warning_pl_itPerson.getText().toString().trim());
        warningEntity.setOperatePerson(warning_pl_operatePerson.getText().toString().trim());
        warningEntity.setContainScope(warning_pl_contain_scope.getText().toString().trim());
        warningEntity.setOperateNotice(warning_pl_operate_require.getText().toString().trim());
        warningEntity.setProblemDescription(warning_pl_problem_desc.getText().toString().trim());
        warningEntity.setReformOperate(warning_pl_reform_operate.getText().toString().trim());
        warningEntity.setNoticeContent(warning_pl_notice_content.getText().toString().trim());
        warningEntity.setSendToPerson(receiver);
        warningEntity.setCopyToPerson(warning_pl_email_ccer.getText().toString().trim());
        Logger.d(warningEntity.toString());
        return warningEntity;
    }


    private void setDefaultContent(NoticeWarningEntity warningEntity) {
        this.warningEntity = warningEntity;
        if (!isUpdate) {
            warningEntity.setId(-1);
        }
        selectedRegionId = warningEntity.getRegionId();
        RegionInfo regionInfo = getRegionById(selectedRegionId);
        if (regionInfo == null) {
            region_checkbox.setText(mContext.getResources().getString(R.string.no_data));
        } else {
            region_checkbox.setText(regionInfo.getRegionName());
        }
        if (!warningEntity.getNoticeTitle().equals(mContext.getResources().getString(R.string.no_data))) {
            CSICInfo csicInfo = new CSICInfo();
            csicInfo.setId(warningEntity.getCsicId());
            csicInfo.setChName(warningEntity.getCsicName());
            csicInfo.setEnName(warningEntity.getCsicNameEn());
            csicInfo.setRegionId(selectedRegionId);
            selectedCsic = csicInfo;
        } else {
            selectedCsic = null;
        }

        csic_checkbox.setText(warningEntity.getCsicName() != null ? warningEntity.getCsicName() : mContext.getResources().getString(R.string.no_data));
        warning_pl_title.setText(warningEntity.getNoticeTitle() != null && !warningEntity.getNoticeTitle().equals(mContext.getResources().getString(R.string.no_data)) ? warningEntity.getNoticeTitle() : "");
        warning_pl_operate_type.setText(warningEntity.getOperateType() != null ? warningEntity.getOperateType() : "");
        warning_pl_contain_version.setText(warningEntity.getContainVersion() != null ? warningEntity.getContainVersion() : "");
        warning_pl_contain_devices.setText(warningEntity.getContainBusiness() != null ? warningEntity.getContainBusiness() : "");
        warning_pl_work_load.setText(warningEntity.getWorkload() != null ? warningEntity.getWorkload() : "");
        warning_pl_csicPerson.setText(warningEntity.getCsicPerson() != null ? warningEntity.getCsicPerson() : "");
        warning_pl_itPerson.setText(warningEntity.getItPerson() != null ? warningEntity.getItPerson() : "");
        warning_pl_operatePerson.setText(warningEntity.getOperatePerson() != null ? warningEntity.getOperatePerson() : "");
        warning_pl_contain_scope.setText(warningEntity.getContainScope() != null ? warningEntity.getContainScope() : "");
        warning_pl_operate_require.setText(warningEntity.getOperateNotice() != null ? warningEntity.getOperateNotice() : "");
        warning_pl_problem_desc.setText(warningEntity.getProblemDescription() != null ? warningEntity.getProblemDescription() : "");
        warning_pl_reform_operate.setText(warningEntity.getReformOperate() != null ? warningEntity.getReformOperate() : "");
        warning_pl_notice_content.setText(warningEntity.getNoticeContent() != null ? warningEntity.getNoticeContent() : "");

        warning_pl_email_receiver.setText(warningEntity.getSendToPerson() != null ? warningEntity.getSendToPerson() : "");
        email_cc_checkbox.setText(mContext.getResources().getString(R.string.no_data));
        warning_pl_email_ccer.setText(warningEntity.getCopyToPerson() != null ? warningEntity.getCopyToPerson() : "");
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
        return selectedCsicInfo;
    }

    @Override
    public void initData() {

    }

}

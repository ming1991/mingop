package com.huawei.mops.ui.inspection;

import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.ShowCaseEntity;
import com.huawei.mops.bean.SolutionTopicInfo;
import com.huawei.mops.ui.BaseActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShowcaseDetailActivity extends BaseActivity {

    @Bind(R.id.showCaseId)
    TextView showCaseId;
    @Bind(R.id.cnName)
    TextView cnName;
    @Bind(R.id.enName)
    TextView enName;
    @Bind(R.id.solutionTopic)
    TextView solutionTopic;
    @Bind(R.id.showArea)
    TextView showArea;
    @Bind(R.id.csic)
    TextView csic;
    @Bind(R.id.bandWidth)
    TextView bandWidth;
    @Bind(R.id.experienceWay)
    TextView experienceWay;
    @Bind(R.id.status)
    TextView status;
    @Bind(R.id.deploymentStatus)
    TextView deploymentStatus;
    @Bind(R.id.loadWay)
    TextView loadWay;
    @Bind(R.id.maintenancePerson)
    TextView maintenancePerson;
    @Bind(R.id.showcase_cn_des_content)
    TextView cnDes;
    @Bind(R.id.showcase_en_des_content)
    TextView enDes;

    private ShowCaseEntity showCaseEntity;

    @Override
    protected String initTitleBar() {
        return getResources().getString(R.string.showcase_detail);
    }

    @Override
    protected void initUIAndListener() {
        initData();
    }

    private void initData() {
        showCaseEntity = (ShowCaseEntity) getIntent().getSerializableExtra("showcase");
        CSICInfo csicInfo = showCaseEntity.getCsic();
        SolutionTopicInfo solutionTopicInfo = showCaseEntity.getSolutionTopic();
        Logger.d("showCaseEntity:" + showCaseEntity.toString());
        showCaseId.setText(showCaseEntity.get_showcaseId()+"");
        cnName.setText(showCaseEntity.getNameCh());
        enName.setText(showCaseEntity.getNameEn());
        solutionTopic.setText(solutionTopicInfo.getTopicName());
        csic.setText(csicInfo.getChName());
        showArea.setText(showCaseEntity.getShowarea());
        bandWidth.setText(showCaseEntity.getBandWidth()+"");
        experienceWay.setText(showCaseEntity.getExperienceWay());
        status.setText(showCaseEntity.getStatus());
        deploymentStatus.setText(showCaseEntity.getDeploymentStatus());
        loadWay.setText(showCaseEntity.getLoadWay());
        maintenancePerson.setText(showCaseEntity.getMaintenancePerson());
        cnDes.setText(showCaseEntity.getDescriptionCh());
        enDes.setText(showCaseEntity.getDescriptionEn());
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_showcase_detail;
    }
}

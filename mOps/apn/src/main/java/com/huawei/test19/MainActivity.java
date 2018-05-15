package com.huawei.test19;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.huawei.test19.ablistview.CommonAdapter;
import com.huawei.test19.ablistview.ViewHolder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private SQLiteDatabase sqLiteDatabase;
    Button query;
    ListView apnListview;
    private ArrayList<APN> apns;
    private Button change;
    private SubscriptionManager manager;
    private SubscriptionInfo mSubInfoRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        CatchHandler.getInstance().init(this);
        query = (Button) findViewById(R.id.query);
        apnListview = (ListView) findViewById(R.id.apns);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apns = APNSwitcher.getAvailableAPNList(MainActivity.this);
                apnListview.setAdapter(new CommonAdapter<APN>(MainActivity.this, R.layout.item, apns) {

                    @Override
                    protected void convert(ViewHolder viewHolder, APN item, int position) {
                        viewHolder.setText(R.id.apnName, item.getName());
                        viewHolder.setText(R.id.apn, item.getApn());
                        viewHolder.setText(R.id.apnId, item.getId());
                        viewHolder.setText(R.id.apnType, item.getType());
                    }
                });
            }
        });

        apnListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, apns.get(position).getName(), Toast.LENGTH_SHORT).show();
                APNSwitcher.setPreferAPN(MainActivity.this, apns.get(position).getId(), apns.get(position).getName());
            }
        });
        change = (Button) findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                manager = SubscriptionManager.from(MainActivity.this);
//                mSubInfoRecord = manager.getActiveSubscriptionInfoForSimSlotIndex(0);
//                Class<?> clazz = manager.getClass();
//                Class<?> recoder = mSubInfoRecord.getClass();
//                try {
//                    Method recoder_setDisplayName = recoder.getDeclaredMethod("setDisplayName", CharSequence.class);
//                    recoder_setDisplayName.setAccessible(true);
//                    recoder_setDisplayName.invoke(mSubInfoRecord, "test");
//                    Field field = clazz.getDeclaredField("NAME_SOURCE_USER_INPUT");
//                    Log.i("huawei", "NAME_SOURCE_USER_INPUT=" + field.getInt(manager));
//                    Method setDisplayName = clazz.getDeclaredMethod("setDisplayName", String.class, int.class, long.class);
//                    setDisplayName.setAccessible(true);
//                    setDisplayName.invoke(manager, "test", mSubInfoRecord.getSubscriptionId(), field.getInt(manager));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                Log.i("huawei", "name=" + mSubInfoRecord.getDisplayName() + ",subId=" + mSubInfoRecord.getSubscriptionId());
//                try {
////                    set();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

                tm.setOperatorBrandOverride("test");
            }
        });
    }
//    11-25 11:33:26.115: I/huawei(31049): setCarrierName
//    11-25 11:33:26.115: I/huawei(31049): setDisplayName

    private void set() throws Exception {
        Class<?> c = Class.forName("android.telephony.SubscriptionInfo");
        Method[] m = c.getMethods();
        for (Method method : m) {
            if (method.getName().equals("setDisplayName")) {
                Log.i("huawei", method.getName());
                Object obj = c.getConstructor();
                method.invoke(obj, "test");
            }
        }
    }

    public void chmod(String path) {
        ShellUtil.runCommand("chmod 777 " + path);
    }
}

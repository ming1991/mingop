package com.huawei.mops.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.components.okhttp.AddCookieInterceptor;
import com.huawei.mops.components.okhttp.LoggerInterceptor;
import com.huawei.mops.components.okhttp.RecivedCookieInterceptor;
import com.huawei.mops.components.security.DeEncode;
import com.huawei.mops.util.Constants;
import com.jakewharton.rxbinding.view.RxView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import rx.functions.Action1;

public class TestActivity extends AppCompatActivity {

    private String url;
    @Bind(R.id.url)
    EditText et;
    @Bind(R.id.button)
    Button bt;
    @Bind(R.id.login)
    Button login;
    @Bind(R.id.test)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
//                .cookieJar(cookieJar)
                .addInterceptor(new LoggerInterceptor("login", true))
                .addInterceptor(new AddCookieInterceptor(this))
                .connectTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .build();
        OkHttpUtils.initClient(mOkHttpClient);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpUtils.post().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        System.out.printf("测试结果：" + s);
                        tv.setText(s);
                    }
                });
            }
        });
        RxView.clicks(bt).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                url = Constants.BASE_URL + et.getText().toString().trim();
            }
        });

        RxView.clicks(login).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                OkHttpUtils.post().url("http://10.78.234.235:8080/CSICLab/app/login.do")
//                        .addParams("loginName", "twx366549")
//                        .addParams("passWord", new DeEncode().strEnc("wei@366549", "3", "5", "7"))
//                        .build().execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int i) {
//                        System.out.printf("登录失败");
//                    }
//
//                    @Override
//                    public void onResponse(String s, int i) {
//                        System.out.printf("登录成功:s=" + s);
//                    }
//                });
            }
        });
    }

}

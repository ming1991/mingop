package com.huawei.mops.components.retrofit;

import com.alibaba.fastjson.JSON;
import com.huawei.mops.api.APIException;
import com.huawei.mops.api.LoginException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by sll on 2016/3/31.
 */
public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Type type;
    private Charset charset;

    public FastJsonResponseBodyConverter() {
    }

    public FastJsonResponseBodyConverter(Type type, Charset charset) {
        this.type = type;
        this.charset = charset;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            JSONObject object = null;
            String result = value.string();
            try {
                object = new JSONObject(result);
                String redirect = object.getString("redirect");
                if (redirect != null && redirect.equals("_redirect123")) {
                    throw new LoginException();
                }
            } catch (JSONException e) {
//                e.printStackTrace();
//                throw new APIException(1, e.getMessage());
            } catch (LoginException e1) {
//                e1.printStackTrace();
                throw new LoginException();
            }

            return JSON.parseObject(result, type);
        } finally {
            value.close();
        }
    }
}

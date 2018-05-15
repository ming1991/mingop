package com.huawei.mops.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by tWX366549 on 2016/10/19.
 */
public class LanUtil {
    static Map<String, String> lan = new HashMap<>();

    static {
        lan.put("area", "区域");
        lan.put("field", "领域");
        lan.put("level", "级别");
        lan.put("state", "状态");
        lan.put("office", "代表处");
        lan.put("market", "细分市场");
    }

    public static String enToCn(String en) {
        return lan.get(en);
    }

    @SuppressWarnings("unchecked")
    public static String cnToEn(String cn) {
        Set<Map.Entry<String, String>> set = lan.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            if (cn.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}

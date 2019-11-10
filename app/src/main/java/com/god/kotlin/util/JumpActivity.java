package com.god.kotlin.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.god.kotlin.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class JumpActivity {
    public static Map<String, Class> classMap = new HashMap<>();

    static {
        classMap.put("登录", MainActivity.class);
    }

    public static void start(Context context, String action) {
        if (!TextUtils.isEmpty(action)) {
                Intent intent = new Intent();
                if (action.equals("登录")) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                intent.setClass(context, classMap.get(action));
                context.startActivity(intent);

        }
    }

}

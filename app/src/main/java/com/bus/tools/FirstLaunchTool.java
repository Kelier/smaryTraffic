package com.bus.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by John Yan on 1/3/2017.
 */

public class FirstLaunchTool {
//保存是否是第一次保存的文件名
    static final String FILE_START="isStart";
    //保存是否是第一次保存的键名
    static final String IS_FIRSTSTART="isStart";
    /**
     * 调用该方法时，说明第一次启动程序结束
     * @param context
     */
    public static void saveOne(Context context){
        SharedPreferences pref=context.getSharedPreferences(FILE_START,context.MODE_PRIVATE);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_FIRSTSTART,false);
        edit.commit();
    }
/**
 * 读取是否第一次启动，如果读取到假，不是第一次启动
 * @param context
 * @return
 */
    public static Boolean isOne(Context context){
    SharedPreferences pref=context.getSharedPreferences(FILE_START,context.MODE_PRIVATE);
        return pref.getBoolean(IS_FIRSTSTART,true);
    }

}

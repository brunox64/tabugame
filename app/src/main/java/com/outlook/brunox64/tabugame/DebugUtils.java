package com.outlook.brunox64.tabugame;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by bruno on 18/09/2016.
 */
public class DebugUtils {
    public static void info(Object obj,String message) {
        Log.i(obj.getClass().getSimpleName(), message);
    }
    public static void dump(Object obj,Rect rect) {
        Log.i(obj.getClass().getSimpleName(), "LEFT:{0};TOP:{1};WIDTH:{2};HEIGHT:{3};".replace("{0}", String.valueOf(rect.left)).replace("{1}", String.valueOf(rect.top)).replace("{2}", String.valueOf(rect.width())).replace("{3}", String.valueOf(rect.height())));
    }
    public static void dump(Object obj,Canvas c) {
        Log.i(obj.getClass().getSimpleName(), "LEFT:0;TOP:0;WIDTH:{2};HEIGHT:{3};".replace("{2}", String.valueOf(c.getWidth())).replace("{3}", String.valueOf(c.getHeight())));
    }
    public static void dump(Object obj,View v) {
        Log.i(obj.getClass().getSimpleName(), "LEFT:{0};TOP:{1};WIDTH:{2};HEIGHT:{3};".replace("{0}",String.valueOf(v.getX())).replace("{1}",String.valueOf(v.getY())).replace("{2}",String.valueOf(v.getWidth())).replace("{3}", String.valueOf(v.getHeight())));
    }
    public static void dump(Object obj) {
        Log.i(obj.getClass().getSimpleName(), "------------------------------------------------------------");
        Log.i(obj.getClass().getSimpleName(), "Fields------------------------------------------------------");
        Field[] fields = obj.getClass().getFields();
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                Log.i(obj.getClass().getSimpleName(), f.getName() + ":" + f.get(obj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i(obj.getClass().getSimpleName(), "Methods-----------------------------------------------------");
        Method[] methods = obj.getClass().getMethods();
        for (Method m : methods) {
            m.setAccessible(true);
            if (m.getName().startsWith("next") && m.getParameterTypes().length == 0) {
                try {
                    Log.i(obj.getClass().getSimpleName(), m.getName() + ":" + m.invoke(obj,null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i(obj.getClass().getSimpleName(), "------------------------------------------------------------");
    }
}

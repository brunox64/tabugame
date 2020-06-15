package com.outlook.brunox64.tabugame;

/**
 * Created by bruno on 18/09/2016.
 */
public class MouseUtils {
    public static boolean entre(float x, float y, float w, float h, float mx, float my) {
        return mx > x
                && mx < x+w
                && my > y
                && my < y+h;
    }
}

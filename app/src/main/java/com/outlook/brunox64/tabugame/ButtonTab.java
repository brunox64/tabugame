package com.outlook.brunox64.tabugame;

/**
 * Created by bruno on 08/09/2016.
 */
public class ButtonTab {

    private float x;
    private float y;
    private float w;
    private float h;

    private String label;
    private int tab;
    private int oper;
    private float perc;

    public ButtonTab(int tab, int oper) {
        this.tab = tab;
        this.oper = oper;
        this.perc = 0f;

        label = String.valueOf(tab) + Operacao.getSinal(oper);
    }

    public String getLabel() {
        return this.label;
    }

    public int getTab() {
        return tab;
    }

    public int getOper() {
        return oper;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public void setPerc(float perc) {
        this.perc = perc;
    }

    public float getPerc() {
        return perc;
    }
}

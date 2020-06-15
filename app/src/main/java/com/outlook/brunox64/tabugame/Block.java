package com.outlook.brunox64.tabugame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno on 18/09/2016.
 */
public class Block extends EventDispacher {

    private GameContext ctx = GameContext.get();

    private int con;
    private boolean visivel,selected;
    private List<AnimatorNumber> animators;
    private AnimatorNumber animX,animY;
    private float errando,x,y,w,h,m;
    private int cor,animations;
    private Paint paintFill,paintText,paintBorder;
    private boolean toDraw = true;
    public Block(int con) {
        super();
        this.con = con;
        visivel = true;
        selected = false;
        animations = 0;
        errando = 0f;
        cor = ctx.getColorPrimary();
        animators = new ArrayList<>();

        paintFill = new Paint();
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setColor(cor);
        //paintFill.setColor(Color.BLACK);

        paintBorder = new Paint();
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setColor(0xFFFFFFFF);
        //paintBorder.setColor(Color.BLACK);

        paintText = new Paint();
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(toPixel(20f));
        //paintText.setColor(Color.YELLOW);
        paintText.setColor(Color.WHITE);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setAntiAlias(true);

        animX = new AnimatorNumber(new AnimatorNumber.Property(){
            @Override
            public float get() {return x;}
            @Override
            public void set(float value) {
                x = value;
                toDraw = true;
            }
        });
        animY = new AnimatorNumber(new AnimatorNumber.Property(){
            @Override
            public float get() {return y;}
            @Override
            public void set(float value) {
                y = value;
                toDraw = true;
            }
        });

        this.animX.addEventListener("beforeAnimation", new EventDispacher.Listener() {
            @Override
            public void invoke(Object arg) {
                beforeAnimation();
            }
        });
        this.animX.addEventListener("afterAnimation", new EventDispacher.Listener() {
            @Override
            public void invoke(Object arg) {
                afterAnimation();
            }
        });
        this.animators.add(this.animX);

        this.animY.addEventListener("beforeAnimation",new EventDispacher.Listener(){
            @Override
            public void invoke(Object arg) {
                beforeAnimation();
            }
        });
        this.animY.addEventListener("afterAnimation",new EventDispacher.Listener(){
            @Override
            public void invoke(Object arg) {
                afterAnimation();
            }
        });
        this.animators.add(this.animY);
    }

    private float toPixel(float dp) {
        return ctx.toPixel(dp);
    }

    private float toDip(float p) {
        return ctx.toDip(p);
    }

    private void beforeAnimation() {
        if (this.animations == 0) {
            this.dispatchEvent("beforeAnimation");
        }
        this.animations++;
    }
    private void afterAnimation() {
        this.animations--;
        if (this.animations == 0) {
            this.dispatchEvent("afterAnimation");
        }
    }
    public boolean update() {
        for (AnimatorNumber a : animators) {
            a.update();
        }
        if (errando > 0) {
            cor = Color.RED;
            paintFill.setColor(cor);
            this.errando -= 1;
            if (this.errando <= 0) {
                this.errando = 0;
                this.unSelect();
            }
            toDraw = true;
        }
        return toDraw;
    }
    public void draw(Canvas c) {
        toDraw = false;
        if (this.visivel) {
            float m = paintBorder.getStrokeWidth()*0.5f;
            c.drawRect(this.x - m, this.y - m, this.w + this.x + m, this.h + this.y + m, paintBorder);
            c.drawRect(this.x, this.y, this.w+this.x, this.h+this.y, paintFill);
            c.drawText(String.valueOf(con), this.x+this.w/2,this.y+this.h/2+paintText.getTextSize()/4, paintText);
        }
    }
    public int getNum() {
        return con;
    }
    public boolean isAnimando() {
        return animations != 0;
    }

    public void show() {
        visivel = true;
        toDraw = true;
    }

    public void hide() {
        visivel = false;
        toDraw = true;
    }

    public void select() {
        selected = true;
        cor = 0xff96969C;// cinza
        paintFill.setColor(cor);
        toDraw = true;
    }

    public void unSelect() {
        selected = false;
        cor = ctx.getColorPrimary();
        paintFill.setColor(cor);
        toDraw = true;
    }

    public void marcarSugestao() {

        if (isSelected() || errando > 0) return;

        cor = 0xFF32C424;
        paintFill.setColor(cor);
        toDraw = true;
    }

    public void desmarcarSugestao() {

        if (isSelected() || errando > 0) return;

        cor = ctx.getColorPrimary();
        paintFill.setColor(cor);
        toDraw = true;
    }

    public boolean isVisivel() {
        return visivel;
    }

    public boolean isSelected() {
        return selected;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        toDraw = true;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
        toDraw = true;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        toDraw = true;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
        toDraw = true;
    }
    public float getM() {
        return m;
    }
    public void setM(float m) {
        this.m = m;
        paintBorder.setStrokeWidth(m*2);
        toDraw = true;
        //DebugUtils.info(this,"Block x:" + x + ", y:" + y + ", w:" + w + ", h:" + h);
    }
    public void errou() {
        errando = 500f/ ctx.getUpdateTime();
        toDraw = true;
    }
    public void acima() {
        this.y = this.y-this.h-this.m*2f;
        toDraw = true;
    }
    public void abaixo() {
        this.animY.sum(this.h+this.m*2f);
        toDraw = true;
    }
    public void abaixoInst() {
        this.y = this.y+this.h+this.m*2f;
        toDraw = true;
    }
    public void esquerda() {
        this.x = this.x-this.w-this.m*2f;
        toDraw = true;
    }
    public void direita() {
        this.x = this.x+this.w+this.m*2f;
        toDraw = true;
    }
    public void move(int px, int py) {
        this.animX.sum((this.w+this.m*2f)*px);
        this.animY.sum((this.h+this.m*2f)*py);
        toDraw = true;
    }
    public void stopMove() {
        animX.stop();
        animY.stop();
        toDraw = true;
    }
}

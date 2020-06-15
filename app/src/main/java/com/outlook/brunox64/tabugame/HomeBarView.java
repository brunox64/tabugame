package com.outlook.brunox64.tabugame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Created by bruno on 22/01/2017.
 */
public class HomeBarView extends View {

    private float dens;
    private Paint paintText;

    public HomeBarView(Context ctx) {
        this(ctx, null);
    }

    public HomeBarView(Context ctx, AttributeSet attr) {
        super(ctx, attr);

        Resources res = getContext().getResources();
        dens = res.getDisplayMetrics().density;

        paintText = new Paint();
        paintText.setFilterBitmap(true);
        paintText.setAntiAlias(true);
        paintText.setColor(Color.WHITE);
        paintText.setAlpha(255);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(toPixel(30f));
        paintText.setFakeBoldText(true);
        paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("Tabu Game", getWidth() / 2, getHeight() / 3 * 2, paintText);
    }


    public float toPixel(float dp) {
        return dp * dens;
    }

    public float toDip(float p) {
        if (Math.abs(p) <= 0f) return 0f;
        return p / dens;
    }
}

package com.outlook.brunox64.tabugame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bruno on 08/09/2016.
 */
public class ExemploButtonTabView extends View {

    private Paint paintRoxo;
    private float dens;

    public ExemploButtonTabView(Context ctx) {
        this(ctx,null);
    }

    public ExemploButtonTabView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);

        dens = getContext().getResources().getDisplayMetrics().density;

        paintRoxo = new Paint();
        paintRoxo.setFilterBitmap(true);
        paintRoxo.setAntiAlias(true);
        paintRoxo.setStyle(Paint.Style.STROKE);
        paintRoxo.setStrokeWidth(10 * dens);
        paintRoxo.setShadowLayer(1 * dens, 0, 1 * dens, Color.argb(255, 110, 110, 110));
        paintRoxo.setColor(0x7C4BBE);
        paintRoxo.setAlpha(255);

        setLayerType(LAYER_TYPE_SOFTWARE, paintRoxo);

        setFocusable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec);
        int h = resolveSize(MeasureSpec.getSize(heightMeasureSpec), heightMeasureSpec);

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(w, MeasureSpec.getMode(widthMeasureSpec)), MeasureSpec.makeMeasureSpec(h, MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getWidth(), getHeight(), Math.min(getWidth()/2, getHeight()/2), paintRoxo);
    }
}

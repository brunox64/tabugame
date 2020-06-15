package com.outlook.brunox64.tabugame;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by bruno on 08/09/2016.
 */
public class DebugView extends View {

    private final String TAG;

    public DebugView(Context ctx) {
        this(ctx,null);
    }

    public DebugView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        TAG = "brunox64";
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "OnMeasure W:" + MeasureSpec.getSize(widthMeasureSpec) + ", H:" + MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "OnSizeChanged w:" + w + ", oldw:" + oldw + ", h:" + h + ", oldh:" + oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "OnDraw w:" + getWidth() + ", h:" + getHeight());
    }
}

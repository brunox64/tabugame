package com.outlook.brunox64.tabugame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by bruno on 30/08/2016.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;
    private GameContext gameContext;
    private Thread thread;
    private boolean running;
    private Table table;
    private CloseListener mCloseListener;
    private int heightBar;
    private int widthBar;
    private Paint paintFill;
    private Drawable arrowBackIcon;

    public GameView(Context ctx) {
        this(ctx, null);
    }

    public GameView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);

        getHolder().addCallback(this);

        gameContext = GameContext.get();

        holder = getHolder();
        table = new Table();

        paintFill = new Paint();
        paintFill.setFilterBitmap(true);
        paintFill.setAntiAlias(true);
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setStrokeWidth(gameContext.toPixel(10f));
        paintFill.setColor(gameContext.getColorPrimary());
        paintFill.setAlpha(255);

        arrowBackIcon = getResources().getDrawable(R.drawable.ic_arrow_back_24dp, getContext().getTheme());

        DebugUtils.info(this, "new GameView");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        DebugUtils.info(this, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        DebugUtils.info(this, "surfaceChanged");
        //DebugUtils.dump(holder);
        //DebugUtils.dump(this);

        DebugUtils.info(this, "resize:" + format + ":" + width + ":" + height);
        Rect frame = holder.getSurfaceFrame();
        DebugUtils.info(this, "resize: x" + frame.left + ",y" + frame.top + ",w" + frame.width() + ",h" + frame.height());

        widthBar = width;
        heightBar = (int) gameContext.toPixel(60f);

        float pad = gameContext.toPixel(10f);

        table.resize((int) pad, (int) (heightBar+pad), width - (int) (pad * 2f), height - heightBar - (int) (pad * 2f));
        table.norm();

        arrowBackIcon.setBounds( (int) (heightBar * 0.275f), (int) (heightBar * 0.275f), (int) (heightBar * 0.725f), (int) (heightBar * 0.725f));
        arrowBackIcon.setTint(0xFFFFFFFF);

        if (running) return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        DebugUtils.info(this, "surfaceDestroyed");
        DebugUtils.info(this, "stop");

        if (running) {
            running = false;
            boolean retry = true;
            while (retry) {
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {}
            }
        }
    }

    @Override
    synchronized public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP
                && MouseUtils.entre(0, 0, heightBar, heightBar, event.getX(), event.getY())) {
            mCloseListener.onClose();
            return true;
        }

        return table.onTouchEvent(event);
    }

    synchronized private boolean update() {
        return table.update();
    }

    synchronized private void draw() {
        Canvas c = holder.lockCanvas();
        //DebugUtils.info(this, "draw 1");
        if (c != null) {
            //DebugUtils.info(this, "draw density:" + c.getDensity());
            c.drawColor(Color.WHITE);

            table.draw(c);

            c.drawRect(0, 0, widthBar, heightBar, paintFill);
            arrowBackIcon.draw(c);

            holder.unlockCanvasAndPost(c);
        }
    }

    @Override
    public void run() {
        DebugUtils.info(this, "Thread start");
        while (running) {
            if (update()) draw();
            try {
                Thread.sleep( (long) gameContext.getUpdateTime());
            } catch (InterruptedException e) {}
        }
        DebugUtils.info(this, "Thread stop");
    }

    public void setOnCloseListener(CloseListener closeListener) {
        mCloseListener = closeListener;
    }

    public interface CloseListener {
        void onClose();
    }

}

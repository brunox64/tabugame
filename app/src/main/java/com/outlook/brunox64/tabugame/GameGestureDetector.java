package com.outlook.brunox64.tabugame;

import android.view.MotionEvent;

/**
 * Created by bruno on 18/09/2016.
 */
public class GameGestureDetector {

    private GameContext ctx = GameContext.get();

    private Coordenada startDown;

    private Listener clickListener;

    public interface Listener {
        boolean onClick(MotionEvent e);
    }

    public GameGestureDetector(Listener listener) {
        clickListener = listener;
    }

    public boolean onTouchEvent(MotionEvent e) {
        boolean result = false;

        switch(e.getAction()) {

            case MotionEvent.ACTION_DOWN:

                startDown = new Coordenada(e.getX(),e.getY());
                result = true;

                break;

            case MotionEvent.ACTION_UP:

                if ((startDown != null) && (Math.abs(startDown.getX()-e.getX()) < ctx.toPixel(30) && Math.abs(startDown.getY()-e.getY()) < ctx.toPixel(30))) {
                    DebugUtils.info(this,"GameGestureDetector.click");
                    result = clickListener.onClick(e);
                }

                break;
        }

        return result;
    }
}

package com.outlook.brunox64.tabugame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bruno on 28/08/2016.
 */
public class HomeView extends View {

    private Paint paintLine;
    private Paint paintFill;
    private Paint paintText;
    private Paint paintPerc;
    private Paint paintTextPerc;
//    private Paint paintShadow;
//    private LinearGradient linGrad;
//    private Matrix linGradMatrix;
    private float dens;
    private int colorPrimary;
    private List<ButtonTab> buttons;
    private Coordenada startDown;
    private ClickBtnListener mClickBtnListener;

    public HomeView(Context ctx) {
        this(ctx, null);
    }

    public HomeView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);

        Resources res = getContext().getResources();

        dens = res.getDisplayMetrics().density;
        colorPrimary = 0x3F51B5;

        paintText = new Paint();
        paintText.setFilterBitmap(true);
        paintText.setAntiAlias(true);
        paintText.setColor(Color.WHITE);
        paintText.setAlpha(255);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(toPixel(37.5f));
        paintText.setFakeBoldText(true);
        paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        paintLine = new Paint();
        paintLine.setFilterBitmap(true);
        paintLine.setAntiAlias(true);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(toPixel(10));
        paintLine.setColor(colorPrimary);
        paintLine.setAlpha(255);

        paintFill = new Paint();
        paintFill.setFilterBitmap(true);
        paintFill.setAntiAlias(true);
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setStrokeWidth(toPixel(10));
        paintFill.setColor(colorPrimary);
        paintFill.setAlpha(255);

        paintPerc = new Paint();
        paintPerc.setFilterBitmap(true);
        paintPerc.setAntiAlias(true);
        paintPerc.setStyle(Paint.Style.STROKE);
        paintPerc.setStrokeWidth(toPixel(5f));
        paintPerc.setColor(Color.GREEN);
        paintPerc.setAlpha(255);

        paintTextPerc = new Paint();
        paintTextPerc.setFilterBitmap(true);
        paintTextPerc.setAntiAlias(true);
        paintTextPerc.setColor(Color.WHITE);
        paintTextPerc.setAlpha(255);
        paintTextPerc.setTextAlign(Paint.Align.CENTER);
        paintTextPerc.setTextSize(toPixel(12.5f));
        paintTextPerc.setFakeBoldText(false);
        paintTextPerc.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

//        linGrad = new LinearGradient(0f, 0f, 0f, toPixel(75f), Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP);
//        linGradMatrix = new Matrix();
//        linGrad.setLocalMatrix(linGradMatrix);
//
//        paintShadow = new Paint();
//        paintShadow.setStyle(Paint.Style.FILL);
//        paintShadow.setColor(Color.GREEN);
//        paintShadow.setShader(linGrad);

        setFocusable(true);

        GameContext game = GameContext.get();

        buttons = new ArrayList<>();
        Map<String,Long> resultados = game.getResultados();
        Map<String,List<Question>> tabuadas = game.buildTabuadas();
        ButtonTab btn;
        for (int i = 2; i < 10; i++) {
            buttons.add(btn = new ButtonTab(i, Operacao.SOMA));
            calcularBtnPerc(btn, tabuadas, resultados);
            buttons.add(btn = new ButtonTab(i, Operacao.SUBTRACAO));
            calcularBtnPerc(btn, tabuadas, resultados);
            buttons.add(btn = new ButtonTab(i, Operacao.MULTIPLICACAO));
            calcularBtnPerc(btn, tabuadas, resultados);
            buttons.add(btn = new ButtonTab(i, Operacao.DIVISAO));
            calcularBtnPerc(btn, tabuadas, resultados);
        }

        DebugUtils.info(this, "new HomeView");
    }

    private void calcularBtnPerc(ButtonTab btn, Map<String,List<Question>> tabuadas, Map<String,Long> resultados) {
        GameContext game = GameContext.get();
        int bons = 0;
        for (Question q : tabuadas.get(game.getKeyTabuada(btn.getTab(),btn.getOper()))) {
            q.time = resultados.get(game.getKeyResultado(q.oper, q.a, q.b));
            if (q.time <= 2000l) bons++;
        }
        //DebugUtils.info(this, "Perc btn:" + btn.getLabel() + ":" + bons);
        btn.setPerc(bons*10f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DebugUtils.info(this, "onMeasure: widthMeasureSpec:" + widthMeasureSpec + ", heigthMeasureSpec:" + heightMeasureSpec);

        float w = toDip(MeasureSpec.getSize(widthMeasureSpec));

        float btnW = 75f;
        float btnH = 75f;
        float btnXL = w * 0.125f + btnW / 2f;
        float btnXR = w * 0.875f - btnW / 2f;

        float y = w * 0.10f + btnH / 2f; // Ínicio.
        float x;
        ButtonTab btn;
        for (int i = 0; i < buttons.size(); i++) {
            x = i % 2 == 0 ? btnXL : btnXR;
            btn = buttons.get(i);
            btn.setX(x);
            btn.setY(y + (float) i * btnH);
            btn.setW(btnW);
            btn.setH(btnH);
        }

        setMeasuredDimension(widthMeasureSpec, (int) toPixel(buttons.size() * btnH + y));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        DebugUtils.info(this, "onSizeChanged: w:" + w + ", h:" + h + ", oldw:" + oldw + ", oldh:" + oldh);

    }

    private float toPixel(float dp) {
        return dp * dens;
    }

    private float toDip(float p) {
        if (Math.abs(p) <= 0f) return 0f;
        return p / dens;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ButtonTab btn1 = null;
        ButtonTab btn2 = null;
        for (int i = 0; i < buttons.size() - 1; i++) {
            btn1 = buttons.get(i);
            btn2 = buttons.get(i + 1);

            canvas.drawLine(toPixel(btn1.getX()),
                    toPixel(btn1.getY()),
                    toPixel(btn2.getX()),
                    toPixel(btn2.getY()), paintLine);
        }

        for (ButtonTab btn : buttons) {
            canvas.drawCircle(toPixel(btn.getX()), toPixel(btn.getY()),
                    toPixel(Math.max(btn.getW(), btn.getH()) / 2f), paintFill);

//            linGradMatrix.reset();
//            linGradMatrix.setTranslate(toPixel(btn.getX()), toPixel(btn.getY() + 10f));
//            linGrad.setLocalMatrix(linGradMatrix);

            canvas.drawCircle(toPixel(btn.getX()), toPixel(btn.getY()),
                    toPixel(Math.max(btn.getW(), btn.getH()) / 2f), paintLine);

            canvas.drawCircle(toPixel(btn.getX()), toPixel(btn.getY()),
                    toPixel(Math.max(btn.getW(), btn.getH()) / 2f), paintFill);

            canvas.drawText(btn.getLabel(), toPixel(btn.getX()),
                    toPixel(btn.getY() + toDip(paintText.getTextSize()) / 4f), paintText);

            canvas.drawArc(toPixel(btn.getX() - btn.getW() / 2f), toPixel(btn.getY() - btn.getH() / 2f),
                    toPixel(btn.getX() + btn.getW() / 2), toPixel(btn.getY() + btn.getH() / 2f),
                    0f, btn.getPerc() / 100f * 360f, false, paintPerc);

            canvas.drawText(Math.round(btn.getPerc()) + "%",
                    toPixel(btn.getX()),
                    toPixel(btn.getY() + btn.getH()*0.35f), paintTextPerc);

            //canvas.drawArc(toPixel(70f), toPixel(70f), toPixel(100f), toPixel(100f), 0f, 360f, false, paintPerc);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //DebugUtils.info(this, "onTouchEvent");

        boolean result = false;

        switch(e.getAction()) {

            case MotionEvent.ACTION_DOWN:

                //DebugUtils.info(this, "ACTION_DOWN");

                startDown = new Coordenada(e.getX(),e.getY());
                result = true;

                break;

            case MotionEvent.ACTION_UP:

                //DebugUtils.info(this, "ACTION_UP");

                if ((startDown != null) && (Math.abs(startDown.getX()-e.getX()) < toPixel(30) && Math.abs(startDown.getY()-e.getY()) < toPixel(30))) {

                    Iterator<ButtonTab> btnIter = buttons.iterator();
                    ButtonTab btn = null;
                    while (btnIter.hasNext()) {
                        btn = btnIter.next();
                        if (MouseUtils.entre(btn.getX()-btn.getW()/2f,btn.getY()-btn.getH()/2f,btn.getW(),btn.getH(),toDip(e.getX()),toDip(e.getY()))) {

                            DebugUtils.info(this, "clicou");

                            mClickBtnListener.onClick(btn);

                            result = true;

                            break;
                        }
                    }
                }

                break;
        }

        return result;
    }

    public void setOnClickBtnListener(ClickBtnListener clickListener) {
        mClickBtnListener = clickListener;
    }

    public interface ClickBtnListener {
        void onClick(ButtonTab btn);
    }
}

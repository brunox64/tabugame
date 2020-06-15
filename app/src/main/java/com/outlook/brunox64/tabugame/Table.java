package com.outlook.brunox64.tabugame;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by bruno on 18/09/2016.
 */
public class Table extends EventDispacher {

    private GameContext ctx = GameContext.get();

    private List<Block> blocks,selects;
    private List<List<Block>> cols;
    private int x,y,w,h,qtdX,qtdY,animations;
    private float bm,bx,by,bw,bh;
    private GameGestureDetector detector;
    private boolean toDraw = true;
    private long timeSugerir;
    private long timePiscar;
    private long timeResposta;
    private boolean piscar;
    private List<Block> sugeridos;
    public Table() {
        super();

        detector = new GameGestureDetector(new GameGestureDetector.Listener() {
            @Override
            public boolean onClick(MotionEvent e) {
                return click(e);
            }
        });
    }

    public void resize(int x, int y, int w, int h) {
        DebugUtils.info(this,"Table x:" + x + ", y:" + y + ", w:" + w + ", h:" + h);

        if (this.x == x && this.y == y && this.w == w && this.h == h) return;

        this.blocks = new LinkedList<>();
        this.selects = new LinkedList<>();

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.qtdX = Math.round (this.w/ctx.toPixel(70f));
        this.qtdY = Math.round (this.h/ctx.toPixel(70f));
        this.animations = 0;
        this.cols = new LinkedList();
        for (int i = 0; i < this.qtdX; i++) {
            this.cols.add(new LinkedList<Block>());
        }

        int qtdX = this.qtdX;
        int qtdY = this.qtdY;

        if ((w > h && this.qtdX < this.qtdY) ||
                (w < h && this.qtdX > this.qtdY)) {
            this.qtdX = qtdY;
            this.qtdY = qtdX;
        }

        this.bm = ctx.toPixel(2f);
        this.bx = x+this.bm;
        this.by = y+this.bm;
        this.bw = (w-this.bm*2f)/this.qtdX;
        this.bh = (h-this.bm*2f)/this.qtdY;
        this.bw = this.bw-this.bm*2f;
        this.bh = this.bh-this.bm*2f;

        toDraw = true;
        timeSugerir = System.currentTimeMillis();
    }

    public boolean isAnimando() {
        return animations != 0;
    }

    public void beforeAnimation() {
        if (this.animations == 0) {
            this.dispatchEvent("beforeAnimation");
        }
        this.animations++;
    }

    public void afterAnimation() {
        this.animations--;
        if (this.animations == 0) {
            this.dispatchEvent("afterAnimation");
        }
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void remItem(Block item) {
        for (List<Block> col : cols) {
            int i = col.indexOf(item);
            if (i > -1) {
                col.remove(i);
                for (int j = i - 1; j >= 0; j--) {
                    col.get(j).abaixo();
                }
                break;
            }
        }
        toDraw = true;
    }

    public void norm() {
        norm(false);
    }

    public void norm(boolean animando) {

        DebugUtils.info(this, "norm");

        List<Block> blocks = this.blocks;
        float m = this.bm;
        float x = this.bx;
        float y = this.by;
        float w = this.bw;
        float h = this.bh;

        QuestionGenerator questions = ctx.getQuestionGenerator();

        //Block b = null;
        for (int i = 0; i < cols.size(); i++) {
            List<Block> col = cols.get(i);
            List<Block> added = new LinkedList<>();
            for (int j = this.qtdY - col.size() - 1; j >= 0; j--) {
                Block b = new Block(questions.next());
                b.addEventListener("beforeAnimation",new EventDispacher.Listener(){
                    @Override
                    public void invoke(Object arg) {
                        beforeAnimation();
                    }
                });
                b.addEventListener("afterAnimation",new EventDispacher.Listener(){
                    @Override
                    public void invoke(Object arg) {
                        afterAnimation();
                    }
                });
                b.setX(x+m+((w+m*2)*i));
                b.setY(y+m);
                b.setW(w);
                b.setH(h);
                b.setM(m);
                col.add(0,b);
                blocks.add(0,b);
                added.add(0,b);
            }
            if (animando) {
                for (int j = 0; j < added.size(); j++) {
                    Block b = added.get(j);
                    for (int k = j; k < added.size(); k++) {
                        b.acima();
                        b.abaixo();
                    }
                    if (j > 0) {
                        for (int k = 0; k < j; k++) {
                            b.abaixo();
                        }
                    }
                }
            } else {
                for (int j = 1; j < added.size(); j++) {
                    Block b = added.get(j);
                    for (int k = 0; k < j; k++) {
                        b.abaixoInst();
                    }
                }
            }
        }

        timeResposta = System.currentTimeMillis();
        toDraw = true;
    }

    private boolean combinou(int oper, int a, int b, int c) {
        return (oper == Operacao.SOMA && (a + b == c))
                ||(oper == Operacao.SUBTRACAO && (a - b == c))
                ||(oper == Operacao.MULTIPLICACAO && (a * b == c))
                ||(oper == Operacao.DIVISAO && (Math.abs((float)a / (float)b - (float)c) <= 0.000f));
    }

    private void procurarABC(int oper, int tab, Map<String,Block> abc) {
        for (Block a : blocks) {
            if (Operacao.SUBTRACAO == oper || Operacao.DIVISAO == oper || tab == a.getNum()) {
                abc.put("a", a);
                procurarBC(oper, tab, abc);
                if (abc.get("c") != null && abc.get("b") != null) break;
            }
        }
    }

    private void procurarBC(int oper, int tab, Map<String,Block> abc) {
        Block a = abc.get("a");
        for (Block b : blocks) {
            if (a != b
                    && ((Operacao.SUBTRACAO == oper && tab == b.getNum())
                        || (Operacao.DIVISAO == oper && tab == b.getNum())
                        || (Operacao.SOMA == oper)
                        || (Operacao.MULTIPLICACAO == oper))) {
                abc.put("b", b);
                procucarC(oper, abc);
                if (abc.get("c") != null) break;
            }
        }
    }

    private void procucarC(int oper, Map<String,Block> result) {
        Block a = result.get("a");
        Block b = result.get("b");
        for (Block c : blocks) {
            if (a != c && b != c && combinou(oper, a.getNum(), b.getNum(), c.getNum())) {
                result.put("c", c);
                break;
            }
        }
    }

    private void removerSugestao() {
        if (sugeridos != null) {
            for (Block su : sugeridos) {
                su.desmarcarSugestao();
            }
            sugeridos = null;
        }
        timeSugerir = System.currentTimeMillis();
    }

    private void fazerSugestao() {
        DebugUtils.info(this, "Sugerindo!");

        int tab = ctx.getQuestionGenerator().getTab();
        int oper = ctx.getQuestionGenerator().getOper();

        Map<String,Block> abc = new HashMap<>();

        if (selects.size() > 0) {
            abc.put("a", selects.get(0));
            if (selects.size() > 1) {
                abc.put("b", selects.get(1));
                procucarC(oper, abc);
            } else {
                procurarBC(oper, tab, abc);
            }
            if (abc.size() < 3) {
                abc.clear();
            }
        }

        if (abc.isEmpty()) procurarABC(oper, tab, abc);

        if (abc.size() == 3) {
            Block a = abc.get("a");
            Block b = abc.get("b");
            Block c = abc.get("c");
            a.marcarSugestao();
            b.marcarSugestao();
            c.marcarSugestao();
            sugeridos = new LinkedList<>();
            sugeridos.add(a);
            sugeridos.add(b);
            sugeridos.add(c);
        }
    }

    private void piscarSugestao() {
        for (Block b : sugeridos) {
            if (piscar) b.marcarSugestao();
            else b.desmarcarSugestao();
        }
        piscar = ! piscar;
        timePiscar = System.currentTimeMillis();
        toDraw = true;
    }

    public boolean onTouchEvent(MotionEvent e) {
        return detector.onTouchEvent(e);
    }

    private boolean click(MotionEvent e) {
        DebugUtils.info(this,"Table.click");
        MouseUtils mu = new MouseUtils();

        Block block = null;
        for (int i = 0; i < blocks.size(); i++) {
            //DebugUtils.info(this,"GET block");
            block = blocks.get(i);
            if (mu.entre(block.getX(),
                    block.getY(),
                    block.getW(),
                    block.getH(),
                    e.getX(),
                    e.getY())) {
                if (block.isSelected()) {
                    //DebugUtils.info(this,"unselect");
                    block.unSelect();
                    this.selects.remove(block);
                } else {
                    //DebugUtils.info(this,"SELECT");
                    block.select();
                    this.selects.add(block);
                }
                if (this.selects.size() == 3) {
                    DebugUtils.info(this,"verificando selecao");
                    int oper = ctx.getQuestionGenerator().getOper();
                    int a = this.selects.get(0).getNum();
                    int b = this.selects.get(1).getNum();
                    int c = this.selects.get(2).getNum();
                    if (combinou(oper, a, b, c)) {

                        ctx.registrarResultado(oper, a, b, System.currentTimeMillis()-timeResposta);
                        timeResposta = System.currentTimeMillis();

                        for (Block b2 : selects) {
                            b2.hide();
                            remItem(b2);
                        }
                        this.selects = new LinkedList<>();
                        removerSugestao();
                    } else {
                        this.selects.remove(selects.size()-1);
                        block.unSelect();
                        block.errou();
                        removerSugestao();
                        fazerSugestao();
                    }
                }
                break;
            }
        }

        DebugUtils.info(this,"limpando blocks");
        List<Block> removerList = new LinkedList<>();
        for (Block b : blocks) {
            if ( ! b.isVisivel()) removerList.add(b);
        }
        blocks.removeAll(removerList);
        this.norm(true);

        toDraw = true;

        return true;
    }

    public boolean update() {

        if (sugeridos == null && System.currentTimeMillis()-timeSugerir > 6000l) {
            fazerSugestao();
        }

        for (Block b : blocks) {
            if(b.update()) toDraw = true;
        }

        if (sugeridos != null && System.currentTimeMillis() - timePiscar > 500l) {
            piscarSugestao();
        }

        return toDraw;
    }

    public void draw(Canvas c) {
        toDraw = false;
        for (Block b : blocks) {
            b.draw(c);
        }
        for (Block b : selects) {
            b.draw(c);
        }
    }
}

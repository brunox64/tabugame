package com.outlook.brunox64.tabugame;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno on 18/09/2016.
 */
public class QuestionGenerator {

    private int oper,tab;
    private List<Integer> questions;
    private int index = 0;

    public void setOperAndTab(int oper, int tab) {
        if ("+-/x".indexOf(Operacao.getSinal(oper)) == -1) throw new RuntimeException("Operação não permitida (operações + - * /).");
        if (tab < 2 || tab > 9) throw new RuntimeException("Tabuada não permitida (tabuadas 2,3,4,5,6,7,8,9).");

        this.oper = oper;
        this.tab = tab;

        index = 0;
        gerarPerguntas();
        embaralharPerguntas();
    }

    private void gerarPerguntas() {
        DebugUtils.info(this, "Gerando Pergutnas");

        this.questions = new ArrayList<Integer>();
        for (int i = 1; i <= 10; i++) {
            if (Operacao.SOMA == oper || Operacao.SUBTRACAO == oper) {
                questions.add(tab);
                questions.add(i);
                questions.add(tab + i);
            } else if (Operacao.MULTIPLICACAO == oper|| Operacao.DIVISAO == oper) {
                questions.add(tab);
                questions.add(i);
                questions.add(tab * i);
            }
        }
    }

    private void embaralharPerguntas() {
        DebugUtils.info(this, "Embaralhando Perguntas");

        List<Integer> list = questions;
        int newIndex = 0;
        int oldIndex = 0;
        Integer o = null;
        Integer n = null;
        for (int i = 0; i < list.size(); i++) {
            newIndex = nextIndexTab();
            oldIndex = nextIndexTab();
            o = list.get(oldIndex);
            n = list.get(newIndex);
            list.set(newIndex, o);
            list.set(oldIndex, n);
        }
    }

    private int nextIndexTab() {
        return BigDecimal.valueOf(Math.round(Math.random() * 29)).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    public int next() {
        int term = questions.get(index++);
        if (index == questions.size()) {
            index = 0;
        }
        return term;
    }

    public int getOper() {
        return oper;
    }
    public int getTab() {
        return tab;
    }
}

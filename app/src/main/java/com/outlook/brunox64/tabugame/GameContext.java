package com.outlook.brunox64.tabugame;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruno on 15/09/2016.
 */
public class GameContext {

    private static GameContext gameContext;

    private Map<String,Long> resultados;
    private float dens;
    private int colorPrimary = 0xFF3F51B5;
    private int updateTime = 60;
    private QuestionGenerator questionGenerator;

    public GameContext(float density) {
        DebugUtils.info(this,"GameContext criado");

        dens = density;
        resultados = new HashMap();
        questionGenerator = new QuestionGenerator();

        gameContext = this;
    }

    public static GameContext get() {
        return gameContext;
    }

    public float getUpdateTime() {
        return updateTime;
    }

    public int getColorPrimary() {
        return colorPrimary;
    }

    public QuestionGenerator getQuestionGenerator() {
        return questionGenerator;
    }

    public void setOperAndTab(int oper, int tab) {
        questionGenerator.setOperAndTab(oper, tab);
    }

    public String getKeyResultado(int oper, int a, int b) {
        return a + ":" + Operacao.getSinal(oper) + ":" + b;
    }

    public String getKeyTabuada(int tab, int oper) {
        return tab + Operacao.getSinal(oper);
    }

    public void registrarResultado(int oper, int a, int b, long tempo) {
        resultados.put(getKeyResultado(oper, a, b), tempo);
    }

    public void persistirResutados(SharedPreferences sharedPref) {
        DebugUtils.info(this, "persistindo resultados");
        SharedPreferences.Editor editor = sharedPref.edit();
        for (Map.Entry<String,Long> resp : resultados.entrySet()) {
            //DebugUtils.info(this, "persistindo key:" + resp.getKey());
            editor.putLong(resp.getKey(), resp.getValue());
        }
        editor.commit();
    }

    public Map<String,List<Question>> buildTabuadas() {
        Map<String,List<Question>> tabuadas = new HashMap();
        List<Question> questionsSO = null;
        List<Question> questionsSU = null;
        List<Question> questionsMU = null;
        List<Question> questionsDI = null;
        Question question = null;
        for (int a = 2; a <= 9; a++) {
            tabuadas.put(getKeyTabuada(a, Operacao.SOMA), questionsSO = new ArrayList<>());
            tabuadas.put(getKeyTabuada(a, Operacao.SUBTRACAO), questionsSU = new ArrayList<>());
            tabuadas.put(getKeyTabuada(a, Operacao.MULTIPLICACAO), questionsMU = new ArrayList<>());
            tabuadas.put(getKeyTabuada(a, Operacao.DIVISAO), questionsDI = new ArrayList<>());
            for (int b = 1; b <= 10; b++) {
                question = new Question();
                question.a = a;
                question.b = b;
                question.c = a + b;
                question.oper = Operacao.SOMA;
                questionsSO.add(question);

                question = new Question();
                question.a = a + b;
                question.b = b;
                question.c = a;
                question.oper = Operacao.SUBTRACAO;
                questionsSU.add(question);

                question = new Question();
                question.a = a;
                question.b = b;
                question.c = a * b;
                question.oper = Operacao.MULTIPLICACAO;
                questionsMU.add(question);

                question = new Question();
                question.a = a * b;
                question.b = b;
                question.c = a;
                question.oper = Operacao.DIVISAO;
                questionsDI.add(question);
            }
        }
        return tabuadas;
    }

    public void recuperarResultados(SharedPreferences sharedPref) {
        DebugUtils.info(this, "recuperando resultados");
        for (List<Question> questions : buildTabuadas().values()) {
            for (Question q : questions) {
                registrarResultado(q.oper, q.a, q.b, sharedPref.getLong(getKeyResultado(q.oper, q.a, q.b), 6000l));
            }
        }
    }

    public Map<String,Long> getResultados() {
        return resultados;
    }

    public float toPixel(float dp) {
        return dp * dens;
    }

    public float toDip(float p) {
        if (Math.abs(p) <= 0f) return 0f;
        return p / dens;
    }
}

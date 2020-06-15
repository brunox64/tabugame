package com.outlook.brunox64.tabugame;

/**
 * Created by bruno on 15/09/2016.
 */
public class Operacao {
    public static final int SOMA = 0;
    public static final int SUBTRACAO = 1;
    public static final int MULTIPLICACAO = 2;
    public static final int DIVISAO = 3;
    public static String getSinal(int oper) {
        String label = null;
        switch (oper) {
            case Operacao.MULTIPLICACAO:
                label = "x";
                break;
            case Operacao.DIVISAO:
                label = "/";
                break;
            case Operacao.SOMA:
                label = "+";
                break;
            case Operacao.SUBTRACAO:
                label = "-";
                break;
        }
        return label;
    }
    public static String getExtenso(int oper) {
        String title = null;
        switch (oper) {
            case Operacao.SOMA:
                title = "Soma";
                break;
            case Operacao.SUBTRACAO:
                title = "Subtração";
                break;
            case Operacao.MULTIPLICACAO:
                title = "Multiplicação";
                break;
            case Operacao.DIVISAO:
                title = "Divisão";
                break;
        }
        return title;
    }
}

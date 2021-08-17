package com.consultmais.consultmais.Model;

import java.io.Serializable;
import java.util.List;

public class PerguntaOpcaoQuantidade implements Serializable {

    private int PerguntaOpcaoQuantidadeID;
    private int FormularioIDFK;
    private int PerguntaPaiIDFK;
    private int PerguntaOpcaoPaiIDFK;
    private int PerguntaFilhaIDFK;
    private int PerguntaOpcaoFilhaIDFK;
    private int Quantidade;

    public PerguntaOpcaoQuantidade () {
    }

    public int getPerguntaOpcaoQuantidadeID () {
        return PerguntaOpcaoQuantidadeID;
    }

    public void setPerguntaOpcaoQuantidadeID (int perguntaOpcaoQuantidadeID) {
        PerguntaOpcaoQuantidadeID = perguntaOpcaoQuantidadeID;
    }

    public int getFormularioIDFK () {
        return FormularioIDFK;
    }

    public void setFormularioIDFK (int formularioIDFK) {
        FormularioIDFK = formularioIDFK;
    }

    public int getPerguntaPaiIDFK () {
        return PerguntaPaiIDFK;
    }

    public void setPerguntaPaiIDFK (int perguntaPaiIDFK) {
        PerguntaPaiIDFK = perguntaPaiIDFK;
    }

    public int getPerguntaOpcaoPaiIDFK () {
        return PerguntaOpcaoPaiIDFK;
    }

    public void setPerguntaOpcaoPaiIDFK (int perguntaOpcaoPaiIDFK) {
        PerguntaOpcaoPaiIDFK = perguntaOpcaoPaiIDFK;
    }

    public int getPerguntaFilhaIDFK () {
        return PerguntaFilhaIDFK;
    }

    public void setPerguntaFilhaIDFK (int perguntaFilhaIDFK) {
        PerguntaFilhaIDFK = perguntaFilhaIDFK;
    }

    public int getPerguntaOpcaoFilhaIDFK () {
        return PerguntaOpcaoFilhaIDFK;
    }

    public void setPerguntaOpcaoFilhaIDFK (int perguntaOpcaoFilhaIDFK) {
        PerguntaOpcaoFilhaIDFK = perguntaOpcaoFilhaIDFK;
    }

    public int getQuantidade () {
        return Quantidade;
    }

    public void setQuantidade (int quantidade) {
        Quantidade = quantidade;
    }

}

package com.consultmais.consultmais.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class Resposta implements Serializable {

    private Integer PerguntaPaiIDFK;
    private Integer PerguntaOpcaoPaiIDFK;
    private int perguntaIDFK;
    private String resposta;


    public Resposta () {}

    public Integer getPerguntaPaiIDFK () {
        return PerguntaPaiIDFK;
    }

    public void setPerguntaPaiIDFK (Integer perguntaPaiIDFK) {
        PerguntaPaiIDFK = perguntaPaiIDFK;
    }

    public Integer getPerguntaOpcaoPaiIDFK () {
        return PerguntaOpcaoPaiIDFK;
    }

    public void setPerguntaOpcaoPaiIDFK (Integer perguntaOpcaoPaiIDFK) {
        PerguntaOpcaoPaiIDFK = perguntaOpcaoPaiIDFK;
    }

    public int getPerguntaIDFK () {
        return perguntaIDFK;
    }

    public void setPerguntaIDFK (int perguntaIDFK) {
        this.perguntaIDFK = perguntaIDFK;
    }

    public String getResposta () {
        return resposta;
    }

    public void setResposta (String resposta) {
        this.resposta = resposta;
    }

}

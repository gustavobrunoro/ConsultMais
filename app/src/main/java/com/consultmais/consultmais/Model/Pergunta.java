package com.consultmais.consultmais.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pergunta implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int PerguntaID;
    private int FormularioIDFK;
    private String Descricao;
    private int PerguntaTipoIDFK;
    private boolean Obrigatoria;
    private int Ordem;
    private int MinimoRespostas;
    private int MaximoRespostas;
    private String TipoPergunta;
    private int Parametrizada;
    @Ignore
    private List<Perguntaopcao> perguntaOpcao = new ArrayList<>();

    public Pergunta () {
    }

    public int getPerguntaID () {
        return PerguntaID;
    }

    public void setPerguntaID (int perguntaID) {
        PerguntaID = perguntaID;
    }

    public int getFormularioIDFK () {
        return FormularioIDFK;
    }

    public void setFormularioIDFK (int formularioIDFK) {
        FormularioIDFK = formularioIDFK;
    }

    public String getDescricao () {
        return Descricao;
    }

    public void setDescricao (String descricao) {
        Descricao = descricao;
    }

    public int getPerguntaTipoIDFK () {
        return PerguntaTipoIDFK;
    }

    public void setPerguntaTipoIDFK (int perguntaTipoIDFK) {
        PerguntaTipoIDFK = perguntaTipoIDFK;
    }

    public boolean isObrigatoria () {
        return Obrigatoria;
    }

    public void setObrigatoria (boolean obrigatoria) {
        Obrigatoria = obrigatoria;
    }

    public int getOrdem () {
        return Ordem;
    }

    public void setOrdem (int ordem) {
        Ordem = ordem;
    }

    public int getMinimoRespostas () {
        return MinimoRespostas;
    }

    public void setMinimoRespostas (int minimoRespostas) {
        MinimoRespostas = minimoRespostas;
    }

    public int getMaximoRespostas () {
        return MaximoRespostas;
    }

    public void setMaximoRespostas (int maximoRespostas) {
        MaximoRespostas = maximoRespostas;
    }

    public String getTipoPergunta () {
        return TipoPergunta;
    }

    public void setTipoPergunta (String tipoPergunta) {
        TipoPergunta = tipoPergunta;
    }

    public int getParametrizada () {
        return Parametrizada;
    }

    public void setParametrizada (int parametrizada) {
        Parametrizada = parametrizada;
    }

    public List<Perguntaopcao> getPerguntaOpcao () {
        return perguntaOpcao;
    }

    public void setPerguntaOpcao (List<Perguntaopcao> perguntaOpcao) {
        this.perguntaOpcao = perguntaOpcao;
    }
}

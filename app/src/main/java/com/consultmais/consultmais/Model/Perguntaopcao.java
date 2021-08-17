package com.consultmais.consultmais.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.consultmais.consultmais.Database.Help.DataConverterPerguntaOpcaoQuantidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Perguntaopcao implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int PerguntaOpcao;
    private int PerguntaIDFK;
    private String Descricao;
    private int Ordem;
    @TypeConverters(DataConverterPerguntaOpcaoQuantidade.class)
    private List<PerguntaOpcaoQuantidade> PerguntaOpcaoQuantidade = new ArrayList<>();

    public Perguntaopcao () {
    }

    public int getPerguntaOpcao () {
        return PerguntaOpcao;
    }

    public void setPerguntaOpcao (int perguntaOpcao) {
        this.PerguntaOpcao = perguntaOpcao;
    }

    public int getPerguntaIDFK () {
        return PerguntaIDFK;
    }

    public void setPerguntaIDFK (int perguntaIDFK) {
        this.PerguntaIDFK = perguntaIDFK;
    }

    public String getDescricao () {
        return Descricao;
    }

    public void setDescricao (String descricao) {
        this.Descricao = descricao;
    }

    public int getOrdem () {
        return Ordem;
    }

    public void setOrdem (int ordem) {
        this.Ordem = ordem;
    }

    public List<PerguntaOpcaoQuantidade> getPerguntaOpcaoQuantidade () {
        return PerguntaOpcaoQuantidade;
    }

    public void setPerguntaOpcaoQuantidade (List<PerguntaOpcaoQuantidade> perguntaOpcaoQuantidade) {
        this.PerguntaOpcaoQuantidade = perguntaOpcaoQuantidade;
    }
}

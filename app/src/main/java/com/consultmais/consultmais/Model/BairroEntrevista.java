package com.consultmais.consultmais.Model;

import java.io.Serializable;

public class BairroEntrevista implements Serializable {

    private int BairroEntrevistaID;
    private int FormularioIDFK;
    private String Descricao;
    private int BairroIDFK;
    private  int Ordem;
    private int Quantidade;

    public BairroEntrevista () {
    }

    public int getBairroEntrevistaID () {
        return BairroEntrevistaID;
    }

    public void setBairroEntrevistaID (int bairroEntrevistaID) {
        BairroEntrevistaID = bairroEntrevistaID;
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

    public int getBairroIDFK () {
        return BairroIDFK;
    }

    public void setBairroIDFK (int bairroIDFK) {
        BairroIDFK = bairroIDFK;
    }

    public int getOrdem () {
        return Ordem;
    }

    public void setOrdem (int ordem) {
        Ordem = ordem;
    }

    public int getQuantidade () {
        return Quantidade;
    }

    public void setQuantidade (int quantidade) {
        Quantidade = quantidade;
    }
}

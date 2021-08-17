package com.consultmais.consultmais.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Entity
public class Notificacao {

    //@PrimaryKey(autoGenerate = true)
    private int NotificacaoID;
    private String Descricao;
    private int TipoNotificacaoIDFK;
    private int Visualizada;

    public Notificacao () {
    }

    public int getNotificacaoID () {
        return NotificacaoID;
    }

    public void setNotificacaoID (int notificacaoID) {
        NotificacaoID = notificacaoID;
    }

    public String getDescricao () {
        return Descricao;
    }

    public void setDescricao (String descricao) {
        Descricao = descricao;
    }

    public int getTipoNotificacaoIDFK () {
        return TipoNotificacaoIDFK;
    }

    public void setTipoNotificacaoIDFK (int tipoNotificacaoIDFK) {
        TipoNotificacaoIDFK = tipoNotificacaoIDFK;
    }

    public int getVisualizada () {
        return Visualizada;
    }

    public void setVisualizada (int visualizada) {
        Visualizada = visualizada;
    }
}

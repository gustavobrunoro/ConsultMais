package com.consultmais.consultmais.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.consultmais.consultmais.Database.Help.DataConverterResposta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FormularioResposta implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int FormularioRespostaID;
    private int FormularioIDFK;
    private int EntrevistadorIDFK;
    private int BairroEntrevistaIDFK;

    @TypeConverters(DataConverterResposta.class)
    private List<Resposta> Resposta = new ArrayList<>();
    private String NumeroFormularioMobile;
    private double Longitude;
    private double Latitude;
    private String InicioEntrevista;
    private String FimEntrevista;
    private int Exportado;

    public FormularioResposta () {
    }

    public int getFormularioRespostaID () {
        return FormularioRespostaID;
    }

    public void setFormularioRespostaID (int formularioRespostaID) {
        FormularioRespostaID = formularioRespostaID;
    }

    public int getFormularioIDFK () {
        return FormularioIDFK;
    }

    public void setFormularioIDFK (int formularioIDFK) {
        FormularioIDFK = formularioIDFK;
    }

    public int getEntrevistadorIDFK () {
        return EntrevistadorIDFK;
    }

    public void setEntrevistadorIDFK (int entrevistadorIDFK) {
        EntrevistadorIDFK = entrevistadorIDFK;
    }

    public int getBairroEntrevistaIDFK () {
        return BairroEntrevistaIDFK;
    }

    public void setBairroEntrevistaIDFK (int bairroEntrevistaIDFK) {
        BairroEntrevistaIDFK = bairroEntrevistaIDFK;
    }

    public List<com.consultmais.consultmais.Model.Resposta> getResposta () {
        return Resposta;
    }

    public void setResposta (List<com.consultmais.consultmais.Model.Resposta> resposta) {
        Resposta = resposta;
    }

    public String getNumeroFormularioMobile () {
        return NumeroFormularioMobile;
    }

    public void setNumeroFormularioMobile (String numeroFormularioMobile) {
        NumeroFormularioMobile = numeroFormularioMobile;
    }

    public double getLongitude () {
        return Longitude;
    }

    public void setLongitude (double longitude) {
        Longitude = longitude;
    }

    public double getLatitude () {
        return Latitude;
    }

    public void setLatitude (double latitude) {
        Latitude = latitude;
    }

    public String getInicioEntrevista () {
        return InicioEntrevista;
    }

    public void setInicioEntrevista (String inicioEntrevista) {
        InicioEntrevista = inicioEntrevista;
    }

    public String getFimEntrevista () {
        return FimEntrevista;
    }

    public void setFimEntrevista (String fimEntrevista) {
        FimEntrevista = fimEntrevista;
    }

    public int getExportado () {
        return Exportado;
    }

    public void setExportado (int exportado) {
        Exportado = exportado;
    }
}

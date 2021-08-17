package com.consultmais.consultmais.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.consultmais.consultmais.Database.Help.DataConverterBairroEntrevista;
import com.consultmais.consultmais.Database.Help.DataConverterPergunta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(  primaryKeys = {"FormularioID","EntrevistadorIDFK"} )
public class Formulario implements Serializable {

    private int FormularioID;
    private int ClienteIDFK;
    private int EntrevistadorIDFK;
    private String Titulo;
    private String DataInicio;
    private String DataFim;
    private String Numero;
    private int Entrevistas;
    private int Ativo;


    @TypeConverters(DataConverterBairroEntrevista.class)
    private List<BairroEntrevista> BairroEntrevista = new ArrayList<>();

    @TypeConverters(DataConverterPergunta.class)
    private List<Pergunta> perguntas = new ArrayList<>();

    public Formulario () {
    }

    public int getFormularioID () {
        return FormularioID;
    }

    public void setFormularioID (int formularioID) {
        FormularioID = formularioID;
    }

    public int getClienteIDFK () {
        return ClienteIDFK;
    }

    public void setClienteIDFK (int clienteIDFK) {
        ClienteIDFK = clienteIDFK;
    }

    public int getEntrevistadorIDFK () {
        return EntrevistadorIDFK;
    }

    public void setEntrevistadorIDFK (int entrevistadorIDFK) {
        EntrevistadorIDFK = entrevistadorIDFK;
    }

    public String getTitulo () {
        return Titulo;
    }

    public void setTitulo (String titulo) {
        Titulo = titulo;
    }

    public String getDataInicio () {
        return DataInicio;
    }

    public void setDataInicio (String dataInicio) {
        DataInicio = dataInicio;
    }

    public String getDataFim () {
        return DataFim;
    }

    public void setDataFim (String dataFim) {
        DataFim = dataFim;
    }

    public String getNumero () {
        return Numero;
    }

    public void setNumero (String numero) {
        Numero = numero;
    }

    public int getEntrevistas () {
        return Entrevistas;
    }

    public void setEntrevistas (int entrevistas) {
        Entrevistas = entrevistas;
    }

    public int getAtivo () {
        return Ativo;
    }

    public void setAtivo (int ativo) {
        Ativo = ativo;
    }

    public List<BairroEntrevista> getBairroEntrevista () {
        return BairroEntrevista;
    }

    public void setBairroEntrevista (List<BairroEntrevista> bairroEntrevista) {
        this.BairroEntrevista = bairroEntrevista;
    }

    public List<Pergunta> getPerguntas () {
        return perguntas;
    }

    public void setPerguntas (List<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }
}

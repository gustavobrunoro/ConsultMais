package com.consultmais.consultmais.Model;

import java.io.Serializable;

public class Entrevistador implements Serializable {

    private int EntrevistadorID;
    private String Nome;
    private String Email;
    private String Senha;
    private String Token_API;
    private String Toke_Mobile;
    private int TermoResponsabilidade;
    private int Ativo;
    private boolean PermaneceConectado;

    public Entrevistador () {
    }

    public int getEntrevistadorID () {
        return EntrevistadorID;
    }

    public void setEntrevistadorID (int entrevistadorID) {
        EntrevistadorID = entrevistadorID;
    }

    public String getNome () {
        return Nome;
    }

    public void setNome (String nome) {
        Nome = nome;
    }

    public String getEmail () {
        return Email;
    }

    public void setEmail (String email) {
        Email = email;
    }

    public String getSenha () {
        return Senha;
    }

    public void setSenha (String senha) {
        Senha = senha;
    }

    public String getToken_API () {
        return Token_API;
    }

    public void setToken_API (String token_API) {
        Token_API = token_API;
    }

    public String getToke_Mobile () {
        return Toke_Mobile;
    }

    public void setToke_Mobile (String toke_Mobile) {
        Toke_Mobile = toke_Mobile;
    }

    public int getTermoResponsabilidade () {
        return TermoResponsabilidade;
    }

    public void setTermoResponsabilidade (int termoResponsabilidade) {
        TermoResponsabilidade = termoResponsabilidade;
    }

    public int getAtivo () {
        return Ativo;
    }

    public void setAtivo (int ativo) {
        Ativo = ativo;
    }

    public boolean isPermaneceConectado () {
        return PermaneceConectado;
    }

    public void setPermaneceConectado (boolean permaneceConectado) {
        PermaneceConectado = permaneceConectado;
    }
}

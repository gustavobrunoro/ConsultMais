package com.consultmais.consultmais.Database;

import android.content.Context;

import com.consultmais.consultmais.Model.Entrevistador;
import com.google.gson.Gson;

public class SharedPreferences {

    public Context context;

    public static final String ARQUIVO = "CONSULTMAIS";
    public static final String DADOSPESSOAIS = "DADOSPESSOAIS";

    private android.content.SharedPreferences sharedPreferences;
    private android.content.SharedPreferences.Editor editor ;

    /**Metodos responsavel retorna configuração SharedPreferences*/
    public SharedPreferences (Context context) {

        this.context = context;
        sharedPreferences = context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    /**Metodos responsavel atualizar dados pessoais no SharedPreferences*/
    public void atualizaDadosPessoais(Entrevistador entrevistador){

        Gson gson = new Gson();
        String json = gson.toJson(entrevistador);
        editor.putString(DADOSPESSOAIS, json);
        editor.commit();

    }

    /**Metodos responsavel por recupera os dados pessoais no SharedPreferences
     @return  Dados do Usuario*/
    public Entrevistador recupraDadosPessoais(){

        Entrevistador entrevistador = new Entrevistador();

        Gson gson = new Gson();
        String json = sharedPreferences.getString(DADOSPESSOAIS, "");
        entrevistador = gson.fromJson(json, Entrevistador.class);

        return entrevistador ;

    }

    public void clear (){

    }

}

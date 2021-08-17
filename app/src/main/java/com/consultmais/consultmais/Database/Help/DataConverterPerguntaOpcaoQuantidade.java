package com.consultmais.consultmais.Database.Help;

import androidx.room.TypeConverter;

import com.consultmais.consultmais.Model.PerguntaOpcaoQuantidade;
import com.consultmais.consultmais.Model.Resposta;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverterPerguntaOpcaoQuantidade {

    @TypeConverter
    public String fromList(List<PerguntaOpcaoQuantidade> perguntaOpcaoQuantidadeList) {
        if (perguntaOpcaoQuantidadeList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<PerguntaOpcaoQuantidade>>() {}.getType();
        String json = gson.toJson(perguntaOpcaoQuantidadeList, type);
        return json;
    }

    @TypeConverter
    public List<PerguntaOpcaoQuantidade> toList(String perguntaOpcaoQuantidadeList) {
        if (perguntaOpcaoQuantidadeList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<PerguntaOpcaoQuantidade>>() {}.getType();
        List<PerguntaOpcaoQuantidade> perguntaOpcaoQuantidades = gson.fromJson(perguntaOpcaoQuantidadeList, type);
        return perguntaOpcaoQuantidades;
    }

}

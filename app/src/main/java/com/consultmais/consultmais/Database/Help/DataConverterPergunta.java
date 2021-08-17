package com.consultmais.consultmais.Database.Help;

import androidx.room.TypeConverter;

import com.consultmais.consultmais.Model.Pergunta;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverterPergunta {

    @TypeConverter
    public String fromList(List<Pergunta>  perguntaList) {
        if (perguntaList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Pergunta>>() {}.getType();
        String json = gson.toJson(perguntaList, type);
        return json;
    }

    @TypeConverter
    public List<Pergunta> toList(String perguntaList) {
        if (perguntaList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Pergunta>>() {}.getType();
        List<Pergunta> perguntas = gson.fromJson(perguntaList, type);
        return perguntas;
    }
}

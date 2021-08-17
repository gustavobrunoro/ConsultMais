package com.consultmais.consultmais.Database.Help;

import androidx.room.TypeConverter;

import com.consultmais.consultmais.Model.Pergunta;
import com.consultmais.consultmais.Model.Resposta;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverterResposta {

    @TypeConverter
    public String fromList(List<Resposta>  respostaList) {
        if (respostaList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Resposta>>() {}.getType();
        String json = gson.toJson(respostaList, type);
        return json;
    }

    @TypeConverter
    public List<Resposta> toList(String respostaList) {
        if (respostaList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Resposta>>() {}.getType();
        List<Resposta> respostas = gson.fromJson(respostaList, type);
        return respostas;
    }
}

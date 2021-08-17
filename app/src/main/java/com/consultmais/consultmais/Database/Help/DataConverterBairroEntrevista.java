package com.consultmais.consultmais.Database.Help;

import androidx.room.TypeConverter;

import com.consultmais.consultmais.Model.BairroEntrevista;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverterBairroEntrevista {

    @TypeConverter
    public String fromList(List<BairroEntrevista> bairroEntrevistas) {
        if (bairroEntrevistas == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BairroEntrevista>>() {}.getType();
        String json = gson.toJson(bairroEntrevistas, type);
        return json;
    }

    @TypeConverter
    public List<BairroEntrevista> toList(String bairroEntrevistadosList) {
        if (bairroEntrevistadosList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BairroEntrevista>>() {}.getType();
        List<BairroEntrevista> bairroEntrevistados = gson.fromJson(bairroEntrevistadosList, type);
        return bairroEntrevistados;
    }
}


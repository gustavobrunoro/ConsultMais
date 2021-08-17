package com.consultmais.consultmais.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.consultmais.consultmais.Model.Pergunta;

import java.util.List;

@Dao
public interface PerguntaDAO {

    @Query("SELECT * FROM Pergunta")
    List<Pergunta> getPerguntalist ();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPergunta (Pergunta pergunta);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPerguntaAll(List<Pergunta> perguntas);

    @Update
    void updatePergunta (Pergunta pergunta);

    @Delete
    void deletePergunta (Pergunta pergunta);
}
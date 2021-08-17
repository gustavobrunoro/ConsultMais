package com.consultmais.consultmais.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.consultmais.consultmais.Model.Pergunta;
import com.consultmais.consultmais.Model.Perguntaopcao;

import java.util.List;

@Dao
public interface PerguntaOpcaoDAO {

    @Query("SELECT * FROM PerguntaOpcao WHERE PerguntaIDFK = :pergunta ORDER BY Ordem")
    List<Perguntaopcao> getPerguntaOpcaolist (int pergunta);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPerguntaOpcao (Perguntaopcao perguntaopcao);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPerguntaOpcaoAll(List<Perguntaopcao> perguntaopcao);

    @Update
    void updatePerguntaOpcao (Perguntaopcao perguntaopcao);

    @Delete
    void deletePerguntaOpcao(Perguntaopcao perguntaopcao);
}
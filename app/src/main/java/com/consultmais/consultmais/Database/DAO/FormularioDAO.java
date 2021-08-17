package com.consultmais.consultmais.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.consultmais.consultmais.Model.Formulario;

import java.util.List;

@Dao
public interface FormularioDAO {

    @Query("SELECT * FROM Formulario")
    List<Formulario> getFomumlariolist ();

    @Query("SELECT * FROM Formulario WHERE EntrevistadorIDFK = :EntrevistadorIDFK ORDER BY FormularioID DESC LIMIT 1 ")
    Formulario getFomumlario (int EntrevistadorIDFK);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFormulario (Formulario formulario);

    @Update
    void updateFormulario (Formulario formulario);

    @Delete
    void deleteFormulario (Formulario formulario);

}

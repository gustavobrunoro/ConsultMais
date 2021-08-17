package com.consultmais.consultmais.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.consultmais.consultmais.Model.Formulario;
import com.consultmais.consultmais.Model.FormularioResposta;
import com.consultmais.consultmais.Model.Resposta;

import java.util.List;

@Dao
public interface FormularioRespostaDAO {

    @Query("SELECT * FROM FormularioResposta WHERE EntrevistadorIDFK = :entrevistadorIDFK AND FormularioIDFK = :formularioID")
    List<FormularioResposta> getFomumlarioList ( int entrevistadorIDFK, int formularioID );

    @Query("SELECT * FROM FormularioResposta WHERE EntrevistadorIDFK = :entrevistadorIDFK AND Exportado = 0")
    List<FormularioResposta> getFormularioExport (int entrevistadorIDFK);

    @Query("SELECT IFNULL(COUNT(*),0) FROM FormularioResposta WHERE EntrevistadorIDFK = :entrevistadorIDFK AND FormularioIDFK = :formularioID")
    int getFormularioCount (int entrevistadorIDFK, int formularioID);

    @Query("SELECT IFNULL(COUNT(*),0) FROM FormularioResposta WHERE EntrevistadorIDFK = :entrevistadorIDFK AND FormularioIDFK = :formularioID and Exportado = 0")
    int getFormularioCountExport ( int entrevistadorIDFK, int formularioID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFormularioResposta (FormularioResposta formularioResposta);

    @Update
    void updateFormularioResposta (FormularioResposta formularioResposta);

    @Update
    void updateFormularioRespostaList (List<FormularioResposta> formularioRespostaList);

    @Delete
    void deleteFormularioResposta (FormularioResposta formularioResposta);
}

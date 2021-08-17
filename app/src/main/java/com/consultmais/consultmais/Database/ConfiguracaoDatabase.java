package com.consultmais.consultmais.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.consultmais.consultmais.Database.DAO.FormularioDAO;
import com.consultmais.consultmais.Database.DAO.FormularioRespostaDAO;
import com.consultmais.consultmais.Database.DAO.PerguntaOpcaoDAO;
import com.consultmais.consultmais.Database.DAO.PerguntaDAO;
import com.consultmais.consultmais.Database.Help.DataConverterBairroEntrevista;
import com.consultmais.consultmais.Database.Help.DataConverterPergunta;
import com.consultmais.consultmais.Database.Help.DataConverterPerguntaOpcaoQuantidade;
import com.consultmais.consultmais.Database.Help.DataConverterResposta;
import com.consultmais.consultmais.Database.Help.RoomTypeConverters;
import com.consultmais.consultmais.Model.Formulario;
import com.consultmais.consultmais.Model.FormularioResposta;
import com.consultmais.consultmais.Model.Pergunta;
import com.consultmais.consultmais.Model.Perguntaopcao;

@TypeConverters({RoomTypeConverters.class, DataConverterPergunta.class, DataConverterResposta.class, DataConverterPerguntaOpcaoQuantidade.class, DataConverterBairroEntrevista.class})
@Database(entities = {Formulario.class, Pergunta.class, Perguntaopcao.class, FormularioResposta.class}, version = 2)
public abstract class ConfiguracaoDatabase extends RoomDatabase {

    private static final  String DB_NAME = "ConsultMais";
    private static ConfiguracaoDatabase instance;

    public static synchronized ConfiguracaoDatabase getInstance(Context context ){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ConfiguracaoDatabase.class,DB_NAME)
                           .fallbackToDestructiveMigration()
                           .build();
        }
        return instance;
    }

    public abstract FormularioDAO formularioDAO();
    public abstract PerguntaDAO perguntaDAO ();
    public abstract PerguntaOpcaoDAO perguntaOpcaoDAO ();
    public abstract FormularioRespostaDAO formularioRespostaDAO();

}

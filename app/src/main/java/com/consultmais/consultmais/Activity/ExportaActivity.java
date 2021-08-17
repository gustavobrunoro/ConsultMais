package com.consultmais.consultmais.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.consultmais.consultmais.API.ConsultMais;
import com.consultmais.consultmais.API.RetrofitConfig;
import com.consultmais.consultmais.Database.ConfiguracaoDatabase;
import com.consultmais.consultmais.Database.Help.AppExecutors;
import com.consultmais.consultmais.Database.SharedPreferences;
import com.consultmais.consultmais.Model.BairroEntrevista;
import com.consultmais.consultmais.Model.Entrevistador;
import com.consultmais.consultmais.Model.FormularioResposta;
import com.consultmais.consultmais.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExportaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView titulo;
    private TextView percentaul;
    private CircularProgressBar progressBar ;
    private Button exporta;
    private List<FormularioResposta> formularioRespostaList = new ArrayList<>();
    private List<FormularioResposta> formularioRespostaControle = new ArrayList<>();
    private List<FormularioResposta> formularioRespostaExportados = new ArrayList<>();
    private Entrevistador entrevistador = new Entrevistador();
    private Retrofit retrofit;
    private ConsultMais consultMais;
    private SharedPreferences sharedPreferences ;
    private AlertDialog alertDialog;
    private int i = 0;
    private int total = 0;
    private ConfiguracaoDatabase configuracaoDatabase;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exporta);

        entrevistador  = (Entrevistador) getIntent().getSerializableExtra( "entrevistador" );

        inicializaComponentes ();

        toolbar.setTitle( "Exportação Formulário" );
        toolbar.setTitleTextColor( getResources().getColor( R.color.primaryTextColor ) );
        setSupportActionBar( toolbar );

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {
                formularioRespostaList = configuracaoDatabase.formularioRespostaDAO().getFormularioExport( entrevistador.getEntrevistadorID() );
                formularioRespostaControle = formularioRespostaList;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        titulo.setText( getString(R.string.aviso_Exportacao, String.valueOf( formularioRespostaList.size() ) ));

                        if (formularioRespostaList.size() > 0 ){
                            exporta.setEnabled(true);
                        }
                        else{
                            exporta.setEnabled(false);
                            progressBar.setProgress(0f);
                            percentaul.setText("");
                        }
                    }
                });
            }
        });
    }

    public void inicializaComponentes (){

        toolbar     = findViewById( R.id.toolbar );
        titulo      = findViewById( R.id.tv_Titulo );
        percentaul  = findViewById( R.id.tv_Percentual );
        progressBar = findViewById( R.id.pb_Percentual );
        exporta     = findViewById( R.id.bt_Exportar );

        retrofit             = RetrofitConfig.getRetrofit( entrevistador );
        consultMais          = retrofit.create( ConsultMais.class );
        configuracaoDatabase = ConfiguracaoDatabase.getInstance(this);
    }

    public void confirmaExportacao(View view ){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.ic_nav_exportacao_24dp);
        alertDialog.setTitle(R.string.aviso_Formulario_Exportacao);
        alertDialog.setMessage(R.string.aviso_Formulario_Exportacao_Mensagem);

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exportaFormulario();
            }
        });
        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.setCancelable(false);

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void exportaFormulario (){
        // verificar se o contador esta maior que o tamanho do Array, Caso esteja sinal q não tem mais obejtos a serem enviados
        if( i >= formularioRespostaList.size()  ){
            fimExportacao( formularioRespostaExportados );
            return;
        }
        else {

            consultMais.postResposta( formularioRespostaList.get( i ) ).enqueue(new Callback<FormularioResposta>() {
                @Override
                public void onResponse(Call<FormularioResposta> call, Response<FormularioResposta> response) {
                    if ( response.isSuccessful() ) {
                        total = total + 1;

                        float percentualTemp =  ( total * 100 ) / formularioRespostaControle.size() ;

                        // Atualiza o Contador do Alert Dialog
                        percentaul.setText( formatarFloat( percentualTemp ) );
                        progressBar.setProgress( percentualTemp );

                        formularioRespostaList.get( i ).setExportado(1);

                        formularioRespostaExportados.add(formularioRespostaList.get( i ));

                        i++;
                        exportaFormulario();
                    }
                }

                @Override
                public void onFailure(Call<FormularioResposta> call, Throwable t) {
                    Toast.makeText(ExportaActivity.this, getString(R.string.aviso_Exportacao_Erro_Conexao) + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void fimExportacao( final List<FormularioResposta> formularioRespostaExportados) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.ic_nav_exportacao_24dp);
        alertDialog.setTitle( R.string.aviso_Exportacao_Sucesso );
        alertDialog.setMessage( R.string.aviso_Exportacao_Sucesso_Mensagem );

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {
                configuracaoDatabase.formularioRespostaDAO().updateFormularioRespostaList( formularioRespostaExportados );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alert = alertDialog.create();
                        alert.show();
                    }
                });
            }
        });
    }

    public String formatarFloat(float numero){
        String retorno = "";
        DecimalFormat formatter = new DecimalFormat("#");
        try{
            retorno = formatter.format(numero) + "%";
        }catch(Exception ex){
            System.err.println("Erro ao formatar numero: " + ex);
        }
        return retorno;
    }

}
package com.consultmais.consultmais;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.consultmais.consultmais.API.ConsultMais;
import com.consultmais.consultmais.API.RetrofitConfig;
import com.consultmais.consultmais.Activity.ExportaActivity;
import com.consultmais.consultmais.Activity.LoginActivity;
import com.consultmais.consultmais.Activity.PreFormularioActivity;
import com.consultmais.consultmais.Adapter.AdapterPrincipal;
import com.consultmais.consultmais.Database.ConfiguracaoDatabase;
import com.consultmais.consultmais.Database.Help.AppExecutors;
import com.consultmais.consultmais.Database.SharedPreferences;
import com.consultmais.consultmais.Model.Entrevistador;
import com.consultmais.consultmais.Model.Formulario;
import com.consultmais.consultmais.Model.FormularioResposta;
import com.consultmais.consultmais.Model.Pergunta;
import com.consultmais.consultmais.Utils.Permissao;
import com.github.islamkhsh.CardSliderViewPager;
import com.github.juanlabrador.badgecounter.BadgeCounter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private String[] permissoes = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    private Toolbar toolbar;
    private ImageView logo;

    private CardView novo;
    private CardView exportar;
    private TextView tvFinalizada;
    private TextView tvPrevistas;
    private TextView tvExportar;
    private Entrevistador entrevistador = new Entrevistador();
    private Formulario formulario;
    private List<Pergunta> perguntas = new ArrayList<>();
    private List<FormularioResposta> formularioRespostas = new ArrayList<>();
    private android.app.AlertDialog alertDialog;
    private int totalFormulario;
    private int totalFormularioExportar;
    private int numeroFomulario;
    private int mNotificationCounter = 0;
    private String tokenNotificacao = "";
    private TextView versao;
    private TextView nomeEntrevistador;
    private CardSliderViewPager cardSliderViewPager;

    private Retrofit retrofit;
    private ConsultMais consultMais;
    private SharedPreferences sharedPreferences;
    private ConfiguracaoDatabase configuracaoDatabase;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        entrevistador = (Entrevistador) getIntent().getSerializableExtra( "entrevistador" );
        inicializaComponentes();

//        Log.i("Controle", entrevistador.getToken_API());

        atualizaTokenNotificacao(entrevistador);

        // Valida Permissão
        Permissao.ValidaPermissao(permissoes, this, 1);

        atualizaContadores();

        novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                carregaFormulario(entrevistador);
            }
        });
        exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                startActivity(new Intent(getApplicationContext(), ExportaActivity.class).putExtra("entrevistador", entrevistador));
            }
        });

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versao.setText("Versão " + pInfo.versionName);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        if (mNotificationCounter > 0) {
            BadgeCounter.update(this
                                      , menu.findItem(R.id.notification)
                                      , R.mipmap.ic_notification
                                      , BadgeCounter.BadgeColor.RED
                                      , mNotificationCounter);
        }
        else {
            BadgeCounter.hide(menu.findItem(R.id.notification));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.notification) {
            mNotificationCounter--;
            BadgeCounter.update(item, mNotificationCounter);
            if (mNotificationCounter == 0) {
                BadgeCounter.hide(item);
            }
        }
        else if (id == R.id.VerificarFormulario){
            checkNewForm(entrevistador, formulario);
        }
        else if (id == R.id.sair) {
            entrevistador.setPermaneceConectado(false);
            savedUsuario(entrevistador);
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Efetuar uma Varredura nas Permissões
        for (int permissaoResultado : grantResults) {
            // Verifica se a Permissão esta negada, para solicita permissão
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                // Solicita a permissão
                alertaPermissao();
            }
        }
    }

    @Override
    protected void onResume () {
        super.onResume();
        atualizaContadores();
    }

    @Override
    public void onBackPressed () {
    }

    public void inicializaComponentes () {

        toolbar             = findViewById(R.id.toolbar);
        nomeEntrevistador   = findViewById(R.id.tv_Entrevistador);
        logo                = findViewById(R.id.im_LogoID);
        cardSliderViewPager = (CardSliderViewPager) findViewById(R.id.viewPager);
        novo                = findViewById(R.id.cv_Entrevista_Nova);
        exportar            = findViewById(R.id.cv_Entrevista_Exportar);
        tvFinalizada        = findViewById(R.id.tv_Entrevistas_Finalizada);
        tvPrevistas         = findViewById(R.id.tv_Entrevistas_Previstas);
        tvExportar          = findViewById(R.id.tv_Entrevistas_Exportar);
        versao              = findViewById(R.id.tv_Versao);

        retrofit             = RetrofitConfig.getRetrofit(entrevistador);
        consultMais          = retrofit.create(ConsultMais.class);
        sharedPreferences    = new SharedPreferences(getApplicationContext());
        configuracaoDatabase = ConfiguracaoDatabase.getInstance(this);

    }

    /**Metodo responsavel por alerta o usuario sobre as permissões necessarias*/
    public void alertaPermissao () {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta");
        builder.setMessage("Aceite as Permissões");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialogInterface, int i) {finish();}
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**Metodo responsavel por efetuar o carregamento do formulario 
     * @param entrevistador Objeto contendo o ID do entrevistado caso seja necessario efetuar o download*/
    public void carregaFormulario (final Entrevistador entrevistador) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {
                
                formulario = new Formulario();
                formulario = configuracaoDatabase.formularioDAO().getFomumlario( entrevistador.getEntrevistadorID() );

                if (formulario != null) {
                    totalFormulario = configuracaoDatabase.formularioRespostaDAO().getFormularioCount( entrevistador.getEntrevistadorID(), formulario.getFormularioID());
                    if (periodoDisponivel(formulario)) {
                        if (totalFormulario < formulario.getEntrevistas()) {
                            abreNovoFormulario();
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run () {
                                    Toast.makeText(getApplicationContext(), "Pesquisa já Concluída", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run () {
                                showAlertPeriodo( formulario.getDataFim() );
                            }
                        });
                    }
                }
                else {
                    if (isOnline(getApplicationContext())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run () {
                                donwloadFormulario(entrevistador);
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run () {
                                Toast.makeText(getApplicationContext(), "Sem Formulario! Necessario conectar há Intenet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        });
    }

    /**Metodo responsavel por verificar se o aparelho se encontra com internet
     * @return boolean*/
    public boolean isOnline (Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**Metodo responsavel por efetuar o download do formulario quando não possui formulario cadastrado
     * @param entrevistador Objeto contendo o ID do entrevistado para efetuar o download do formulaio*/
    public void donwloadFormulario (final Entrevistador entrevistador) {
        alertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.aviso_Main_Download_Formulario)
                .setCancelable(false)
                .build();

        alertDialog.show();

        consultMais.getFormulario(entrevistador.getEntrevistadorID()).enqueue(new Callback<Formulario>() {
            @Override
            public void onResponse (Call<Formulario> call, Response<Formulario> response) {

                if (response.isSuccessful()) {

                    formulario = response.body();
                    alertDialog.dismiss();
                    AppExecutors.getInstance().diskIO().execute( new Runnable() {
                       @Override
                       public void run () {
                           configuracaoDatabase.formularioDAO().insertFormulario(formulario);
                           atualizaContadores ();
                       }
                    });

                }
                else {
                    alertDialog.dismiss();
                    Toast.makeText(MainActivity.this, R.string.aviso_Main_Sem_Formulaios, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure (Call<Formulario> call, Throwable t) {
                alertDialog.dismiss();
                Toast.makeText(MainActivity.this, getString(R.string.aviso_Main_Erro_Download_Formulario) + t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**Metodo responsavel por abrir a Activity com o novo formulario*/
    public void abreNovoFormulario () {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {

                numeroFomulario = configuracaoDatabase.formularioRespostaDAO().getFormularioCount( entrevistador.getEntrevistadorID(), formulario.getFormularioID()) + 1;
                startActivity(new Intent(getApplicationContext(), PreFormularioActivity.class).putExtra("Formulario", formulario)
                                                                                              .putExtra("Entrevistador", entrevistador)
                                                                                              .putExtra("NumeroFormulaio", numeroFomulario));
            }
        });
    }

    /**Metodo responsavel atualizar os contadores*/
    public void atualizaContadores () {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {

                formulario = new Formulario();
                formulario = configuracaoDatabase.formularioDAO().getFomumlario( entrevistador.getEntrevistadorID() );

                if (formulario != null) {

                    totalFormulario         = configuracaoDatabase.formularioRespostaDAO().getFormularioCount( entrevistador.getEntrevistadorID(), formulario.getFormularioID());
                    totalFormularioExportar = configuracaoDatabase.formularioRespostaDAO().getFormularioCountExport( entrevistador.getEntrevistadorID(), formulario.getFormularioID() );
                    formularioRespostas     = configuracaoDatabase.formularioRespostaDAO().getFomumlarioList( entrevistador.getEntrevistadorID(), formulario.getFormularioID());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run () {

                            toolbar.setTitle( getString(R.string.app_name) + getString(R.string.title_Formulario, formulario.getNumero() ) );
                            nomeEntrevistador.setText(getString(R.string.title_entrevistador, entrevistador.getNome() ) );
                            setSupportActionBar(toolbar);

                            tvPrevistas.setText( String.valueOf( formulario.getEntrevistas() - totalFormulario ) );
                            tvFinalizada.setText( String.valueOf( totalFormulario ) );
                            tvExportar.setText( String.valueOf(totalFormularioExportar) );

                            logo.setVisibility(View.INVISIBLE);
                            cardSliderViewPager.setVisibility(View.VISIBLE);
                            cardSliderViewPager.setAdapter(new AdapterPrincipal( formulario,formularioRespostas ));

                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run () {

                            toolbar.setTitle( getString(R.string.app_name) );
                            nomeEntrevistador.setText(getString(R.string.title_entrevistador, entrevistador.getNome() ));
                            setSupportActionBar(toolbar);

                            tvPrevistas.setText("0");
                            tvFinalizada.setText("0");
                            tvExportar.setText("0");

                            logo.setVisibility(View.VISIBLE);
                            cardSliderViewPager.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }
        });
    }

    /**Metodo responsavel por atualizar o token do usuario*/
    public void atualizaTokenNotificacao (final Entrevistador entrevistador) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess (InstanceIdResult instanceIdResult) {

                tokenNotificacao = instanceIdResult.getToken();

                if (! tokenNotificacao.equals(entrevistador.getToke_Mobile()) || entrevistador.getToke_Mobile().equals(null) ) {

                    entrevistador.setToke_Mobile(tokenNotificacao);

                    consultMais.atualizaTokenNotificacao( entrevistador.getEmail(), tokenNotificacao).enqueue(new Callback<Entrevistador>() {
                        @Override
                        public void onResponse (Call<Entrevistador> call, Response<Entrevistador> response) {
                            if(response.isSuccessful()){
                                savedUsuario(entrevistador);
                            }
                        }

                        @Override
                        public void onFailure (Call<Entrevistador> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    /**Metodo responsavel por salvar o usuario no SharedPreferences
     * @param entrevistador Objeto a ser salvo*/
    public void savedUsuario (Entrevistador entrevistador) {
        sharedPreferences.atualizaDadosPessoais(entrevistador);
    }

    /**Metodo responsavel por verificar se a data de hoje ainda se encontra no limite do formulario
     * @param formulario Objeto contendo data Inicial e Data Final
     * @return boolean */
    public boolean periodoDisponivel(Formulario formulario){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date hoje = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hoje);

        try {
               Date inicio = sdf.parse(formulario.getDataInicio());
               Date fim = sdf.parse(formulario.getDataFim());
                /* verifica se data do calendario esta entre datas inicial e final */
                if (calendar.getTime().after(inicio) && calendar.getTime().before(fim)) {
                    return true;
                }
                else {
                    return false;
                }

        } catch (ParseException ex) {
            return  false;
        }
    }

    /**Metodo responsavel por alertar ao usuario sobre a data limite do formulario
     * @param datafim Data fim para conclusão do formulario*/
    public void showAlertPeriodo( String datafim){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setIcon( R.drawable.ic_baseline_insert_invitation_24 );
        alertDialog.setTitle( R.string.aviso_Main_Periodo_Finalizado);

        try {
            alertDialog.setMessage( getString(R.string.aviso_Main_Periodo_Data_Limite, format.format( sdf.parse( datafim ) ) ) );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                
            }
        });

        android.app.AlertDialog alert = alertDialog.create();
        alert.show();

    }

    public void checkNewForm( Entrevistador entrevistador, Formulario formulario){

        final Formulario[] formularioNovo = {new Formulario()};
        String mensagem = "";

        alertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.aviso_Main_Novo_Formulario)
                .setCancelable(false)
                .build();

        alertDialog.show();

        consultMais.getFormulario(entrevistador.getEntrevistadorID()).enqueue(new Callback<Formulario>() {
            @Override
            public void onResponse (Call<Formulario> call, Response<Formulario> response) {

                if (response.isSuccessful()) {

                    formularioNovo[0] = response.body();
                    alertDialog.dismiss();

                    if (formularioNovo[0].getFormularioID() != formulario.getFormularioID()) {
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run () {
                                configuracaoDatabase.formularioDAO().insertFormulario(formulario);
                                atualizaContadores();
                            }
                        });
                        showNovoFormulario(getString(R.string.aviso_Main_Novo_Formulario_Sucesso));
                    }
                    else{
                        showNovoFormulario(getString(R.string.aviso_Main_Novo_Formulario_Não_Existe));
                    }

                }
                else {
                    alertDialog.dismiss();
                    Toast.makeText(MainActivity.this, R.string.aviso_Main_Novo_Formulario_Não_Existe, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure (Call<Formulario> call, Throwable t) {
                alertDialog.dismiss();
                Toast.makeText(MainActivity.this, getString(R.string.aviso_Main_Novo_Formulario_Erro_Conexao) + t.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showNovoFormulario(String mensagem ){

        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setMessage( mensagem );

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

}

package com.consultmais.consultmais.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.consultmais.consultmais.API.ConsultMais;
import com.consultmais.consultmais.API.RetrofitConfig;
import com.consultmais.consultmais.BuilderFoms.FormBuilder;
import com.consultmais.consultmais.BuilderFoms.model.TyperInput;
import com.consultmais.consultmais.MainActivity;
import com.consultmais.consultmais.Model.Resposta;
import com.consultmais.consultmais.Utils.GPSTracker;
import com.consultmais.consultmais.Database.ConfiguracaoDatabase;
import com.consultmais.consultmais.Database.Help.AppExecutors;
import com.consultmais.consultmais.Model.Entrevistador;
import com.consultmais.consultmais.Model.Formulario;
import com.consultmais.consultmais.Model.FormularioResposta;
import com.consultmais.consultmais.Model.Perguntaopcao;
import com.consultmais.consultmais.Model.Pergunta;
import com.consultmais.consultmais.R;
import com.consultmais.consultmais.Utils.CommonUtils;
import com.consultmais.consultmais.Utils.Permissao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FormularioActivity extends AppCompatActivity {

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private Toolbar toolbar;

    private LinearLayout layout;
    private FormBuilder builder;

    private static final String DATA_JSON_PATH = "formulario.json";
    private Formulario formulario = new Formulario() ;
    private Entrevistador entrevistador = new Entrevistador();
    private FormularioResposta formularioResposta = new FormularioResposta();
    private int numeroFomulario;
    private String titulo;
    private String titulonumero;
    private android.app.AlertDialog alertDialogProgresso;
    private int exportado = 0;
    private List<Resposta> respostaList = new ArrayList<>();
    private int Bairro;
    private String inicioFormulario;

    private ConfiguracaoDatabase configuracaoDatabase;
    private Retrofit retrofit;
    private ConsultMais consultMais;

    private GPSTracker gps;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_formulario);

        formulario       = (Formulario) getIntent().getSerializableExtra( "Formulario" );
        entrevistador    = (Entrevistador) getIntent().getSerializableExtra( "Entrevistador" );
        numeroFomulario  = (int) getIntent().getSerializableExtra( "NumeroFormulaio" );
        respostaList     = (List<Resposta>) getIntent().getSerializableExtra("ListaResposta");
        Bairro           = (int) getIntent().getSerializableExtra( "Bairro" );
        inicioFormulario = (String) getIntent().getStringExtra("InicioFormulario");
        inicializaComponentes ();

        titulonumero = formulario.getNumero() + "-" + leftZero( String.valueOf( entrevistador.getEntrevistadorID() ) ,2) + "/" + leftZero( String.valueOf( numeroFomulario ),3) ;
        titulo = getString(R.string.aviso_Pre_Formulario, titulonumero );

        toolbar.setTitle( titulo );
        toolbar.setTitleTextColor( getResources().getColor( R.color.primaryTextColor ) );
        setSupportActionBar( toolbar );

        // Vslida Permissão
        Permissao.ValidaPermissao(permissoes, this, 1);

        gps = new GPSTracker( getApplicationContext() );

        if (!gps.canGetLocation()){
            showAlertGPS();
        }

        criaFormulario();

        for (Pergunta pergunta : formulario.getPerguntas()) {
            if(pergunta.getParametrizada() == 0) {
                addElementos(pergunta);
                createElementSectionBreak();
            }
        }

        createButton();
    }

    @Override
    protected void onResume () {
        super.onResume();
        gps = new GPSTracker( getApplicationContext() );
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        gps = new GPSTracker( getApplicationContext() );
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate( R.menu.menu_novo_formulario, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected( item );
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
            else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
                gps = new GPSTracker( getApplicationContext() );
            }

        }

    }

    public void inicializaComponentes (){

        toolbar  = findViewById( R.id.toolbar );

        retrofit             = RetrofitConfig.getRetrofit( entrevistador );
        consultMais          = retrofit.create( ConsultMais.class );
        configuracaoDatabase = ConfiguracaoDatabase.getInstance(this);
    }

    public void alertaPermissao () {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.aviso_Formulaio_Alerta);
        builder.setMessage("Aceite as Permissões");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialogInterface, int i) {finish();}
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //region Criação de Elementos

    public void criaFormulario () {

        List<Pergunta> perguntas = new ArrayList<>();

        for (Pergunta pergunta : formulario.getPerguntas()) {
            if(pergunta.getParametrizada() == 0) {
                perguntas.add(pergunta);
            }
        }

        layout = (LinearLayout) findViewById(R.id.ly_FormularioID);
        builder = new FormBuilder(this, layout, perguntas );
    }

    public void carregaJson () {

//        List<Formulario> form = new ArrayList<>();
//        String json = CommonUtils.loadJSONFromAsset(getApplicationContext(), DATA_JSON_PATH);
//        form = new Gson().fromJson(json, new TypeToken<List<Formulario>>() {}.getType());
//        formulario = form.get(0);
    }

    public void addElementos (Pergunta pergunta) {

        switch (pergunta.getPerguntaTipoIDFK()) {

            case TyperInput.TYPER_INPUT_TEXT_VIEW:
                createElementTextView(pergunta);
                break;
            case TyperInput.TYPER_INPUT_EDIT_TEXT:
                createElementEditText(pergunta);
                break;
            case TyperInput.TYPER_INPUT_RADIO_GROUP:
                createElementRadioGroup(pergunta);
                break;
            case TyperInput.TYPER_INPUT_RATING:
                createElementRATING(pergunta);
                break;
            case TyperInput.TYPER_INPUT_CHECKBOX:
                createElementCheckBox(pergunta);
                break;
            case TyperInput.TYPER_INPUT_CHECKBOX_GROUP:
                createElementCheckBoxGroup(pergunta);
                break;
            case TyperInput.TYPER_INPUT_SWITCH:
                createElementSwitch(pergunta);
                break;
            case TyperInput.TYPER_INPUT_DROP_DOWN_LIST:
                createElementDropDownList(pergunta);
                break;
            case TyperInput.TYPER_INPUT_DATE:
                createElementDate(pergunta);
                break;
            case TyperInput.TYPER_INPUT_TIME:
                createElementTime(pergunta);
                break;
            case TyperInput.TYPER_INPUT_SECTIONBREAK:
                createElementSectionBreak();
                break;
        }

    }

    public void createElementTextView (Pergunta pergunta) {
        builder.createTextView(pergunta.getDescricao());
    }

    public void createElementEditText (Pergunta pergunta) {
        builder.createEditText(pergunta.getPerguntaID(),"Edit Text in HINT mode - single line", FormBuilder.EDIT_TEXT_MODE_HINT, true);
        builder.createEditText(pergunta.getPerguntaID(),"Edit Text in HINT mode - multiline", FormBuilder.EDIT_TEXT_MODE_HINT, false);
        builder.createEditText(pergunta.getPerguntaID(),"Edit Text in non HINT mode - single line", FormBuilder.EDIT_TEXT_MODE_SEPARATE, true);
        builder.createEditText(pergunta.getPerguntaID(),"Edit Text in non HINT mode - multiline", FormBuilder.EDIT_TEXT_MODE_SEPARATE, false);
    }

    public void createElementRadioGroup (Pergunta pergunta) {
        builder.createRadioGroup(pergunta.getPerguntaID(),pergunta.getDescricao(), pergunta.getPerguntaOpcao());
    }

    public void createElementRATING (Pergunta pergunta) {
        builder.createRatingsGroup(pergunta.getPerguntaID(),pergunta.getDescricao(), 1, 1, 5);
    }

    public void createElementCheckBox (Pergunta pergunta) {
        builder.createCheckbox(pergunta.getPerguntaID(),pergunta.getDescricao());
    }

    public void createElementCheckBoxGroup (Pergunta pergunta) {
        builder.createCheckboxGroup( pergunta.getPerguntaID(), pergunta.getDescricao(), options(pergunta.getPerguntaOpcao()));
    }

    public void createElementSwitch (Pergunta pergunta) {
        builder.createSwitch(pergunta.getPerguntaID(),pergunta.getDescricao(), options(pergunta.getPerguntaOpcao()));
    }

    public void createElementDropDownList (Pergunta pergunta) {
        builder.createDropDownList(pergunta.getPerguntaID(), pergunta.getDescricao(), options(pergunta.getPerguntaOpcao()));
    }

    public void createElementDate (Pergunta pergunta) {
        builder.createDatePicker(pergunta.getPerguntaID(),pergunta.getDescricao());
    }

    public void createElementTime (Pergunta pergunta) {
        builder.createTimePicker(pergunta.getPerguntaID(),pergunta.getDescricao());
    }

    public void createElementSectionBreak () {
        builder.createSectionBreak();
    }

    public void createButton() {

        Button button = new Button(this);

        int width = (int) getResources().getDimension(R.dimen.dp60);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width );
        button.setLayoutParams(params);
        button.setText(R.string.aviso_Formulaio_Finaliza);

        layout.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                saveForm();
            }
        });

    }

    private int getDimension(int id) {
        return (int) getResources().getDimension(id);
    }

    public List<String> options (List<Perguntaopcao> opcoesPerguntas) {

        List<String> options = new ArrayList<>();

        for (Perguntaopcao op : opcoesPerguntas) {
            options.add(op.getDescricao());
        }
        return options;
    }

    //endregion

    //region Cadastros

    public void saveForm() {

        SimpleDateFormat fomataData= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (builder.listaRespostas() != null) {

            List<Resposta> respostas = new ArrayList<>();

            respostas.addAll( respostaList );
            respostas.addAll( builder.listaRespostas() );

            formularioResposta.setBairroEntrevistaIDFK(Bairro);
            formularioResposta.setFormularioIDFK(formulario.getFormularioID());
            formularioResposta.setEntrevistadorIDFK(entrevistador.getEntrevistadorID());
            formularioResposta.setNumeroFormularioMobile(titulonumero);
            formularioResposta.setResposta( respostas );
            formularioResposta.setInicioEntrevista( inicioFormulario );
            formularioResposta.setFimEntrevista( fomataData.format( new Date() ) );

            if( gps.canGetLocation() ) {

                formularioResposta.setLatitude(gps.getLatitude());
                formularioResposta.setLongitude(gps.getLongitude());
                gps.stopUsingGPS();

                alertDialogProgresso = new SpotsDialog.Builder()
                        .setContext(this)
                        .setMessage(R.string.aviso_Formulario_Exportando)
                        .setCancelable(false)
                        .build();
                alertDialogProgresso.show();

                if (isOnline(getApplicationContext())) {
                    cadastroOnLine(formularioResposta);
                }
                else {
                    cadastroOffline(formularioResposta);
                    showAlertConclusao(false);
                }
            }
            else{
                showAlertGPS();
            }

        }
        else{

            final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
            alertDialog.setIcon(R.drawable.ic_baseline_error_24);
            alertDialog.setTitle( R.string.aviso_Formulario_Validacao );

            alertDialog.setMessage( getString(R.string.aviso_Pre_Campo_Obrigatorios) );
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            android.app.AlertDialog alert = alertDialog.create();
            alert.show();
        }
    }

    public void cadastroOnLine(final FormularioResposta formularioResposta){
        consultMais.postResposta(formularioResposta).enqueue(new Callback<FormularioResposta>() {
            @Override
            public void onResponse (Call<FormularioResposta> call, Response<FormularioResposta> response) {

                if(response.isSuccessful()){
                    alertDialogProgresso.dismiss();
                    exportado = 1;
                    cadastroOffline(formularioResposta);
                    showAlertConclusao(true);
                }
                else{
                    alertDialogProgresso.dismiss();
                    cadastroOffline(formularioResposta);
                    showAlertConclusao(false);
                }
            }

            @Override
            public void onFailure (Call<FormularioResposta> call, Throwable t) {
                alertDialogProgresso.dismiss();
                cadastroOffline(formularioResposta);
                showAlertConclusao(false);
            }
        });
    }

    public void cadastroOffline(final FormularioResposta formularioResposta){

        formularioResposta.setBairroEntrevistaIDFK(Bairro);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {
                formularioResposta.setExportado( exportado );
                configuracaoDatabase.formularioRespostaDAO().insertFormularioResposta( formularioResposta );
                alertDialogProgresso.dismiss();
            }
        });
    }

    //endregion

    //region Demais Controle
    public String leftZero( String texto, int tamanho ){

        String s = texto.trim();
        StringBuffer resp = new StringBuffer();

        int fim = tamanho - s.length();

        for (int x=0; x<fim; x++) {
            resp.append('0');
        }

        return  resp + s;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;
    }

    public void showAlertGPS(){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Configuração GPS");
            alertDialog.setMessage("GPS não está habilitado. Você quer ir para o menu de configurações?");
            alertDialog.setCancelable(false);

            alertDialog.setPositiveButton("Configurações", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });

            alertDialog.show();
    }

    public void showAlertConclusao(boolean controle){

        String Mensagem = controle == true ? getString(R.string.aviso_Formulario_Sucesso) : getString(R.string.aviso_Formulario_Salvo_Exportacao);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setMessage(Mensagem);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("entrevistador",entrevistador));
            }
        });

        alertDialog.show();

    }

    //endregion

}
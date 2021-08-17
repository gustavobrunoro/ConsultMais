package com.consultmais.consultmais.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.consultmais.consultmais.Database.ConfiguracaoDatabase;
import com.consultmais.consultmais.Database.Help.AppExecutors;
import com.consultmais.consultmais.Model.BairroEntrevista;
import com.consultmais.consultmais.Model.Entrevistador;
import com.consultmais.consultmais.Model.Formulario;
import com.consultmais.consultmais.Model.FormularioResposta;
import com.consultmais.consultmais.Model.Pergunta;
import com.consultmais.consultmais.Model.PerguntaOpcaoQuantidade;
import com.consultmais.consultmais.Model.Perguntaopcao;
import com.consultmais.consultmais.Model.Resposta;
import com.consultmais.consultmais.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PreFormularioActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private static LinearLayout parentLayout;
    private Formulario formulario = new Formulario() ;
    private Entrevistador entrevistador = new Entrevistador();
    private List<FormularioResposta> formularioRespostas = new ArrayList<>();
    private static List<Resposta> respostaList = new ArrayList<>();
    private static List<Pergunta> perguntaList = new ArrayList<>();
    private List<BairroEntrevista> bairroEntrevistaList = new ArrayList<>();
    private HashMap<Integer, Integer> respondidoBairro=  new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> respondidoSexo =  new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> respondidoFaixaEtariaMasculino =  new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> respondidoFaixaEtariaFeminino  =  new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> opcoesEscolhidas =  new HashMap<Integer, Integer>();

    private int numeroFomulario;
    private String titulo;
    private String titulonumero;
    private int Bairro;
    private Integer PerguntaParametrizada;
    private int PerguntaPai;
    private String tagBairro;
    private String tagSexo;
    private String tagFaixaEtaria;
    private String tagGrauEscolaridade;
    private String tagFaixaRenda;
    private String inicioFormulario;
    private SimpleDateFormat fomataData= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ConfiguracaoDatabase configuracaoDatabase;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_formulario);

        formulario       = (Formulario) getIntent().getSerializableExtra( "Formulario" );
        entrevistador    = (Entrevistador) getIntent().getSerializableExtra( "Entrevistador" );
        numeroFomulario  = (int) getIntent().getSerializableExtra( "NumeroFormulaio" );

        bairroEntrevistaList = formulario.getBairroEntrevista();
        perguntaList         = formulario.getPerguntas();

        inicializaComponentes ();
        inicioFormulario = fomataData.format(new Date());

        titulonumero = formulario.getNumero() + "-" + leftZero( String.valueOf( entrevistador.getEntrevistadorID() ) ,2) + "/" + leftZero( String.valueOf( numeroFomulario ),3) ;
        titulo = getString(R.string.aviso_Pre_Formulario, titulonumero);

        toolbar.setTitle( titulo );
        toolbar.setTitleTextColor( getResources().getColor( R.color.primaryTextColor ) );
        setSupportActionBar( toolbar );

        loadReposta();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate( R.menu.menu_pre_formulario, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if ( id == R.id.Next ) {
            save();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    public void inicializaComponentes (){

        toolbar      = findViewById( R.id.toolbar );
        parentLayout = findViewById(R.id.ly_preFormularioID);

        configuracaoDatabase = ConfiguracaoDatabase.getInstance(this);
    }

    public void createTextView( int tag, String text) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridViewAndroid  = new View(this);

        gridViewAndroid = inflater.inflate(R.layout.textview, null);
        TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.descricao);
        gridViewAndroid.setTag( "tv_" + String.valueOf( tag ) );
        textViewAndroid.setText(text);

        parentLayout.addView(gridViewAndroid);

    }

    public void createSectionBreak(int tag ) {

        Button button = new Button(this);
        button.setBackgroundColor(Color.GRAY);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDimension(R.dimen.dp1));
        params.setMargins(0, 0, 0, getDimension(R.dimen.dp10));
        button.setLayoutParams(params);
        button.setTag("sb_" + String.valueOf(tag));
        parentLayout.addView(button);

    }

    @SuppressLint("ResourceAsColor")
    public void createBairro (){

        createTextView( 0, getString(R.string.aviso_Pre_Informe_Bairro));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(params);
        radioGroup.setTag( "0" );
        tagBairro = "0";

        RadioButton radioButton;
        for (BairroEntrevista bairroEntrevista : bairroEntrevistaList) {
            radioButton = new RadioButton(this);
            radioButton.setText( bairroEntrevista.getDescricao() );
            radioButton.setId( bairroEntrevista.getOrdem() );

            if ( verificarDisponibilidadeBairro( bairroEntrevista.getOrdem() ) ){
                radioButton.setClickable(false);
                radioButton.setTextColor(R.color.EXPORTA_FORMULARIO);
            }

            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int posicao) {

                RadioGroup rg = parentLayout.findViewById( radioGroup.getId() );

                Bairro = posicao;

                // Remove  a Mensagem de erro
                for(int j = 0; j < radioGroup.getChildCount(); j ++) {
                    ((RadioButton) radioGroup.getChildAt(j)).setError(null);
                }

                removeView(tagSexo);
                removeView(tagFaixaEtaria);
                removeView(tagGrauEscolaridade);
                removeView(tagFaixaRenda);

                createSexo ();
            }
        });

        parentLayout.addView(radioGroup);

    }

    @SuppressLint("ResourceAsColor")
    public void createSexo (){

        Pergunta pergunta ;
        pergunta = formulario.getPerguntas().get(0);
        List<Perguntaopcao> options = pergunta.getPerguntaOpcao();

        createTextView( pergunta.getPerguntaID(), pergunta.getDescricao());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(params);
        radioGroup.setTag( String.valueOf( pergunta.getPerguntaID() ) );
        tagSexo =  String.valueOf( pergunta.getPerguntaID() );
        PerguntaPai = pergunta.getPerguntaID();

        RadioButton radioButton;
        for (Perguntaopcao opcoesPerguntas : options) {
            radioButton = new RadioButton(this);
            radioButton.setText(opcoesPerguntas.getDescricao());
            radioButton.setId(opcoesPerguntas.getOrdem());

            if ( verificarDisponibilidadeOpcao( opcoesPerguntas.getPerguntaIDFK(),opcoesPerguntas.getOrdem(), opcoesPerguntas.getPerguntaOpcaoQuantidade()) ){
                radioButton.setClickable(false);
                radioButton.setTextColor(R.color.EXPORTA_FORMULARIO);
            }

            radioGroup.addView(radioButton);
       }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int posicao) {

                RadioGroup rg = parentLayout.findViewById(radioGroup.getId());

                // Remove  a Mensagem de erro
                for(int j = 0; j < radioGroup.getChildCount(); j ++) {
                    ((RadioButton) radioGroup.getChildAt(j)).setError(null);
                }

                opcoesEscolhidas.put( Integer.parseInt((String) radioGroup.getTag()) , Integer.valueOf( posicao )  );

                inseriResposta( Integer.parseInt((String) radioGroup.getTag())  ,String.valueOf( posicao ),false,3,  1 , posicao );

                removeView(tagFaixaEtaria);
                removeView(tagGrauEscolaridade);
                removeView(tagFaixaRenda);

                createFaixaEtaria ();
            }
        });

        parentLayout.addView(radioGroup);

    }

    @SuppressLint("ResourceAsColor")
    public void createFaixaEtaria (){

        Pergunta pergunta ;
        pergunta = formulario.getPerguntas().get(1);
        List<Perguntaopcao> options = pergunta.getPerguntaOpcao();

        createSectionBreak(pergunta.getPerguntaID());

        createTextView( pergunta.getPerguntaID(), pergunta.getDescricao());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(params);
        radioGroup.setTag( String.valueOf( pergunta.getPerguntaID() ) );
        tagFaixaEtaria =  String.valueOf( pergunta.getPerguntaID() );

        RadioButton radioButton;
        for (Perguntaopcao opcoesPerguntas : options) {
            radioButton = new RadioButton(this);
            radioButton.setText(opcoesPerguntas.getDescricao());
            radioButton.setId(opcoesPerguntas.getOrdem());

            if ( verificarDisponibilidadeOpcao( opcoesPerguntas.getPerguntaIDFK(), opcoesPerguntas.getOrdem(), opcoesPerguntas.getPerguntaOpcaoQuantidade()) ){
                radioButton.setClickable(false);
                radioButton.setTextColor(R.color.EXPORTA_FORMULARIO);
            }

            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int posicao) {

                RadioGroup rg = parentLayout.findViewById(radioGroup.getId());

                // Remove  a Mensagem de erro
                for(int j = 0; j < radioGroup.getChildCount(); j ++) {
                    ((RadioButton) radioGroup.getChildAt(j)).setError(null);
                }

                inseriResposta( Integer.parseInt((String) radioGroup.getTag()) ,String.valueOf( posicao ),false,3,PerguntaPai, opcoesEscolhidas.get(PerguntaPai) );

                removeView(tagGrauEscolaridade);
                removeView(tagFaixaRenda);

                createGrauEsolaridade();
                createFaixaRenda();
            }
        });

        parentLayout.addView(radioGroup);

    }

    @SuppressLint("ResourceAsColor")
    public void createGrauEsolaridade (){

        Pergunta pergunta ;
        pergunta = formulario.getPerguntas().get(2);
        List<Perguntaopcao> options = pergunta.getPerguntaOpcao();

        createSectionBreak(pergunta.getPerguntaID());

        createTextView( pergunta.getPerguntaID(), pergunta.getDescricao());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(params);
        radioGroup.setTag( String.valueOf( pergunta.getPerguntaID() ) );
        tagGrauEscolaridade =  String.valueOf( pergunta.getPerguntaID() );

        RadioButton radioButton;
        for (Perguntaopcao opcoesPerguntas : options) {
            radioButton = new RadioButton(this);
            radioButton.setText(opcoesPerguntas.getDescricao());
            radioButton.setId(opcoesPerguntas.getOrdem());
            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int posicao) {

                RadioGroup rg = parentLayout.findViewById(radioGroup.getId());
                inseriResposta( Integer.parseInt((String) radioGroup.getTag()) ,String.valueOf( posicao ),false,3, null,null);
             }
        });

        parentLayout.addView(radioGroup);

    }

    @SuppressLint("ResourceAsColor")
    public void createFaixaRenda (){

        Pergunta pergunta ;
        pergunta = formulario.getPerguntas().get(3);
        List<Perguntaopcao> options = pergunta.getPerguntaOpcao();

        createSectionBreak(pergunta.getPerguntaID());

        createTextView( pergunta.getPerguntaID(), pergunta.getDescricao());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(params);
        radioGroup.setTag( String.valueOf( pergunta.getPerguntaID() ) );
        tagFaixaRenda = String.valueOf( pergunta.getPerguntaID() );

        RadioButton radioButton;
        for (Perguntaopcao opcoesPerguntas : options) {
            radioButton = new RadioButton(this);
            radioButton.setText(opcoesPerguntas.getDescricao());
            radioButton.setId(opcoesPerguntas.getOrdem());
            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int posicao) {

                RadioGroup rg = parentLayout.findViewById(radioGroup.getId());
                inseriResposta( Integer.parseInt((String) radioGroup.getTag()) ,String.valueOf( posicao ),false,3,null,null);
            }
        });

        parentLayout.addView(radioGroup);

    }

    private int getDimension(int id) {
        return (int) getResources().getDimension(id);
    }

    public void save() {

        if (!validaCampos()) {
            startActivity(new Intent(getApplicationContext(), FormularioActivity.class)
                    .putExtra("Formulario", formulario)
                    .putExtra("Entrevistador", entrevistador)
                    .putExtra("NumeroFormulaio", numeroFomulario)
                    .putExtra("ListaResposta", (Serializable) respostaList )
                    .putExtra("Bairro", Bairro)
                    .putExtra("InicioFormulario", inicioFormulario));
        }
        else{

            final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
            alertDialog.setIcon(R.drawable.ic_baseline_error_24);
            alertDialog.setTitle( R.string.aviso_Formulario_Validacao );

            alertDialog.setMessage( R.string.aviso_Pre_Campo_Obrigatorios );

            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            android.app.AlertDialog alert = alertDialog.create();
            alert.show();
        }
    }

    public String leftZero( String texto, int tamanho ){

        String s = texto.trim();
        StringBuffer resp = new StringBuffer();

        int fim = tamanho - s.length();

        for (int x=0; x<fim; x++) {
            resp.append('0');
        }

        return  resp + s;
    }

    public void loadReposta(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run () {

               formularioRespostas = configuracaoDatabase.formularioRespostaDAO().getFomumlarioList( entrevistador.getEntrevistadorID(), formulario.getFormularioID());

               if (formularioRespostas != null) {
                   for (FormularioResposta fr : formularioRespostas) {
                       for (BairroEntrevista be : bairroEntrevistaList) {
                           if (be.getOrdem() == fr.getBairroEntrevistaIDFK()) {
                               respondidoBairro.put(Integer.valueOf(be.getOrdem()), Integer.valueOf(respondidoBairro.get(Integer.valueOf(be.getOrdem())) == null ? 0 : respondidoBairro.get(Integer.valueOf(be.getOrdem()))) + 1);
                           }
                       }
                   }
               }
               else {
                   for (BairroEntrevista be : bairroEntrevistaList) {
                       respondidoBairro.put(be.getBairroEntrevistaID(), 0);
                   }
               }

               // Percorre todos os Formularios
               for (FormularioResposta fr : formularioRespostas) {
                    // Percorre todas as Resposta do Formulario
                   for (Resposta r : fr.getResposta()) {

                       for (Pergunta p : perguntaList){
                           if (p.getPerguntaID() == r.getPerguntaIDFK()){
                               if ( p.getParametrizada() == 1 || p.getParametrizada() == 2 )
                                   PerguntaParametrizada = p.getParametrizada();
                           }
                       }

                       if (PerguntaParametrizada != null ) {
                           // Verificar qual foi a Pergunta
                           switch (PerguntaParametrizada) {

                               // Se a Pergunta for Referente ao Sexo 1º Nivel
                               case 1:
                                   switch (r.getResposta()) {
                                       case "0":
                                           respondidoSexo.put(0, Integer.valueOf(respondidoSexo.get(0) == null ? 0 : respondidoSexo.get(0)) + 1);
                                           break;
                                       case "1":
                                           respondidoSexo.put(1, Integer.valueOf(respondidoSexo.get(1) == null ? 0 : respondidoSexo.get(1)) + 1);
                                           break;
                                   }
                                   break;
                               // Se a Pergunta for referente a Faixa de Renda 2ºNivel
                               case 2:
                                   if (r.getPerguntaOpcaoPaiIDFK() != null ) {
                                       // Verificar se o Opção Pai
                                       switch (r.getPerguntaOpcaoPaiIDFK()) {
                                           //Masculino
                                           case 0:
                                               switch (r.getResposta()) {
                                                   case "0":
                                                       respondidoFaixaEtariaMasculino.put(0, Integer.valueOf(respondidoFaixaEtariaMasculino.get(0) == null ? 0 : respondidoFaixaEtariaMasculino.get(0)) + 1);
                                                       break;
                                                   case "1":
                                                       respondidoFaixaEtariaMasculino.put(1, Integer.valueOf(respondidoFaixaEtariaMasculino.get(1) == null ? 0 : respondidoFaixaEtariaMasculino.get(1)) + 1);
                                                       break;
                                                   case "2":
                                                       respondidoFaixaEtariaMasculino.put(2, Integer.valueOf(respondidoFaixaEtariaMasculino.get(2) == null ? 0 : respondidoFaixaEtariaMasculino.get(2)) + 1);
                                                       break;
                                                   case "3":
                                                       respondidoFaixaEtariaMasculino.put(3, Integer.valueOf(respondidoFaixaEtariaMasculino.get(3) == null ? 0 : respondidoFaixaEtariaMasculino.get(3)) + 1);
                                                       break;
                                                   case "4":
                                                       respondidoFaixaEtariaMasculino.put(4, Integer.valueOf(respondidoFaixaEtariaMasculino.get(4) == null ? 0 : respondidoFaixaEtariaMasculino.get(4)) + 1);
                                                       break;
                                                   case "5":
                                                       respondidoFaixaEtariaMasculino.put(5, Integer.valueOf(respondidoFaixaEtariaMasculino.get(5) == null ? 0 : respondidoFaixaEtariaMasculino.get(5)) + 1);
                                                       break;
                                               }
                                               break;
                                           //Feminino
                                           case 1:
                                               switch (r.getResposta()) {
                                                   case "0":
                                                       respondidoFaixaEtariaFeminino.put(0, Integer.valueOf(respondidoFaixaEtariaFeminino.get(0) == null ? 0 : respondidoFaixaEtariaFeminino.get(0)) + 1);
                                                       break;
                                                   case "1":
                                                       respondidoFaixaEtariaFeminino.put(1, Integer.valueOf(respondidoFaixaEtariaFeminino.get(1) == null ? 0 : respondidoFaixaEtariaFeminino.get(1)) + 1);
                                                       break;
                                                   case "2":
                                                       respondidoFaixaEtariaFeminino.put(2, Integer.valueOf(respondidoFaixaEtariaFeminino.get(2) == null ? 0 : respondidoFaixaEtariaFeminino.get(2)) + 1);
                                                       break;
                                                   case "3":
                                                       respondidoFaixaEtariaFeminino.put(3, Integer.valueOf(respondidoFaixaEtariaFeminino.get(3) == null ? 0 : respondidoFaixaEtariaFeminino.get(3)) + 1);
                                                       break;
                                                   case "4":
                                                       respondidoFaixaEtariaFeminino.put(4, Integer.valueOf(respondidoFaixaEtariaFeminino.get(4) == null ? 0 : respondidoFaixaEtariaFeminino.get(4)) + 1);
                                                       break;
                                                   case "5":
                                                       respondidoFaixaEtariaFeminino.put(5, Integer.valueOf(respondidoFaixaEtariaFeminino.get(5) == null ? 0 : respondidoFaixaEtariaFeminino.get(5)) + 1);
                                                       break;
                                               }
                                               break;
                                       }
                                   }
                                   break;
                           }
                       }
                   }
               }

               runOnUiThread(new Runnable() {
                   @Override
                   public void run () {
                       createBairro();
                   }
               });

            }
        });
    }

    public boolean verificarDisponibilidadeBairro( int Ordem){

        boolean status = false;

        for (int i = 0; i < bairroEntrevistaList.size() ; i ++ ){
            if(respondidoBairro.size() > 0) {
                if (respondidoBairro.get(Ordem) != null) {
                    if (bairroEntrevistaList.get(i).getOrdem() == Ordem) {
                        if (bairroEntrevistaList.get(i).getQuantidade() == respondidoBairro.get( Ordem )) {
                            status = true;
                        }
                    }
                }
            }
        }

        return status;
    }

    public boolean verificarDisponibilidadeOpcao(int Pergunta, int Opcao, List<PerguntaOpcaoQuantidade> perguntaOpcaoQuantidades){

        boolean status = false;
        int Quantidade=0;
        Integer param = null;

        for (Pergunta p : perguntaList){
            if (p.getPerguntaID() == Pergunta){
                if( p.getParametrizada() == 1 || p.getParametrizada() == 2 ){
                    param = p.getParametrizada();
                }
            }
        }

        if (param !=null ) {
            for (PerguntaOpcaoQuantidade poq : perguntaOpcaoQuantidades) {

                // Verificar se a Pergunta e do Sexo
                if (param == 1) {
                    if (poq.getPerguntaPaiIDFK() == Pergunta && poq.getPerguntaOpcaoPaiIDFK() == Opcao) {
                        Quantidade = poq.getQuantidade();
                        if (respondidoSexo.size() > 0) {
                            if (respondidoSexo.get(Opcao) != null) {
                                if (respondidoSexo.get(Opcao) == Quantidade)
                                    status = true;
                            }
                        }
                    }
                }

                // Verificar se a Pergunta e da Faixa Etária
                if (param == 2) {
                    // Verificar qual foi a opção escolhida Acima ( Sexo ) 1º Nivel
                    switch (opcoesEscolhidas.get(PerguntaPai)) {

                        // Caso a Opção tenha sido Masculino
                        case 0:
                            if (poq.getPerguntaFilhaIDFK() == Pergunta && poq.getPerguntaOpcaoFilhaIDFK() == Opcao && poq.getPerguntaOpcaoPaiIDFK() == 0) {
                                Quantidade = poq.getQuantidade();

                                if (respondidoFaixaEtariaMasculino.size() > 0) {
                                    if (respondidoFaixaEtariaMasculino.get(Opcao) != null) {
                                        if (respondidoFaixaEtariaMasculino.get(Opcao) == Quantidade)
                                            status = true;
                                    }
                                }
                            }
                            break;
                        // Caso a Opção tenha do Feminino
                        case 1:
                            if (poq.getPerguntaFilhaIDFK() == Pergunta && poq.getPerguntaOpcaoFilhaIDFK() == Opcao && poq.getPerguntaOpcaoPaiIDFK() == 1) {
                                Quantidade = poq.getQuantidade();

                                if (respondidoFaixaEtariaFeminino.size() > 0) {
                                    if (respondidoFaixaEtariaFeminino.get(Opcao) != null) {
                                        if (respondidoFaixaEtariaFeminino.get(Opcao) == Quantidade)
                                            status = true;
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }

        return status;
    }

    public void inseriResposta (int pergunta, String resp, boolean isChecked, int tipoPerguntaIDFK, Integer PerguntaPaiIDFK, Integer PerguntaOpcaoPaiIDFK ){

        Resposta resposta;
        int flag = 0;

        List<Integer> TipoPerguntaIDFK = Arrays.asList( 6 );

        for (int i = 0; i < respostaList.size() ; i++){
            if ( respostaList.get(i).getPerguntaIDFK() == pergunta ){
                if ( ( respostaList.get(i).getResposta() != null ) && TipoPerguntaIDFK.contains( tipoPerguntaIDFK ) ){

                    List<String> opcoes = new ArrayList<String>(Arrays.asList(respostaList.get(i).getResposta().split(", ")));

                    if ( isChecked == true ){
                        opcoes.add(resp);
                    }
                    else{
                        opcoes.remove(resp);
                    }

                    resp = TextUtils.join(", ", opcoes).equals("") ? null : TextUtils.join(", ", opcoes);

                    if (resp == null) {
                        respostaList.remove(i);
                    }
                    else{
                        respostaList.get(i).setResposta(resp);
                    }

                }
                else{
                    respostaList.get(i).setPerguntaPaiIDFK(PerguntaPaiIDFK);
                    respostaList.get(i).setPerguntaOpcaoPaiIDFK(PerguntaOpcaoPaiIDFK);
                    respostaList.get(i).setResposta(resp);
                }
                flag = 1;
            }
        }

        if (flag == 0) {
            resposta = new Resposta();
            resposta.setPerguntaPaiIDFK(PerguntaPaiIDFK);
            resposta.setPerguntaOpcaoPaiIDFK(PerguntaOpcaoPaiIDFK);
            resposta.setPerguntaIDFK(pergunta);
            resposta.setResposta(resp);

            respostaList.add(resposta);
        }

    }

    public boolean validaCampos(){

        boolean retorno = false;
        Pergunta pergunta = new Pergunta();
        int id;
        RadioGroup radioGroup;

        radioGroup = parentLayout.findViewWithTag(String.valueOf("0"));
        if (radioGroup != null) {
            if (radioGroup.getCheckedRadioButtonId() == - 1) {
                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    ((RadioButton) radioGroup.getChildAt(j)).setError("");
                }
                retorno = true;
            }
            else {
                ((RadioButton) radioGroup.getChildAt(0)).setError(null);
            }
        }

        for (Pergunta p : perguntaList){
            if (p.isObrigatoria()) {
                radioGroup = parentLayout.findViewWithTag(String.valueOf(p.getPerguntaID()));
                if (radioGroup != null) {
                    if (radioGroup.getCheckedRadioButtonId() == - 1) {
                        for (int j = 0; j < radioGroup.getChildCount(); j++) {
                            ((RadioButton) radioGroup.getChildAt(j)).setError("");
                        }
                        retorno = true;
                    }
                    else {
                        ((RadioButton) radioGroup.getChildAt(0)).setError(null);
                    }
                }
            }
        }

        return retorno;
    }

    public void removeView(String tag){

        parentLayout.removeView(parentLayout.findViewWithTag("sb_" + tag));
        parentLayout.removeView(parentLayout.findViewWithTag("tv_" + tag));
        parentLayout.removeView(parentLayout.findViewWithTag(tag));

    }

}
package com.consultmais.consultmais.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;



import com.consultmais.consultmais.API.ConsultMais;
import com.consultmais.consultmais.API.RetrofitConfig;
import com.consultmais.consultmais.Database.SharedPreferences;
import com.consultmais.consultmais.MainActivity;
import com.consultmais.consultmais.Model.Entrevistador;
import com.consultmais.consultmais.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText email;
    private TextInputEditText senha;
    private Switch permarceConectaro;
    private Entrevistador entrevistador = new Entrevistador();
    private Retrofit retrofit;
    private ConsultMais consultMais;
    private SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                if ( key.equals( "Tipo" ) ) {
                    if (  getIntent().getExtras().getString( key ).equals( "Atualizacao" ) ) {
                        //controleAtualizacao = true;
                    }
                }
            }
        }

        inicializaComponentes ();

        toolbar.setTitle( getString(R.string.app_name));
        setSupportActionBar(toolbar);

        if ( verificarPermaceConectado() ) {
            entrevistador = sharedPreferences.recupraDadosPessoais();
            verificaTermoResponsabilidade( entrevistador );
        }

    }

    public void inicializaComponentes (){

        toolbar           = findViewById( R.id.toolbar);
        email             = findViewById( R.id.tv_Login_EmailID );
        senha             = findViewById( R.id.tv_Login_SenhaID );
        permarceConectaro = findViewById( R.id.sw_Login_PermaneceConectadoID );

        retrofit          = RetrofitConfig.getRetrofit( entrevistador );
        consultMais       = retrofit.create( ConsultMais.class );
        sharedPreferences = new SharedPreferences( getApplicationContext() );

    }

    /**Metodo responsavel por verifica se o usuario escolheu  opção de permanece conectado*/
    public boolean verificarPermaceConectado(){

        Entrevistador entrevistador = sharedPreferences.recupraDadosPessoais();

        if ( entrevistador != null ){
            if ( entrevistador.isPermaneceConectado() ){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    /**Metodo responsavel por entra na aplicação*/
    public void entra(View view){

        entrevistador.setEmail( email.getText().toString() );
        entrevistador.setSenha( senha.getText().toString() );

        if ( !entrevistador.getEmail().equals( "" ) ) {
            if ( !entrevistador.getSenha().equals( "" ) ) {
                final ProgressDialog dialog = ProgressDialog.show( LoginActivity.this, "","Verificando", true);
                consultMais.login( entrevistador.getEmail(),entrevistador.getSenha() ).enqueue( new Callback<Entrevistador>() {
                    @Override
                    public void onResponse(Call<Entrevistador> call , Response<Entrevistador> response) {
                        dialog.dismiss();

                        if ( response.isSuccessful() ){
                            if ( response.body() != null ){

                                entrevistador = response.body();
                                entrevistador.setPermaneceConectado( permarceConectaro.isChecked() );

                                // Verificar o status , ou seja o entrevistador ainda ta ativo
                                if (entrevistador.getAtivo() == 1 ){

                                    if  ( entrevistador.getTermoResponsabilidade() == 1 ){
                                        saveEntrevistador( entrevistador );
                                        abrirPrincipal( entrevistador );
                                    }
                                   else{
                                        dialog.dismiss();
                                        termoResponsabilidade( entrevistador );
                                    }

                                }
                                else{
                                    Toast.makeText( LoginActivity.this , "Por favor entre em contato com o Responsável Técnico" , Toast.LENGTH_LONG ).show();
                                }

                            }
                            else{
                                Toast.makeText( LoginActivity.this , "Usuário não encontrado !" , Toast.LENGTH_LONG ).show();
                            }
                        }
                        else{
                            Toast.makeText( LoginActivity.this , R.string.aviso_Login_Entrevistador_nao_Encontrado, Toast.LENGTH_LONG ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Entrevistador> call , Throwable t) {
                        dialog.dismiss();
                        Toast.makeText( LoginActivity.this , R.string.aviso_Login_Usuario_Senha_Incorreto, Toast.LENGTH_SHORT ).show();
                    }
                } );
            }
            else {
                Toast.makeText( this , getString(R.string.aviso_Login_Senha) , Toast.LENGTH_SHORT ).show();
            }
        }
        else{
            Toast.makeText( this , R.string.aviso_Login_Email, Toast.LENGTH_SHORT ).show();
        }

    }

    public void abrirPrincipal(Entrevistador entrevistador){
        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra( "entrevistador",entrevistador ));
    }

    public void saveEntrevistador (Entrevistador entrevistador){
        sharedPreferences.atualizaDadosPessoais( entrevistador );
    }

    public void verificaTermoResponsabilidade(final Entrevistador entrevistador){

       if ( entrevistador.getTermoResponsabilidade() == 1 ){
            saveEntrevistador( entrevistador );
            abrirPrincipal( entrevistador );
         }
       else{
            termoResponsabilidade( entrevistador );
         }
    }

    public void termoResponsabilidade(final Entrevistador entrevistador){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setIcon( R.drawable.ic_baseline_assignment_24 );
        builder1.setTitle("Termo de Responsabilidade ");
        builder1.setMessage("Declaro Comprometimento referente: \n" +
                "\n" +
                "a) Acessar o (s) sistema (s) informatizado (s) somente por necessidade de serviço ou por determinação expressa de superior hierárquico, realizando as tarefas e operações, em estrita observância aos procedimentos, normas e disposições contidas na instrução normativa que rege os acessos a sistemas; \n" +
                "\n" +
                "b) Não revelar fora do âmbito profissional fato ou informação de qualquer natureza de que tenha conhecimento por força de minhas atribuições, salvo em decorrência de decisão competente na esfera legal ou judicial, bem como de autoridade superior;\n" +
                "\n" +
                " c) Manter a necessária cautela quando da exibição de dados em tela, impressora ou na gravação em meios eletrônicos, a fim de evitar que deles venham a tomar ciência pessoas não autorizadas; \n" +
                "\n" +
                "d) Não revelar minha senha de acesso do sistema a ninguém e tomar o máximo de cuidado para que ela permaneça somente de meu conhecimento;\n" +
                "\n" +
                " e) Responder, em todas as instâncias, pelas consequências das ações ou omissões de minha parte que possam pôr em risco ou comprometer a exclusividade de conhecimento de minha senha ou das transações a que tenha acesso. \n" +
                "\n" +
                "Declaro, ainda, estar plenamente esclarecido e consciente que: \n" +
                "\n" +
                "1) É minha responsabilidade cuidar da integridade, confidencialidade e disponibilidade dos dados, informações contidas nos sistemas, devendo comunicar por escrito à chefia imediata quaisquer indícios ou possibilidades de irregularidades, de desvios ou falhas identificadas nos sistemas, sendo proibida a exploração de falhas ou vulnerabilidades porventura existentes;\n" +
                "\n" +
                " 2) O acesso à informação não me garante direito sobre ela, nem me confere autoridade para liberar acesso a outras pessoas; \n" +
                "\n" +
                "3) Constitui descumprimento de normas legais, regulamentares e quebra de sigilo funcional divulgar dados obtidos dos sistemas aos quais tenho acesso para outros servidores não envolvidos nos trabalhos executados; \n" +
                "\n" +
                "4) Devo alterar minha senha, sempre que obrigatório ou que tenha suposição de haver sido descoberta por terceiros, não usando combinações simples que possam ser facilmente descobertas; \n" +
                "\n" +
                "5) Cumprir e fazer cumprir os dispositivos da Política Corporativa de Segurança da Informação, de suas diretrizes, bem como deste Termo de Responsabilidade. \n" +
                "\n" +
                "Ressalvadas as hipóteses de requisições legalmente autorizadas, constitui infração funcional e penal a revelação de segredo do qual me apropriei em razão do cargo. Sendo crime contra a administração pública, a divulgação a quem não seja servidor, das informações do (s) sistema (s) ao (s) qual (is) tenho acesso, estando sujeito às penalidades previstas em lei; Sem prejuízo da responsabilidade penal e civil, e de outras infrações disciplinares, constitui falta de zelo e dedicação às atribuições do cargo e descumprimento de normas legais e regulamentares, não proceder com cuidado na guarda e utilização de senha ou emprestá-la a outro servidor, ainda que habilitado; Constitui infração funcional e penal inserir ou facilitar a inserção de dados falsos, alterar ou excluir indevidamente dados corretos dos sistemas ou bancos de dados da Administração Pública, com o fim de obter vantagem indevida para si ou para outrem ou para causar dano; bem como modificar ou alterar o sistema de informações ou programa de informática sem autorização ou sem solicitação de autoridade competente; ficando o infrator sujeito as punições previstas no Código Penal Brasileiro, conforme responsabilização por crime contra a Administração Pública, tipificado no art. 313-A e 313-B. Declaro, nesta data, ter ciência e estar de acordo com os procedimentos acima descritos, comprometendo-me a respeitá-los e cumpri-los plena e integralmente.\n");

        builder1.setCancelable(true);

        builder1.setPositiveButton( "Aceito", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                atualizatermoResponsabilidade(entrevistador);
                saveEntrevistador( entrevistador );
                abrirPrincipal( entrevistador );
            }
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void atualizatermoResponsabilidade(final Entrevistador entrevistador){

        retrofit = RetrofitConfig.getRetrofit( entrevistador );
        consultMais = retrofit.create( ConsultMais.class );

        consultMais.postTermoResponsabilidade( entrevistador.getEntrevistadorID() ).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call , Response<HashMap<String, String>> response) {

                if ( response.isSuccessful() ){
                    saveEntrevistador( entrevistador );
                }
                else{
                    entrevistador.setTermoResponsabilidade(0);
                    saveEntrevistador( entrevistador );
                    Toast.makeText( LoginActivity.this , response.toString() , Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call , Throwable t) {
                entrevistador.setTermoResponsabilidade(0);
                saveEntrevistador( entrevistador );
                Toast.makeText( LoginActivity.this , getString(R.string.aviso_Login_Termo_Erro) + "\n" + t.toString() , Toast.LENGTH_SHORT ).show();
            }
        } );

    }
    
}
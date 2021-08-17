package com.consultmais.consultmais.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.service.controls.Control;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.consultmais.consultmais.Database.ConfiguracaoDatabase;
import com.consultmais.consultmais.Database.Help.AppExecutors;
import com.consultmais.consultmais.Model.BairroEntrevista;
import com.consultmais.consultmais.Model.Formulario;
import com.consultmais.consultmais.Model.FormularioResposta;
import com.consultmais.consultmais.Model.Pergunta;
import com.consultmais.consultmais.Model.Resposta;
import com.consultmais.consultmais.R;
import com.github.islamkhsh.CardSliderAdapter;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterPrincipal extends CardSliderAdapter<AdapterPrincipal.MyviewHolder> {

    private Context context;
    private Formulario formulario;
    private List<Pergunta> perguntaList = new ArrayList<>();
    private List<FormularioResposta> formularioRespostas = new ArrayList<>();
    private List<BairroEntrevista> bairroEntrevistaList = new ArrayList<>();
    private HashMap<Integer, Integer> respondidoBairro=  new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> respondidoSexo =  new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> respondidoFaixaEtariaMasculino =  new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> respondidoFaixaEtariaFeminino  =  new HashMap<Integer, Integer>();
    private Pergunta pergunta = new Pergunta();
    private TableRow linha;
    private TextView cod;
    private TextView descricao;
    private TextView quantidade;
    private TextView opcao_0;
    private TextView opcao_1;
    private Integer PerguntaParametrizada;

    public AdapterPrincipal ( Formulario formulario,List<FormularioResposta> formularioRespostas ) {

        this.formulario = formulario;
        this.formularioRespostas = formularioRespostas;
        perguntaList.addAll( listaPerguntas( formulario ) );
        bairroEntrevistaList = formulario.getBairroEntrevista();
        loadReposta();
    }

    @Override
    public MyviewHolder onCreateViewHolder ( ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_paramentro_pesquisa, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void bindVH ( MyviewHolder holder, int position) {

       if (perguntaList.size() > 0) {

           if  ( position == 0 ) {

               holder.Opcao_0_Header.setVisibility(View.GONE);
               holder.Opcao_1_Header.setVisibility(View.GONE);
               holder.descricao.setText("Total por Bairro(s)");

               if (holder.tabela.getChildCount() > 1) {
                   holder.tabela.removeViews(1, holder.tabela.getChildCount() - 1);
               }

               for (BairroEntrevista be : bairroEntrevistaList) {
                   holder.tabela.addView(createRowBairro(be));
               }
           }
           else{

               pergunta = perguntaList.get(position -1 );

               holder.Opcao_0_Header.setVisibility(View.VISIBLE);
               holder.Opcao_1_Header.setVisibility(View.VISIBLE);
               holder.descricao.setText(pergunta.getDescricao());

               if (holder.tabela.getChildCount() > 1) {
                   holder.tabela.removeViews(1, holder.tabela.getChildCount() - 1);
               }

               for (int i = 0; i < pergunta.getPerguntaOpcao().size(); i++) {
                   holder.tabela.addView(createRow(pergunta).get(i));
               }
           }
       }
    }

    @Override
    public int getItemCount () {
        //return perguntaList.size();
        return 3;
    }

    public class MyviewHolder extends  RecyclerView.ViewHolder{

        TextView descricao;
        TableLayout tabela;
        TextView Opcao_0_Header;
        TextView Opcao_1_Header;

        public MyviewHolder(final View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.tv_descricaoPergunta);
            tabela = itemView.findViewById(R.id.tl_TabelaID);
            Opcao_0_Header = itemView.findViewById(R.id.tv_Opcao_0_Header);
            Opcao_1_Header = itemView.findViewById(R.id.tv_Opcao_1_Header);

        }
    }

    public List<Pergunta> listaPerguntas(Formulario formulario){

        List<Pergunta> list = new ArrayList<>();

        for (int i = 0 ; i < formulario.getPerguntas().size() ; i ++ ){
            if ( formulario.getPerguntas().get(i).getParametrizada() > 0  ){
                list.add( formulario.getPerguntas().get(i) );
            }
        }
        return list;
    }

    public TableRow createRowBairro(BairroEntrevista bairroEntrevista){

            linha = (TableRow)  LayoutInflater.from(context).inflate(R.layout.table, null);

            cod         = linha.findViewById(R.id.tv_CodID);
            descricao   = linha.findViewById(R.id.tv_DescricaoOpcaoID);
            quantidade  = linha.findViewById(R.id.tv_QuantidadeID);
            opcao_0     = linha.findViewById(R.id.tv_Opcao_0);
            opcao_1     = linha.findViewById(R.id.tv_Opcao_1);

            opcao_0.setVisibility(View.GONE);
            opcao_1.setVisibility(View.GONE);

            cod.setText( String.valueOf( bairroEntrevista.getOrdem() ));
            descricao.setText( bairroEntrevista.getDescricao());

            int qntdGeral =0;
            int qntdRespondido=0;
            Integer controle;

            qntdGeral = bairroEntrevista.getQuantidade();

            quantidade.setText( String.valueOf( qntdGeral ) );

            if  (respondidoBairro != null) {
                if (respondidoBairro.size() > 0) {
                    for (int b = 1; b <= respondidoBairro.size() + 1; b++) {
                        if ( b == bairroEntrevista.getOrdem()){
                            if (respondidoBairro.get(Integer.valueOf( b )) != null)
                            qntdRespondido = respondidoBairro.get(Integer.valueOf( b ) );
                        }
                    }
                }
            }

            quantidade.setText( String.valueOf( qntdGeral ) + "/" + qntdRespondido );

        return  linha;
    }

    public List<TableRow> createRow(Pergunta pergunta){

        List<TableRow> lista = new ArrayList<>();

        for ( int i = 0; i < pergunta.getPerguntaOpcao().size() ; i++ )
        {
            linha = (TableRow)  LayoutInflater.from(context).inflate(R.layout.table, null);

            cod         = linha.findViewById(R.id.tv_CodID);
            descricao   = linha.findViewById(R.id.tv_DescricaoOpcaoID);
            quantidade  = linha.findViewById(R.id.tv_QuantidadeID);
            opcao_0     = linha.findViewById(R.id.tv_Opcao_0);
            opcao_1     = linha.findViewById(R.id.tv_Opcao_1);

            opcao_0.setVisibility(View.VISIBLE);
            opcao_1.setVisibility(View.VISIBLE);

            cod.setText( String.valueOf( pergunta.getPerguntaOpcao().get(i).getOrdem() + 1 ));
            descricao.setText( pergunta.getPerguntaOpcao().get(i).getDescricao());

            int qntdGeral =0;
            int qntdRespondido=0;
            int qntdPerguntas=0;

            // Caso  a Pergunta Parametrizada seja 1
            if ( pergunta.getParametrizada() == 1 ) {

                qntdGeral = quantidadePergunta(pergunta,pergunta.getPerguntaOpcao().get(i).getOrdem() , null );

                quantidade.setText( String.valueOf( qntdGeral ) );

                if ( pergunta.getPerguntaOpcao().get(i).getOrdem() == 0 ) {
                    opcao_0.setText( String.valueOf( quantidadeRespondido(pergunta, pergunta.getPerguntaOpcao().get(i).getOrdem(), 0 ) ) );
                    opcao_1.setText("-");
                }
                else {
                    opcao_0.setText("-");
                    opcao_1.setText( String.valueOf(  quantidadeRespondido(pergunta, pergunta.getPerguntaOpcao().get(i).getOrdem(), 1 ) ) );
                }
            }

            if ( pergunta.getParametrizada() == 2 ) {

                qntdGeral = quantidadePergunta(pergunta,pergunta.getPerguntaOpcao().get(i).getOrdem() , null );

                quantidade.setText( String.valueOf( qntdGeral ) );

                qntdRespondido = quantidadeRespondido(pergunta, pergunta.getPerguntaOpcao().get(i).getOrdem(), 0 );
                qntdPerguntas  = quantidadePergunta(pergunta,pergunta.getPerguntaOpcao().get(i).getOrdem() , 0 );
                opcao_0.setText( qntdRespondido + "/" + qntdPerguntas );
                if ( qntdRespondido == qntdPerguntas) {
                    opcao_0.setBackgroundColor(R.color.qntdConcluida);
                }

                qntdRespondido = quantidadeRespondido(pergunta, pergunta.getPerguntaOpcao().get(i).getOrdem(), 1 );
                qntdPerguntas  = quantidadePergunta(pergunta,pergunta.getPerguntaOpcao().get(i).getOrdem() , 1 );
                opcao_1.setText( qntdRespondido + "/" + qntdPerguntas );
                if ( qntdRespondido == qntdPerguntas) {
                    opcao_1.setBackgroundColor(R.color.qntdConcluida);
                }
            }

            lista.add(linha);
        }

        return  lista;
    }

    public void loadReposta(){

        if (formularioRespostas != null) {
            for (FormularioResposta fr : formularioRespostas) {
                for (BairroEntrevista be : bairroEntrevistaList) {
                    if (be.getOrdem() == fr.getBairroEntrevistaIDFK()) {
                        respondidoBairro.put(Integer.valueOf(be.getOrdem()), Integer.valueOf( respondidoBairro.get(Integer.valueOf(be.getOrdem())) == null ? 0 : respondidoBairro.get(Integer.valueOf(be.getOrdem()))) + 1);
                    }
                }
            }
        }
        else {
            for (BairroEntrevista be : bairroEntrevistaList) {
                respondidoBairro.put(be.getOrdem(), 0);
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

                if (PerguntaParametrizada != null) {

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
                            if (r.getPerguntaOpcaoPaiIDFK() != null) {
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

    }

    public int quantidadePergunta(Pergunta pergunta , int Ordem, Integer Opcao){

        int Quantidade=0;

        if (pergunta.getParametrizada() == 1){
            Quantidade = pergunta.getPerguntaOpcao().get(Ordem).getPerguntaOpcaoQuantidade().get( 0 ).getQuantidade();
        }

        if (pergunta.getParametrizada() == 2){

            if ( Opcao == null ) {
                Quantidade = pergunta.getPerguntaOpcao().get(Ordem).getPerguntaOpcaoQuantidade().get( 0 ).getQuantidade();
                Quantidade = Quantidade + pergunta.getPerguntaOpcao().get(Ordem).getPerguntaOpcaoQuantidade().get( 1 ).getQuantidade();
            }
            else{
                Quantidade = pergunta.getPerguntaOpcao().get(Ordem).getPerguntaOpcaoQuantidade().get( Opcao ).getQuantidade();
            }
        }

        return Quantidade;
    }

    public int quantidadeRespondido(Pergunta pergunta , int Ordem, Integer Opcao ){

        int Quantidade=0;

        if (pergunta.getParametrizada() == 1){
            switch (Ordem){
                case 0:
                    if ( respondidoSexo.get(0) != null )
                    Quantidade = Integer.parseInt( String.valueOf( respondidoSexo.get(0) ) );
                    break;
                case 1:
                    if ( respondidoSexo.get(1) != null )
                    Quantidade = Integer.parseInt( String.valueOf( respondidoSexo.get(1) ) );
                    break;
            }
        }
        if (pergunta.getParametrizada() == 2){
            switch (Opcao){
                case 0:
                    if ( respondidoFaixaEtariaMasculino.size() > 0 )
                        if (respondidoFaixaEtariaMasculino.get(Ordem) != null )
                        Quantidade = Integer.parseInt( String.valueOf( respondidoFaixaEtariaMasculino.get(Ordem) ) );
                    break;
                case 1:
                    if ( respondidoFaixaEtariaFeminino.size() > 0 )
                        if (respondidoFaixaEtariaFeminino.get(Ordem) != null )
                        Quantidade = Integer.parseInt( String.valueOf( respondidoFaixaEtariaFeminino.get(Ordem) ) );
                    break;
            }
        }

        return Quantidade ;

    }

}



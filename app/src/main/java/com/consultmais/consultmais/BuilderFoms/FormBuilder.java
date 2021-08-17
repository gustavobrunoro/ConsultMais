package com.consultmais.consultmais.BuilderFoms;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.consultmais.consultmais.BuilderFoms.fragment.DatePickerFragment;
import com.consultmais.consultmais.BuilderFoms.fragment.TimePickerFragment;
import com.consultmais.consultmais.Model.Perguntaopcao;
import com.consultmais.consultmais.Model.Pergunta;
import com.consultmais.consultmais.Model.Resposta;
import com.consultmais.consultmais.R;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.consultmais.consultmais.BuilderFoms.model.TyperInput.TYPER_INPUT_CHECKBOX_GROUP;
import static com.consultmais.consultmais.BuilderFoms.model.TyperInput.TYPER_INPUT_DROP_DOWN_LIST;
import static com.consultmais.consultmais.BuilderFoms.model.TyperInput.TYPER_INPUT_RADIO_GROUP;

/**
 * FormBuilder is the main point of entry for this library.
 * <p>
 * It takes in the Context of the Application using the library and a LinearLayout object in its constructors.
 * It contains methods in it to dynamically construct views inside the LinearLayout provided.
 *
 * @author Girish Raman
 */
public class FormBuilder<Static> extends ContextWrapper  {

    private static LinearLayout parentLayout;
    public static final int EDIT_TEXT_MODE_HINT = 1;
    public static final int EDIT_TEXT_MODE_SEPARATE = 2;
    private List<View> views;
    private List<Pergunta> perguntas = new ArrayList<>();
    private static List<Resposta> respostaList = new ArrayList<>();
    private Context context;

    public FormBuilder(Context context, LinearLayout parentLayout, List<Pergunta> perguntas ) {
        super(context);

        this.respostaList.clear();
        this.perguntas.clear();

        this.context = context;
        this.parentLayout = parentLayout;
        this.views = new ArrayList<>();
        this.perguntas = perguntas;

    }

    //region Create Elements
    /**
     * Creates a TextView in the previously set Parent Layout displaying the given text in the default size of 16sp
     *
     * @param text the text to display
     */
    public void createTextView(String text) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridViewAndroid;

        gridViewAndroid = new View(context);
        gridViewAndroid = inflater.inflate(R.layout.textview, null);
        TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.descricao);
        textViewAndroid.setText(text);

        parentLayout.addView(gridViewAndroid);
    }

    public void createTextView(String text, int id) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);

        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTextSize(16);
        textView.setLayoutParams(params);
        textView.setText(text);
        parentLayout.addView(textView);
        views.add(textView);

    }

    /**
     * Creates an EditText in the previously set Parent Layout displaying the description as either a hint
     * or as a separate TextView above it.
     *
     * @param description a description that informs the user of the purpose of this EditText.
     * @param mode        EDIT_TEXT_MODE_HINT for display as a hint or EDIT_TEXT_MODE_SEPARATE for display as a separate TextView.
     * @param singleLine  true, if the EditText needs to be restricted to a single line. false, if the EditText needs to contain
     *                    paragraphs
     */
    public void createEditText(int id, String description, int mode, boolean singleLine) {

        EditText editText = new EditText(this);
        editText.setMinimumWidth(getDimension(R.dimen.dp200));
        LinearLayout.LayoutParams params = null;

        if (mode == EDIT_TEXT_MODE_HINT) {
            editText.setHint(description);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);
        }
        else if (mode == EDIT_TEXT_MODE_SEPARATE) {
            TextView textView = new TextView(this);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);
            textView.setLayoutParams(params);
            textView.setText(description);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            parentLayout.addView(textView);

            params = new LinearLayout.LayoutParams(getDimension(R.dimen.dp200), ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, getDimension(R.dimen.dp20));
        }

        if (singleLine) {
            editText.setMaxLines(1);
            editText.setSingleLine();
            editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            editText.setHorizontallyScrolling(true);
        }
        else {
            editText.setLines(5);
        }

        editText.setLayoutParams(params);
        parentLayout.addView(editText);
        views.add(editText);

    }

    /**
     * Creates a RadioGroup in the previously set Parent Layout displaying the options given by the List parameter.
     *
     * @param description a description of what the Radio Group stands for. This is displayed above the RadioGroup itself.
     * @param options     a List of Strings that represent the options that are to be part of the RadioGroup.
     */
    public void createRadioGroup(int id, String description, List<Perguntaopcao> options) {

        createTextView(description);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(params);
        radioGroup.setTag( String.valueOf( id ));

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

                // Remove  a Mensagem de erro
                for(int j = 0; j < radioGroup.getChildCount(); j ++) {
                    ((RadioButton) radioGroup.getChildAt(j)).setError(null);
                }

                inseriResposta( Integer.parseInt((String) radioGroup.getTag())  ,String.valueOf( posicao ),false,3);
            }
        });

        parentLayout.addView(radioGroup);
        views.add(radioGroup);
    }

    /**
     * Creates a RadioGroup for Ratings in the previously set Parent Layout.
     *
     * @param description     a description of the radio group
     * @param minRating       the minimum ratings number to display
     * @param inStepsOf       the difference between successive ratings
     * @param numberOfRatings the total number of ratings to provide
     */
    public void createRatingsGroup(int id, String description, int minRating, int inStepsOf, int numberOfRatings) {

        createTextView(description);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        radioGroup.setLayoutParams(params);
        radioGroup.setTag(  String.valueOf( id ) );

        for (int index = minRating; index <= inStepsOf * numberOfRatings; index += inStepsOf) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setLayoutParams(params);
            radioButton.setText(String.valueOf(index));
            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i) {
                inseriResposta( Integer.parseInt((String) radioGroup.getTag())  ,String.valueOf( i ),false,4);
            }
        });

        parentLayout.addView(radioGroup);
        views.add(radioGroup);
    }

    /**
     * Creates a Checkbox in the previously set Parent Layout.
     *
     * @param description a description of the Checkbox to display before the Checkbox.
     */
    public void createCheckbox(int id, String description) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setId(id);
        checkBox.setText(description);
        checkBox.setLayoutParams(params);

        inseriResposta( id ,"0",false,5);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

               int id =  ((CheckBox) view).getId();

                if ( ((CheckBox) view).isChecked() ) {
                    inseriResposta( id ,"1",false,5);
                }
                else{
                    inseriResposta( id ,"0",false,5);
                }
            }
        });

        parentLayout.addView(checkBox);
        views.add(checkBox);
    }

    /**
     * Creates a Checkbox Group in the previously set Parent Layout.
     *
     * @param description a description of the Checkbox Group to display before it.
     * @param options     a List of String that contains the options to display as part of the Checkbox Group.
     */
    public void createCheckboxGroup( int id, String description, List<String> options) {

        createTextView( description, id ) ;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final int CHECKBOX_ID = id;

        for (int i = 0; i < options.size(); ++i) {

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(options.get(i));
            checkBox.setTag(CHECKBOX_ID + "_"+ i);
            checkBox.setLayoutParams(params);

            checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    String tag = String.valueOf( buttonView.getTag() );
                    String pergunta = tag.substring(0,( tag.indexOf("_")) );

                    removeSetErroCheckBoxGroup(pergunta,options);

                    inseriResposta( CHECKBOX_ID ,encontraOrdem( String.valueOf(buttonView.getTag()) ), isChecked , 6);
                }
            });

            if (i == options.size() - 1) {
                params.setMargins(0, 0, 0, 0);
            }
            views.add(checkBox);
            parentLayout.addView(checkBox);
        }
    }

    public void createSwitch(int id, String description, List<String> options) {

        createTextView( description, id );

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(this);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);
        ll.setLayoutParams(params);

        //firstChoice
        textView = new TextView(this);
        textView.setLayoutParams(params);
        textView.setText(options.get(0));
        ll.addView(textView);

        Switch switchCompat = new Switch(this);
        switchCompat.setId(id);
        params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);
        switchCompat.setLayoutParams(params);
        ll.addView(switchCompat);

        //secondChoice
        textView = new TextView(this);
        textView.setLayoutParams(params);
        textView.setText(options.get(1));
        ll.addView(textView);
        parentLayout.addView(ll);

        inseriResposta(id,"0",false,7);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {

                if (b){
                    inseriResposta(compoundButton.getId(),"1",false,7);
                }
                else{
                    inseriResposta(compoundButton.getId(),"0",false,7);
                }
            }
        });
    }

    /**
     * Creates a Dropdown in the previously set Parent Layout.
     *
     * @param description a description for the dropdown list to display before it
     * @param options     a List of String that represents the options in the drop down list.
     */
    public void createDropDownList(int id, String description, List<String> options) {

        createTextView(description);

        int width = (int) getResources().getDimension(R.dimen.dp60);

        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width);
        params.setMargins(0, 0, 0, 0);

        Spinner spinner = new Spinner(this,Spinner.MODE_DIALOG);
        spinner.setId(id);
        spinner.setLayoutParams(params);

        //spinner.setBackgroundColor(Color.parseColor("#e1f5fe"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.buildformer_spinner_item){
            @Override
            public boolean isEnabled(int position){

                if(position == 0){
                    // Disabilita a primeira posição (hint)
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == 0){
                    // Deixa o hint com a cor cinza ( efeito de desabilitado)
                    tv.setTextColor(Color.GRAY);
                }else {
                    tv.setTextColor(Color.BLACK);
                }

                return view;
            }
        };
        adapter.add("Selecione opção");

        for (String option : options) {
            adapter.add(option);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    for (int i = 0 ; i < perguntas.size() ; i++ ){
                        if ( perguntas.get( i ).getPerguntaID() == spinner.getId() ){
                            inseriResposta( perguntas.get( i ).getPerguntaID() , String.valueOf( position - 1 ), false , 8 );
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        spinner.setAdapter(adapter);

        parentLayout.addView(spinner);
        views.add(spinner);

    }

    /**
     * Creates a DatePicker in the previously set Parent Layout.
     */
    public void createDatePicker(int id, String description) {

        createTextView( description, id );

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);

        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setTag("date");
        textView.setLayoutParams(params);

        final Activity activity = (Activity) getBaseContext();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setParentLayout(parentLayout);
                datePickerFragment.show(activity.getFragmentManager(), "datePicker");
            }
        });

        parentLayout.addView(textView);
    }

    /**
     * Creates a TimePicker in the previously set Parent Layout.
     */
    public void createTimePicker(int id, String description) {

        createTextView( description, id );

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getDimension(R.dimen.dp10), 0, getDimension(R.dimen.dp10), 0);

        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setTag("time");
        textView.setLayoutParams(params);

        final Activity activity = (Activity) getBaseContext();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setParentLayout(parentLayout);
                timePickerFragment.show(activity.getFragmentManager(), "timePicker");
            }
        });

        parentLayout.addView(textView);

    }

    /**
     * Adds a section break to the screen.
     */
    public void createSectionBreak() {

        Button button = new Button(this);
        button.setBackgroundColor(Color.GRAY);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDimension(R.dimen.dp1));
        params.setMargins(0, 0, 0, getDimension(R.dimen.dp10));
        button.setLayoutParams(params);
        parentLayout.addView(button);

    }

    /**
     * Adds a section break to the screen.
     */
    public void createButton() {

        Button button = new Button(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getDimension(R.dimen.dp10), getDimension(R.dimen.dp10), getDimension(R.dimen.dp10), getDimension(R.dimen.dp10));
        button.setText("FINALIZAR ENTREVISTA");
        button.setLayoutParams(params);
        parentLayout.addView(button);

    }
    //endregion

    //region Demais Metodos
    private int getDimension(int id) {
        return (int) getResources().getDimension(id);
    }

    public static void inseriResposta (int pergunta, String resp, boolean isChecked, int tipoPerguntaIDFK ){

        Resposta resposta;
        int flag = 0;

        List<Integer> TipoPerguntaIDFK = Arrays.asList( 6 );

        for (int i=0 ; i < respostaList.size() ; i++){
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
                    respostaList.get(i).setResposta(resp);
                }
                flag = 1;
            }
        }

        if (flag == 0) {
            resposta = new Resposta();
            resposta.setPerguntaIDFK(pergunta);
            resposta.setResposta(resp);
            respostaList.add(resposta);
        }
    }

    public boolean validaCampos(){

        boolean retorno = false;
        Pergunta pergunta = new Pergunta();
        int id;

        for (int i=0 ; i < perguntas.size() ; i++){

            id = views.get(i).getId();
            pergunta = perguntas.get(i);

            if (pergunta.isObrigatoria()){
                switch (pergunta.getPerguntaTipoIDFK()){

                    case TYPER_INPUT_CHECKBOX_GROUP:
                        if( respostaList.size() > 0 ){
                            boolean existe = false;

                            for (int r =0 ; r < respostaList.size() ; r++){
                                if (respostaList.get(r).getPerguntaIDFK() == pergunta.getPerguntaID()){
                                    existe = true;

                                    List<String> opcoes = new ArrayList<String>(Arrays.asList(respostaList.get(r).getResposta().split(", ")));

                                    if ( opcoes != null || opcoes.size() > 0 ) {
                                        if (opcoes.size() < pergunta.getMinimoRespostas()) {
                                            marcaErroCheckBoxGroup(String.valueOf(pergunta.getPerguntaID()), pergunta.getPerguntaOpcao());
                                        } else if (opcoes.size() > pergunta.getMaximoRespostas()) {
                                            marcaErroCheckBoxGroup(String.valueOf(pergunta.getPerguntaID()), pergunta.getPerguntaOpcao());
                                        }
                                    }
                                    else{
                                        marcaErroCheckBoxGroup(String.valueOf(pergunta.getPerguntaID()), pergunta.getPerguntaOpcao());
                                    }
                                }
                            }

                            if (!existe){
                                marcaErroCheckBoxGroup(String.valueOf(pergunta.getPerguntaID()), pergunta.getPerguntaOpcao());
                            }
                        }
                        else{
                            marcaErroCheckBoxGroup(String.valueOf(pergunta.getPerguntaID()), pergunta.getPerguntaOpcao());
                        }
                        break;

                    case TYPER_INPUT_RADIO_GROUP:
                        RadioGroup radioGroup = parentLayout.findViewWithTag( String.valueOf( pergunta.getPerguntaID() ) );
                        if ( radioGroup !=null ) {
                            if (radioGroup.getCheckedRadioButtonId() == - 1) {
                                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                                    ((RadioButton) radioGroup.getChildAt(j)).setError("");
                                }
                                retorno = true;
                            }
                            else{
                                ((RadioButton) radioGroup.getChildAt(0)).setError(null);
                            }
                        }
                        break;

                    case TYPER_INPUT_DROP_DOWN_LIST:
                        Spinner spinner = parentLayout.findViewById(pergunta.getPerguntaID());
                        if (spinner != null){
                            if ( spinner.getSelectedItem() == "Selecione opção") {
                                ((TextView) spinner.getSelectedView()).setError("");
                                retorno = true;
                            }
                        }
                        break;
                }
            }
        }

        return retorno;
    }

    public List<Resposta> listaRespostas (){

        List<Resposta> list = new ArrayList<>();

        if (validaCampos()){
            return list = null;
        }
        else{
            return respostaList;
        }
    }

    public String encontraOrdem(String opcao){

        String ordem = "";
        ordem = opcao.substring( opcao.indexOf("_") , opcao.length() ).replace("_","");
        return  ordem;

    }

    public void removeSetErroCheckBoxGroup(String pergunta, List<String> options){

        for ( int i =0 ; i < views.size() ; i++ ){

            String tag_pergunta =  String.valueOf( views.get(i).getTag() );

            if (tag_pergunta.indexOf(pergunta + "_") != -1) {
                TextView textView = parentLayout.findViewWithTag(tag_pergunta);
                textView.setError(null);
            }
        }
    }

    public void marcaErroCheckBoxGroup( String pergunta, List<Perguntaopcao> options ){

        for ( int i =0 ; i < views.size() ; i++ ){

            String tag_pergunta =  String.valueOf( views.get(i).getTag() );

            if (tag_pergunta.indexOf(pergunta + "_") != -1) {
                TextView textView = parentLayout.findViewWithTag(tag_pergunta);
                textView.setError("");
            }
        }

    }
    //endregion

}

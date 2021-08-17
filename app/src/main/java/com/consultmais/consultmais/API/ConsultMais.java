package com.consultmais.consultmais.API;

import com.consultmais.consultmais.Model.Entrevistador;
import com.consultmais.consultmais.Model.Formulario;
import com.consultmais.consultmais.Model.FormularioResposta;
import com.consultmais.consultmais.Model.Resposta;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ConsultMais {

    @POST("login")
    Call<Entrevistador> login (@Query("Email") String Email,
                               @Query("Senha") String Senha);

    @POST("aceitartermo/{entrevistadorID}")
    Call<HashMap<String,String>> postTermoResponsabilidade(@Path("entrevistadorID") int entrevistadorID );

    @GET("formulario/{EntrevistadorID}")
    Call<Formulario> getFormulario (@Path("EntrevistadorID") int EntrevistadorID);

    @POST("resposta")
    Call<FormularioResposta> postResposta(@Body FormularioResposta formularioResposta) ;

    @POST("tokennotificacao")
    Call<Entrevistador> atualizaTokenNotificacao(@Query("Email") String Email,
                                                 @Query( "Token_Mobile" ) String Token_Mobile) ;

}

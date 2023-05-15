package mx.itson.world.interfaces

import mx.itson.world.entidades.Visita
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CheemsAPI {
    @GET("visitas")
    fun getVisitas(): Call<List<Visita>>

    @FormUrlEncoded
    @POST("visitas/agregar")
    fun createVisita(@Field("lugar") lugar: String,
                     @Field("motivo") motivo: String,
                     @Field("responsable") responsable: String,
                     @Field("latitud") latitud: String,
                     @Field("longitud") longitud: String) : Call<Void>
}
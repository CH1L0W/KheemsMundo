package mx.itson.world

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import mx.itson.world.entidades.Visita
import mx.itson.world.utilerias.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener{
    var mapa: GoogleMap? = null
    var visitas: List<Visita>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapaFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapaFragment.getMapAsync(this)
        obtenerVisitas()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnAgregar -> {
                val intentListado = Intent(this, ViajeFormActivity::class.java)
                startActivity(intentListado)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            mapa = googleMap
            mapa!!.mapType = GoogleMap.MAP_TYPE_HYBRID
            mapa!!.setOnMarkerClickListener(this)
        }catch (ex: java.lang.Exception){

        }
    }

    fun obtenerVisitas(){
        val call: Call<List<Visita>> = RetrofitUtil.getApi().getVisitas()
        call.enqueue(object: Callback<List<Visita>>{
            override fun onResponse(call: Call<List<Visita>>, response: Response<List<Visita>>) {
                visitas = response.body()!!
                mapa?.clear()

                for(v in visitas!!){
                    val latLng = LatLng(v.latitud!!,v.longitud!!)
                    mapa?.addMarker(MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cheems))
                        .title("Informacion del Viaje")
                        .snippet("Lugar: "+v.lugar+"\n Motivo: "+v.motivo+"\n Responsable: "+v.responsable))

                }


            }

            override fun onFailure(call: Call<List<Visita>>, t: Throwable) {
            }

        })
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        mapa?.setInfoWindowAdapter(infoActivity(this@MainActivity,visitas,marker.id))
        return false
    }
}
package mx.itson.world

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import mx.itson.world.entidades.Visita
import mx.itson.world.utilerias.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViajeFormActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener, OnClickListener {
    var mapaForm : GoogleMap? = null
    lateinit var lugar: EditText
    lateinit var motivo: EditText
    lateinit var responsable: EditText
    lateinit var latitudForm: String
    lateinit var longitudForm: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viaje_form)
        val mapaFragment = supportFragmentManager.findFragmentById(R.id.mapaForm) as SupportMapFragment
        mapaFragment.getMapAsync(this)
        lugar = findViewById(R.id.txtLugar)
        motivo = findViewById(R.id.txtMotivo)
        responsable = findViewById(R.id.txtResponsable)
        val btnAceptar = findViewById<Button>(R.id.btnContinuar)
        btnAceptar.setOnClickListener(this)

    }

    fun guardar(lugar: String, motivo: String, responsable: String, latitud:String, longitud: String){
        val llamada: Call<Void> = RetrofitUtil.getApi().createVisita(lugar, motivo, responsable, latitud, longitud)
        llamada.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val a = 1;
                Toast.makeText(this@ViajeFormActivity, "guard0 :c", Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("","Error",t)
            }
        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        try{
            mapaForm = googleMap
            mapaForm!!.mapType = GoogleMap.MAP_TYPE_HYBRID

            val estaOtorgado = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if(estaOtorgado) {
                googleMap.isMyLocationEnabled = true
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(
                locationManager.getBestProvider(Criteria(), true)!!)

            // if(location != null){onLocationChanged(location)}
            location?.let{onLocationChanged(it)}

        }catch(ex: Exception){
            Log.e("Ocurrio un error al cargar el mapa", ex.toString())
        }
    }

    override fun onLocationChanged(location: Location) {
        val latitud : Double = location.latitude
        val longitud : Double = location.longitude

        val latLng = LatLng(latitud, longitud)
        mapaForm?.clear()
        mapaForm?.addMarker(MarkerOptions().position(latLng).draggable(true))

        mapaForm?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mapaForm?.animateCamera(CameraUpdateFactory.zoomTo(5f))

        mapaForm?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {
                val latLng = marker.position
                latitudForm = latLng.latitude.toString()
                longitudForm = latLng.longitude.toString()
            }

            override fun onMarkerDragStart(p0: Marker) {

            }

        })


    }

    override fun onClick(v: View?) {
        when (v?.id){
            (R.id.btnContinuar) -> {guardar(
                                    lugar.text.toString(),
                                    motivo.text.toString(),
                                    responsable.text.toString(),
                                    latitudForm.toString(),
                                    longitudForm.toString())
                val intentMainActivity = Intent(this, MainActivity::class.java)
                startActivity(intentMainActivity)
            }
        }
    }


}
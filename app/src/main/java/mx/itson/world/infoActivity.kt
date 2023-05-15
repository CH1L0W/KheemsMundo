package mx.itson.world

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import mx.itson.world.entidades.Visita


class infoActivity(context: Context, v: List<Visita>?, marker: String):GoogleMap.InfoWindowAdapter {
    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.activity_info, null)
    val visitas = v
    val posicion = marker
    var a: Int = 0
    var continuar: Boolean = true
    private fun rendowWindowText(marker: Marker, view: View ){


        val lugar = view.findViewById<TextView>(R.id.LugarText)
        val motivo = view.findViewById<TextView>(R.id.MotivoText)
        val responsable = view.findViewById<TextView>(R.id.ResponsableText)
        while(continuar){
            if(posicion.contains(a.toString())){
                lugar.text = visitas?.get(a)?.lugar
                motivo.text = visitas?.get(a)?.motivo
                responsable.text = visitas?.get(a)?.responsable
            }else{
                if(visitas?.size == a){
                    continuar = false
                }
            }
            a++
        }

    }

    override fun getInfoContents(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}
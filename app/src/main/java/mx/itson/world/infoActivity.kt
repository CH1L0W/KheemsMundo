package mx.itson.world

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class infoActivity(context: Context, l: String, m: String, r: String):GoogleMap.InfoWindowAdapter {
    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.activity_info, null)
    var l = l
    var m = m
    var r = r
    private fun rendowWindowText(marker: Marker, view: View ){

        val lugar = view.findViewById<TextView>(R.id.LugarText)
        val motivo = view.findViewById<TextView>(R.id.MotivoText)
        val responsable = view.findViewById<TextView>(R.id.ResponsableText)

        lugar.text = l
        motivo.text = m
        responsable.text = r
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
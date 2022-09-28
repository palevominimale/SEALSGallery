package app.seals.sealsgallery.domain.interfaces

import android.location.Location
import android.location.LocationListener

interface LocationRepository : LocationListener {

    override fun onLocationChanged(location: Location)

}
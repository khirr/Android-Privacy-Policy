package net.khirr.android.privacypolicy

import android.content.Context
import android.telephony.TelephonyManager
import java.util.*

private enum class EUCountry {
    AT, BE, BG, HR, CY, CZ, DK, EE, FI, FR, DE, GR, HU, IE, IT, LV, LT, LU, MT, NL, PL, PT, RO, SK, SI, ES, SE, GB, //28 member states
    GF, PF, TF, //French territories French Guiana, Polynesia, Southern Territories
    EL, UK, // alternative EU names for GR and GB
    ME, IS, AL, RS, TR, MK;
}

object EUHelper {

    fun isEu(context: Context): Boolean {
        return isEuByNetwork(context) || isEuBySim(context) || isEuByZoneId(context)
    }

    private fun contains(s: String): Boolean {
        for (country in EUCountry.values())
            if (country.name.equals(s, ignoreCase = true))
                return true
        return false
    }

    private fun isEuBySim(context: Context): Boolean {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            var simCountry: String? = tm.simCountryIso
            if (simCountry != null && simCountry.length == 2) {
                simCountry = simCountry.toUpperCase()
                if (contains(simCountry)) {
                    return true
                }
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    private fun isEuByNetwork(context: Context): Boolean {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA && tm.phoneType != TelephonyManager.PHONE_TYPE_NONE) {
                var networkCountry: String? = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) {
                    networkCountry = networkCountry.toUpperCase()
                    if (contains(networkCountry)) {
                        return true
                    }
                }
            }
            return false
        } catch (e: Exception) {
            return true
        }
    }

    private fun isEuByZoneId(context: Context): Boolean {
        try {
            val tz = TimeZone.getDefault().id.toLowerCase()
            if (tz.length < 10) {
                return true
            } else if (tz.contains("euro")) {
                return true
            }
        } catch (e: Exception) {
            return true
        }
        return false
    }
}
package com.nis.ccd.preference

import android.content.Context
import androidx.core.content.edit
import com.nis.ccd.BuildConfig

class PreferenceManager(private val context: Context) : IPreferenceManager {

    private val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    override fun saveAddress(address: String) {
        pref.edit {
            putString(ADDRESS, address).apply()
        }
    }

    override fun getAddress() = pref.getString(ADDRESS, "").toString()

    companion object {
        const val PREFERENCE_NAME = BuildConfig.APPLICATION_ID;
        const val ADDRESS = "address"
    }
}
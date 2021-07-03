package com.nis.ccd.preference

interface IPreferenceManager {

    fun saveAddress(address: String)
    fun getAddress(): String

}